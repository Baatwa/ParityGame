# -*- coding: utf-8 -*-
import random
import networkx as nx
import matplotlib.pyplot as plt

def create_graph():
    """
    Create a directed graph for the parity game.
    """
    graph = {
        '1': ['2', '3'],
        '2': ['4'],
        '3': ['5'],
        '4': ['1'],
        '5': ['6'],
        '6': ['7'],
        '7': ['3']
    }
    priorities = {
        '1': 1,
        '2': 2,
        '3': 3,
        '4': 4,
        '5': 5,
        '6': 6,
        '7': 7
    }
    return graph, priorities

def create_graph2():
    graph = {
        '1': ['2', '3'],
        '2': ['4', '5'],
        '3': ['6'],
        '4': ['7'],
        '5': ['8'],
        '6': ['9','10'],
        '7': ['10','8'],
        '8': ['11'],
        '9': ['12','6'],
        '10': ['13'],
        '11': ['14'],
        '12': ['15'],
        '13': ['15'],
        '14': ['13'],
        '15': ['4','13']
    }
    priorities = {
        '1': 1,
        '2': 2,
        '3': 3,
        '4': 4,
        '5': 5,
        '6': 6,
        '7': 7,
        '8': 8,
        '9': 9,
        '10': 10,
        '11': 11,
        '12': 12,
        '13': 13,
        '14': 14,
        '15': 15
    }
    return graph, priorities

def gen_connected_graph(nb_nodes):
    """
    Génère un graphe connexe avec le nombre de nœuds spécifié.

    Parameters
    ----------
    nb_nodes : int
        Nombre de nœuds dans le graphe.

    Returns
    -------
    G : dict
        Graphe représenté sous forme de dictionnaire.

    """
    if nb_nodes <= 0:
        return {}

    G = {str(0): []}  # Commencer avec un nœud
    
    priorities = dict()

    for i in range(nb_nodes): 
        priorities[str(i)] = i

    # Ajouter des nœuds et les connecter à des nœuds existants dans le graphe
    for i in range(1, nb_nodes):
        # Choisir un nœud existant aléatoire pour se connecter
        node_to_connect = str(random.randint(0, i - 1))
        # Connecter le nouveau nœud à un nœud existant
        G[str(i)] = [node_to_connect]
        # Mettre à jour les connexions du nœud connecté
        G[node_to_connect].append(str(i))

    return G, priorities



def gen_graph(nb_nodes,links,p = 0.5): 
    """
    Parameters
    ----------
    nb_nodes : integer
        number of nodes in the graph.
    links : integer
        number of links added over the existing ones that guarantee the existence of an 'exit' from every node.
    p : float, optional
        probability of adding a link for each node in ascending order. The default is 0.5.

    Returns
    -------
    G : dictionnary
        graph.
    priorities : dictionnary
        priorities of nodes.

    """
    flag = 0
    G = dict()
    priorities = dict()

    
    for i in range(nb_nodes): # There is always an outgoing edge for each vertex. The play can't be 'stuck'.
        nb = list(range(0, nb_nodes))
        nb.remove(i)
        G[str(i)] = [str(random.choice(nb))]
        priorities[str(i)] = i
        
    while flag < links:
        
        for i in range(nb_nodes):
            test = random.random()
            if test < p:
                nb = list(range(0, nb_nodes))
                for x in G[str(i)]:
                    nb.remove(int(x))
                if nb:
                    new = str(random.choice(nb))
                    target = random.choice(nb)
                    if str(target) not in G[str(i)] and str(i) not in G.get(str(target), []):
                        G.setdefault(str(i), []).append(str(target))
                        flag += 1
                        
                
    return G, priorities


def max_list_val(L):
    """
    Parameters
    ----------
    L : list

    Returns
    -------
    maxim : integer
        Highest value in the list L

    """
    maxim = 0
    for x in L:
        if int(x) > maxim:
            maxim = int(x)
    return maxim

def play_parity_game(graph, priorities):
    """
    Play the parity game considering the highest priority in the first formed cycle
    """
    G = nx.DiGraph(graph)
    pos = nx.spring_layout(G)
    
    current_node = '1'  # Starting node
    
    L = []
    
    # Visual representation of the graph and solving 
    nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
    nx.draw_networkx_nodes(G, pos, nodelist=[current_node], node_color='red', node_size=500) # current node at each step is red
    plt.show()
    
    while True:
        print("Current node:", current_node)
        random_next = random.randint(0,len(graph[current_node])-1)
        print(random_next)
        next_node = graph[current_node][random_next]
        
        if next_node in L:
            ind = L.index(next_node)
            temp = L[ind:]
            print(temp)
            maximum = max_list_val(temp)
            print(maximum)
            if maximum % 2 == 0:
                print('player 0 wins')
                break
            if maximum % 2 == 1:
                print('player 1 wins')
                break
            
        L.append(next_node)
        current_node = next_node
        
        nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
        nx.draw_networkx_nodes(G, pos, nodelist=[current_node], node_color='red', node_size=500)
        plt.show()
    
    # Last node that closes the cycle
    nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
    nx.draw_networkx_nodes(G, pos, nodelist=[next_node], node_color='red', node_size=500)
    plt.show()
    
