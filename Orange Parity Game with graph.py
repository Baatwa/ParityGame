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
        '9': ['12'],
        '10': ['13'],
        '11': ['14'],
        '12': ['15'],
        '13': ['15'],
        '14': ['13'],
        '15': ['4']
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

    
    for i in range(nb_nodes):
        nb = list(range(0, nb_nodes))
        nb.remove(i)
        G[str(i)] = [str(random.choice(nb))]
        priorities[str(i)] = [str(i)]
        
    while flag < links:
        
        for i in range(nb_nodes):
            test = random.random()
            if test < p:
                nb = list(range(0, nb_nodes))
                for x in G[str(i)]:
                    nb.remove(int(x))
                G[str(i)].append(str(random.choice(nb)))
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
    player = 0  # Player 0 starts
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

if __name__ == "__main__":
    graph, priorities = gen_graph(10, 6)
    play_parity_game(graph, priorities)
