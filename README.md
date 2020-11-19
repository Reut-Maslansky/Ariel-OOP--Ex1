# **OOP-Ex1: Weighted Graph**

## **About this project**
This project represents an implementation of an undirectional unweighted graph with different methods and algorithms.

## **The classes in this project**
- [ ] ***WGraph_DS-*** implements weighted_graph and node_info interfaces.
- [ ] ***WGraph_Algo-*** implements weighted_graph_algorithms interface.

In the diffrent classes I chose to use a HashMap in order to get a data of each vertex or edge in o(1).

## **Main algorithms:**
- ***copy-*** compute a deep copy of this weighted graph.
- ***isConnected-*** return if there is a valid path from every vertex to each other vertex in the graph.
- ***shortestPathDist-*** return the length of the shortest path between source vertex to the target vertex.
- ***shortestPath-*** return the shortest path between source vertex to the target vertex- as an ordered list of nodes:
*source--> n1--> n2--> ... -->target*
- ***save-*** saves this weighted graph to the given file name.
- ***load-*** loads a graph from the given file name to a graph on which We'll operates the algorithms above.

## **Dijkstra's algorithm**
In the development of the algorithm of shortestPath in the weighted graph, I base on Dijkstra's algorithm.
More about- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

## **To clone down this repository**
Write in your Git Bash the follow:

```
$ git clone https://github.com/Reut-Maslansky/OOP-Ex1.git
```

### *Example of Weighted Graph:*
![Example of Weighted Graph](https://media.geeksforgeeks.org/wp-content/uploads/graphhh.png)
