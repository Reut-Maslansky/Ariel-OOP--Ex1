package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class represents an Undirected (positive) Weighted Graph Theory algorithms including:
 *      0. clone();
 *      1. init(graph);
 *      2. isConnected();
 *      3. double shortestPathDist(int src, int dest);
 *      4. List<node_data> shortestPath(int src, int dest);
 *      5. Save(file);
 *      6. Load(file);
 *
 * @param: gr- a graph on which We'll operates the algorithms.
 *
 * @author Reut-Maslansky
 */
public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph gr;

    /**
     * Default constructor
     */
    public WGraph_Algo() {
        this.gr = new WGraph_DS();
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.gr = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return gr;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        if (gr == null) return null;
        weighted_graph g = new WGraph_DS(this.gr);
        return g;
    }

    /**
     * Returns true if and only if there is a valid path from EVREY node to each other node.
     *
     * this method based on BFS Algorithm.
     * We use the private function- bfsInt-
     * that will return the number of times we changed the vertex info,
     * and thus we will know if during the iterations we went over the amount of vertices that exist in the graph.
     *
     * In this operation, of comparing the counter with the number of vertices in the graph -
     * we saved another pass over all the vertices in the graph.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (gr == null) return false;

        //if graph contain N vertexes and less than N-1 edges the graph can't be connect.
        if (this.gr.edgeSize() < (this.gr.getV().size() - 1)) return false;
        if (this.gr.getV().size() < 2) return true;

        //Choose a idNode we will check from him and do on him bfs algo
        Iterator<node_info> it = this.gr.getV().iterator();
        node_info temp = it.next();

        int count = bfsInt(temp.getKey());
        return (count == gr.getV().size());
    }

    /**
     * Returns the length of the shortest path between src to dest
     * if no such path, returns -1
     *
     * @param src - start node
     * @param dest - end (target) node
     * @return
     *
     * This method based on Dijkstra Algorithm.
     * The data of this algorithm will save in a temporal Node-
     * 1. The shortest weight from the source vertex to this vertex.
     * 2. The vertex neighbor that connect this vertex and update him this value of weight accepted.
     *
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (gr == null) return -1;
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return -1;
        if (src == dest) return 0;

        //dij return the length of the shortest path between src to dest for dest-node.
        HashMap<Integer, Node_Temp> temp = dij(src, dest);

        //If dest.visit will be 0 we will understand that not exist a path between this nodes.
        if (temp.get(dest).visit == 0) return -1;

        return temp.get(dest).tag;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     *
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     *
     *
     * This method based on Dijkstra Algorithm.
     * We use the private function- dij- that will return a HashMap.
     *
     * In this HasMap, the data of this algorithm will save in a temporal Node- contains:
     *      1. The shortest weight from the source vertex to this vertex.
     *      2. The vertex neighbor that connect this vertex and update him this value of weight accepted.
     *
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if (gr == null) return null;
        if (!(this.gr.getV().contains(gr.getNode(src))) || !(this.gr.getV().contains(gr.getNode(dest)))) return null;

        HashMap<Integer, Node_Temp> temp = dij(src, dest);

        if (temp.get(dest).visit == 0) return null;

        //We will return this list of path.
        LinkedList<node_info> list = new LinkedList<>();

        node_info current = this.gr.getNode(dest);
        list.add(current);

        if (src == dest) return list;

        // loop: check who is the "parent node" of each node.

        Node_Temp nt, parent;

        while (temp.get(current.getKey()).parentId != -1) {
            nt = temp.get(current.getKey());
            parent = temp.get(nt.parentId);
            current = gr.getNode(parent.idNode);
            list.addFirst(current);
        }
        return list;
    }

    /**
     * Saves this weighted (undirected) graph to the given file name.
     *
     * @param file - the file name (may include a relative path).
     * @return true - if the file was successfully saved
     *
     */
    @Override
    public boolean save(String file) {
        System.out.println("Starting serialize to " + file + "\n");
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOutput);
            out.writeObject((WGraph_DS) getGraph());
            out.close();
            fileOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Serialized data is saved in " + file);
        return true;

    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - if the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {

        try {
            System.out.println("start" + "\n");
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            weighted_graph wgraph = (WGraph_DS) in.readObject();
            in.close();
            fileIn.close();
            init(wgraph);
            System.out.println("end");
        } catch (Exception i) {
            i.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * bfsInt will pass over the nodes.
     *
     * @Return how many nodes we marked- then we will know if we pass all the nodes in the graph.
     *
     * The function receives a vertex key from which we will perform the test on the graph connected test.
     * During the test we will mark the vertices in the graph in the "info" that each vertex holds.
     * At the end we will return the number of times we changed the vertex info,
     * and thus we will know if during the iterations we went over the amount of vertices that exist in the graph.
     * In this operation, of comparing the counter with the number of vertices in the graph -
     * we saved another pass over all the vertices in the graph - in a loop of o(v) where v is the number of vertices in the graph.
     */
    private int bfsInt(int key) {

        //reset info&tag
        for (node_info n : gr.getV()) {
            n.setInfo("");
            n.setTag(0);
        }

        // init counter
        // When we pass over a node we will do count++ and then we could check if we pass all the nodes.
        int count = 0;

        // Init node that we will start with him.
        node_info start = this.gr.getNode(key);

        Queue<node_info> q = new LinkedList<>();

        // Work on start's neighbors.
        // We marked "red" a node that we work on him now.

        for (node_info n : this.gr.getV(key)) {
            n.setInfo("red");
            q.add(n);
        }

        // We marked "blue" a node that we finish work with him.
        start.setInfo("blue");
        count++;

        while (!q.isEmpty()) {
            node_info current = q.poll();
            for (node_info n : this.gr.getV(current.getKey())) {
                if ((n.getInfo().equals(""))) {
                    q.add(n);
                    n.setInfo("red");
                }
            }

            current.setInfo("blue");
            count++;
        }
        return count;
    }

    /**
     * This algorithm based on Dijkstra Algorithm.
     *
     * The data of this algorithm will save in a temporal Node-
     *      1. tag- The shortest weight from the source vertex to this vertex.
     *      2. parent- The vertex neighbor that connect this vertex and update him this value of weight accepted.
     *      (By default the vertex tag contains Max_Value_Int and the vertex parent points as a vertex with key -1).
     *
     * The function goes through all the neighbors of the source vertex and does them "weight relief" if necessary-
     * that is, if we have reached a vertex whose weight can be reduced relative to its current weight,
     * we will update the information contained the temporal vertex.
     *
     *
     * The implementation of the algorithm is done by a priority queue,
     * which is implementation by a function of comparing the weights (tag) of the temporal vertexes in the queue.
     *
     * @return a HashMap with all the temporal vertexes respectively-
     * so that we can retrieve the information about each vertex in the most efficient and convenient way.
     */
    private HashMap<Integer, Node_Temp> dij(int src, int dest) {

        HashMap<Integer, Node_Temp> temp = new HashMap<>();

        // init NodeTemp
        for (node_info n : gr.getV()) {
            Node_Temp a = new Node_Temp(n.getKey());
            temp.put(a.idNode, a);
        }

        // We define Node_Temp.tag to be the length of the shortest path
        // from the src vertex to the current vertex as of this moment.

        // the shortest length of src-->src is 0.
        temp.get(src).tag = 0;

        PriorityQueue<Node_Temp> q = new PriorityQueue(gr.nodeSize(), new NodeComparator());

        q.add(temp.get(src));

        while (!q.isEmpty()) {
            Node_Temp current = q.poll();
            int keyC = current.idNode;

            // We define Node_Temp.visit to check if we done with this Node and his current length is final.
            if (current.visit != 1) {

                // We pass over the neighbors of the current Node.
                for (node_info n : this.gr.getV(keyC)) {
                    Node_Temp b = temp.get(n.getKey());
                    if (b.visit != 1) {

                        // Check if we need to update the shortest length.
                        if (b.tag > current.tag + gr.getEdge(n.getKey(), keyC)) {
                            b.tag = current.tag + gr.getEdge(n.getKey(), keyC);
                            b.parentId = keyC;
                        }
                        q.add(b);
                    }
                }
                current.visit = 1;
                if (current.idNode == dest) return temp;
            }
        }
        return temp;
    }

    /**
     * Node_Temp class
     *
     * this class will help implement Dijkstra Algorithm.
     * */
    private class Node_Temp {
        private int idNode;
        private int visit;
        private double tag;
        private int parentId;

        public Node_Temp(int key) {
            this.idNode = key;
            this.visit = 0;
            this.tag = Integer.MAX_VALUE;
            this.parentId = -1;
        }
    }

    private class NodeComparator implements Comparator<Node_Temp> {
        public int compare(Node_Temp n1, Node_Temp n2) {
            if (n1.tag < n2.tag)
                return -1;
            else if (n1.tag > n2.tag)
                return 1;
            return 0;
        }
    }
}
