import random

"""
Code for solving considering the highest priotiry in the first cycle that randomly appears
"""
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

def max_list_val(L):
    maxim = 0
    for x in L:
        if int(x) > maxim:
            maxim = int(x)
    return maxim

def play_parity_game(graph, priorities):
    """
    Play the parity game.
    """
    current_node = '1'  # Starting node
    player = 0  # Player 0 starts
    L = []
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
                return print('player 0 wins')
            if maximum % 2 == 1:
                return print('player 1 wins')
            
            
        L.append(next_node)
        current_node = next_node
            

if __name__ == "__main__":
    graph, priorities = create_graph()
    play_parity_game(graph, priorities)
