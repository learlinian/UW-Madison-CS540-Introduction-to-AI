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


def DFS(array):
    global visited_list, tree
    # if all the children of current node has already been visited
    # or has no children, dequeue the last node in array and rerun
    # function DFS
    if set(tree[array[-1]]).issubset(visited_list):
        del array[-1]
        return DFS(array)

    for item in tree[array[-1]]:
        if item in visited_list:
            continue
        visited_list.append(item)
        array.append(item)
        if item == 'G':
            return array, visited_list
        else:
            return DFS(array)


if __name__ == '__main__':
    visited_list = ['S']
    solution, visited_nodes = DFS(['S'])
    print('Optimal solution: ' + str(solution))
    print('visited nodes: ' + str(visited_nodes))