exemple = {'0' : ['3','1'],
          '1' : ['4','6'],
          '2' : ['0','5'],
          '3' : ['2'],
          '4' : ['0','1'],
          '5' : ['8'],
          '6' : ['8','1'],
          '8' : ['2','5']}

exemple2 = {'0' : ['2'],
          '1' : ['5'],
          '2' : ['0'],
          '4' : ['5'],
          '3' : ['2'],
          '5' : ['1']}

exemple3 = {'0' : ['1','4'],
          '1' : ['5','0'],
          '2' : ['0','4'],
          '4' : ['2'],
          '3' : ['1'],
          '5' : ['1','3']}

priorities = dict()

for i in range(len(exemple3.keys())): 
    priorities[str(i)] = i
"""
for i in range(len(bidule.keys())+1):
    if i != 7:
        priorities[str(i)] = i
"""


if __name__ == "__main__":
    #graph, priorities = gen_connected_graph(15)
    #graph, priorities = gen_graph(10,10)
    #graph, priorities = create_graph2()
    graph = exemple3
    play_parity_game(graph, priorities)
   

def Zielonka_attractor(graph, U, player, draw = False):
    """
    Parameters
    ----------
    graph : dict
        graph of the parity game.
    U : set
        subset of the nodes of the graph.
    player : bool
        player 0 or player 1.

    Returns
    -------
    Attr_n1 : set
        attractor of the input 'player'.

    """
    
    G = nx.DiGraph(graph)
    pos = nx.spring_layout(G)
    if draw :
        current_node = list(U)
        
        nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
        nx.draw_networkx_nodes(G, pos, nodelist=current_node, node_color='red', node_size=500) # current node at each step is red
        plt.show()
    
    V = set(graph.keys())
    
    V_p = set() # Set of elements of 'player'
    V_opp = set() # Set of elements of 'opponent'
    for x in V:
        if int(x)%2 == player:
            V_p.add(x)
        else:
            V_opp.add(x)
    
    Attr_n1 = U.copy()
    
    Attr_n = set()
    
    
    while Attr_n1 != Attr_n: # Continue until Attr reach a stationary state
        Attr_n = Attr_n1.copy()
        for u in V_p:
            if any(v in Attr_n for v in graph.get(u)): # checks whether v is a successor of u
                Attr_n1.add(u)
                if draw :
                    current_node.append(u)
                    nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
                    nx.draw_networkx_nodes(G, pos, nodelist=current_node, node_color='orange', node_size=500) # current node at each step is red
                    plt.show()
                
        for u in V_opp:
            if all(v in Attr_n for v in graph.get(u)): # checks if all successors v of node u are in the attractor.
                Attr_n1.add(u)
                if draw :
                    current_node.append(u)
                    nx.draw(G, pos, with_labels=True, arrows=True, node_color='skyblue')
                    nx.draw_networkx_nodes(G, pos, nodelist=current_node, node_color='orange', node_size=500) # current node at each step is red
                    plt.show()
                
    return Attr_n1



def ZIELONKA(G, priorities):
    """

    Parameters
    ----------
    G : dict
        graph of the parity game.
    priorities : dict
        priorities.

    Returns
    -------
    set
        Winning set of player 1.
    set
        Winning set of player 2.

    """
    print(G)
    if not G:
        print('VIDE')
        return set(), set()  
    else:
        m = int(max(priorities.values()))
        if m % 2 == 0:
            p1 = 0
        else:
            p1 = 1
        U = set([str(priorities[str(m)])])
        A = Zielonka_attractor(G, U, p1)
        print('Attracteur A :')
        print(A)
        if p1 == 0:
            W_prime_a, W_prime_b = ZIELONKA({key: [value for value in values if value not in A] for key, values in G.items() if key not in A}, {key: values for key, values in priorities.items() if key not in A})
            print('W_primea,w_prime_b  A1' )
            print(W_prime_a, W_prime_b)
        else:
            W_prime_b, W_prime_a = ZIELONKA({key: [value for value in values if value not in A] for key, values in G.items() if key not in A}, {key: values for key, values in priorities.items() if key not in A})
            print('W_primea,w_prime_b A2')
            print(W_prime_a, W_prime_b)
        if not W_prime_b:
            W_p1 = A.union(W_prime_a)  
            W_p2 = set()  
        else:
           
            if p1 == 0:
                B = Zielonka_attractor(G, W_prime_b, 1 - p1)
                print('Attracteur B :')
                print(B)
                W_prime_a, W_prime_b = ZIELONKA({key: [value for value in values if value not in B] for key, values in G.items() if key not in B}, {key: values for key, values in priorities.items() if key not in B})  
                print('W_primea,w_prime_b B1')
                print(W_prime_a, W_prime_b)
                W_p1 = W_prime_a
                W_p2 = W_prime_b.union(B)
            else:
                B = Zielonka_attractor(G, W_prime_b, 1 - p1)
                print('Attracteur B :')
                print(B)
                W_prime_b, W_prime_a = ZIELONKA({key: [value for value in values if value not in B] for key, values in G.items() if key not in B}, {key: values for key, values in priorities.items() if key not in B})
                print('W_primea,w_prime_b B2')
                print(W_prime_a, W_prime_b)
                W_p2 = W_prime_b
                W_p1 = W_prime_a.union(B)
        return W_p1, W_p2



        
    
    
    
    










