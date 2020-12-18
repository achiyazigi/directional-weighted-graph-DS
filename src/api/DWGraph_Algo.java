package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _g;

    /**
     * init target graph
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        _g = g;
    }

    /**
     * returns the initialized graph
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return _g;
    }

    /**
     * perform a deep copy of the graph by using NodeData and DWGraph_DS copy constructors
     * and basically building a new graph from scratch.
     * @return directed_weighted_graph
     */
    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(_g);
    }

    /**
     * running BFS but each edge u,v we check if there's a path back from v to u
     * (not necessarily the shortest one)
     * @return boolean
     */
    @Override
    public boolean isConnected() {
        if (_g.nodeSize() == 0 || _g.nodeSize() == 1) {
            return true;
        }
        if (this._g.nodeSize() > this._g.edgeSize() + 1) { //the minimum number of edges for a connected graph, shallow chek.
            return false;
        }
        if (reset_and_lonely_check()) {//reset all the Tags in the graph to -1 and check if there is a lonely node.
            return false;            // if there is a lonely node return false immediately.
        }

        Queue<node_data> q = new LinkedList<>();
        q.add(_g.getV().iterator().next());
        int counter = 0;
        node_data cur = q.peek();
        cur.setTag(0);

        while (!q.isEmpty()) {
            cur = q.poll();
            counter++;
            for (edge_data i : _g.getE(cur.getKey())) {
                node_data dest = _g.getNode(i.getDest());
                if (dest.getTag() == -1) {
                    if (!path(dest.getKey(), cur.getKey())) {
                        return false;
                    }
                    dest.setTag(0);
                    q.add(dest);
                }
            }
        }
        return _g.getV().size() == counter;
    }

    /**
     * returns true iff there's a path from src to dest.
     * DFS concept implementation (with stack)
     * @param src
     * @param dest
     * @return boolean
     */

    public boolean path(int src, int dest) {
        node_data cur = _g.getNode(src);
        Stack<node_data> s = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        visited.add(src);
        s.add(cur);
        while (!s.empty()) {
            cur = s.pop();
            for (edge_data d : _g.getE(cur.getKey())) {
                node_data next = _g.getNode(d.getDest());
                if (!visited.contains(next.getKey())) {
                    visited.add(next.getKey());
                    s.add(next);
                }
                if (next.getKey() == dest)
                    return true;
            }
        }
        return false;
    }

    /**
     * 1. Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
     * 2. Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. Set the initial node as current.
     * 3. For the current node, consider all of its unvisited neighbours and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with a neighbour B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
     * 4. When we are done considering all of the unvisited neighbours of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
     * 5. If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
     * 6. Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new "current node", and go back to step 3.
     */

    @Override
    public double shortestPathDist(int src, int dest) {
        this.reset();
        node_data destination = _g.getNode(dest);
        _g.getNode(src).setWeight(0);
        PriorityQueue<node_data> q = new PriorityQueue<>(new Comparator<node_data>(){

            @Override
            public int compare(node_data o1, node_data o2) {
                double delta = o1.getWeight()-o2.getWeight();
                if(delta > 0) return 1;
                if(delta < 0) return -1;
                return 0;
            }
        });
        q.add(_g.getNode(src));
        while (!q.isEmpty()) {
            node_data cur = q.poll();
            int curkey = cur.getKey();
            for (edge_data d : _g.getE(curkey)) {
                node_data ni = _g.getNode(d.getDest());
                double distance = d.getWeight() + cur.getWeight();
                if (ni.getWeight() > distance) {
                    ni.setWeight(distance);
                    ni.setInfo("" + curkey);
                    if (!q.contains(ni)) {
                        q.add(ni);
                    }
                }

            }
        }
        if (destination.getWeight() < Double.MAX_VALUE) {
            return destination.getWeight();
        }
        return -1;
    }

    private void reset() {
        for (node_data n : _g.getV()) {
            n.setWeight(Double.MAX_VALUE);
            n.setInfo("");
        }
    }

    /**
     * reset for isConnected(). it's also checking for lonely nodes in the graph.
     * @return boolean
     */
    private boolean reset_and_lonely_check() {
        for (node_data n : _g.getV()) {
            n.setTag(-1);
            if (_g.getE(n.getKey()).size() == 0) { //check if there is a lonely node --> false
                return true;
            }
        }
        return false;
    }

    /**
     * backtracking after running shortestPathDist().
     * each node holdes the key exposed it. so starting from dest,
     * adding to the head of the list the key stored in node.info() recursively untill reaching src.
     * @param src
     * @param dest
     * @return List<node_data>
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (this.shortestPathDist(src, dest) == -1) {
            return null;
        }
        LinkedList<node_data> res = new LinkedList<>();
        node_data cur = _g.getNode(dest);
        while (cur.getKey() != src) {
            res.addFirst(cur);
            cur = _g.getNode(Integer.parseInt(cur.getInfo()));
        }
        res.addFirst(cur);
        return res;
    }

    /**
     * save the graph in a json format using Gson lib.
     * @param file
     * @return boolean
     */
    @Override
    public boolean save(String file) {
        JsonObject jsonObject = new JsonObject();
        JsonArray Nodes = new JsonArray();
        JsonArray Edges = new JsonArray();

        for (node_data n : _g.getV()) {
            JsonObject node = new JsonObject();
            if (n.getLocation() != null) {
                String loc = n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z();
                node.addProperty("pos", loc);
                node.addProperty("id", n.getKey());
                Nodes.add(node);
            }

            for (edge_data e : _g.getE(n.getKey())) {
            JsonObject edge = new JsonObject();
            if (e != null){
                    edge.addProperty("src",e.getSrc());
                edge.addProperty("w",e.getWeight());
                edge.addProperty("dest",e.getDest());
                }
                Edges.add(edge);
            }
        }
        jsonObject.add("Edges",Edges);
        jsonObject.add("Nodes",Nodes);


        Gson g = new  GsonBuilder().create();
         String json = g.toJson(jsonObject);
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * loads a graph from json test using Gson lib.
     * @param file
     * @return boolean
     */
    @Override
    public boolean load(String file) {

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new JsonGraph());
            Gson gson = builder.create();

            FileReader reader = new FileReader((file));
            _g = gson.fromJson(reader, directed_weighted_graph.class);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
