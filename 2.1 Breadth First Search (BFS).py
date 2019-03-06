tree = {'S': ['A', 'B', 'C'],
        'A': ['S', 'D', 'E', 'G'],
        'B': ['S', 'G'],
        'C': ['S', 'G'],
        'D': ['A'],
        'E': ['A']}

tree2 = {'S': ['A', 'B'],
         'A': ['S'],
         'B': ['S', 'C', 'D'],
         'C': ['B', 'E', 'F'],
         'D': ['B', 'G'],
         'E': ['C'],
         'F': ['C']
         }


def BFS(array):
    global tree2
    index = 0               # record steps needed to achieve goal node
    nodes_layers = [['S']]  # record nodes in all visited layers
    solution = ['G']
    current_target = 'G'    # current node to find reversely in order to find the optimal path

    # Get visited nodes sequence
    while 'G' not in array:
        temp = []   # record nodes in each layer
        for item in tree2[array[index]]:
            if item in array:
                continue
            temp.append(item)
            array.append(item)
            if item == 'G':     # stop the loop if goal is finded
                break
        nodes_layers.append(temp)
        index += 1

    # get optimal path, starting from goal
    for i in range(index-1, 0, -1):
        for j in range(len(nodes_layers[i])):
            if current_target in tree2[nodes_layers[i][j]]:
                current_target = nodes_layers[i][j]
                solution.append(nodes_layers[i][j])
                break
    solution.append('S')    # append starting node to solution
    solution.reverse()      # reverse the solution array to get the path
    return solution, array


if __name__ == '__main__':
    solution, nodes_visited = BFS(['S'])
    print('Optimal solution: ' + str(solution))
    print('Visited nodes: ' + str(nodes_visited))
