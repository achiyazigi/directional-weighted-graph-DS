package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph _g;

    @Override
    public void init(directed_weighted_graph g) {
        _g = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return _g;
    }

    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(_g);
    }

    @Override
    public boolean isConnected() {
        if (_g.nodeSize() == 0 || _g.nodeSize() == 1) {
            return true;
        }
        if (this._g.nodeSize() > this._g.edgeSize() + 1) { //the minimum number of edges for a connected graph, shallow chek.
            return false;
        }
        if (reset_and_lonely_check()) {//reset all the Tags in the graph to 0 and check if there is a lonely node.
            return false;            // if there is a lonely node return false immediately.
        }

        Queue<node_data> q = new LinkedList<>(); // Queue to store all the checked Nodes.
        q.add(_g.getV().iterator().next());
        int counter = 0;
        node_data cur = q.peek();
        cur.setTag(0);
        
        while (!q.isEmpty()) {
            cur = q.poll();
            counter++;
            for (edge_data i : _g.getE(cur.getKey())) { //check if V neighbor has been checked.
                node_data dest = _g.getNode(i.getDest());
                if (dest.getTag() == -1) {
                    if(!path(dest.getKey(),cur.getKey())){
                        return false;
                    }
                    dest.setTag(0);
                    q.add(dest);
                }
            }
        }
        return _g.getV().size() == counter;
    }

    public boolean path(int src, int dest) {
        node_data cur = _g.getNode(src);
        Stack<node_data> s = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        visited.add(src);
        s.add(cur);
        while(!s.empty()){
            cur = s.pop();
            for(edge_data d: _g.getE(cur.getKey())){
                node_data next = _g.getNode(d.getDest());
                if(!visited.contains(next.getKey())){
                    visited.add(next.getKey());
                    s.add(next);
                }
                if(next.getKey() == dest)
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
        PriorityQueue<NodeData> q = new PriorityQueue<>();
        q.add((NodeData) _g.getNode(src));
        while (!q.isEmpty()) {
            node_data cur = q.poll();
            int curkey = cur.getKey();
            for (edge_data d : _g.getE(curkey)) {
                NodeData ni = (NodeData) _g.getNode(d.getDest());
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

    private boolean reset_and_lonely_check() {
        for (node_data n : _g.getV()) {
            n.setTag(-1);
            if (_g.getE(n.getKey()).size() == 0) { //check if there is a lonely node --> false
                return true;
            }
        }
        return false;
    }

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

    @Override
    public boolean save(String file) {

        Gson g = new  GsonBuilder().create();
         String json = g.toJson(_g);

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

    @Override
    public boolean load(String file) {

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class, new JsonGraph() );
            Gson gson = builder.create();

            FileReader reader = new FileReader((file));
            _g = gson.fromJson(reader,directed_weighted_graph.class);

         return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
