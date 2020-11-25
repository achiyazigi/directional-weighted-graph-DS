package api;

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
        // TODO Auto-generated method stub
        // TODO kaki
        return false;
    }

    /**
     * 1. Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
     * 2. Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. Set the initial node as current.
     * 3. For the current node, consider all of its unvisited neighbours and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one. For example, if the current node A is marked with a distance of 6, and the edge connecting it with a neighbour B has length 2, then the distance to B through A will be 6 + 2 = 8. If B was previously marked with a distance greater than 8 then change it to 8. Otherwise, the current value will be kept.
     * 4. When we are done considering all of the unvisited neighbours of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
     * 5. If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
     * 6. Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new "current node", and go back to step 3.
     * */

    @Override
    public double shortestPathDist(int src, int dest) {
        this.reset();
        node_data cur = _g.getNode(src);
        node_data destination = _g.getNode(dest);
        cur.setWeight(0);
        node_data minNode = null;
        while(destination.getTag() == -1){
            int curkey = cur.getKey();
            double min = Double.MAX_VALUE;
            // boolean flag = false;
            for (edge_data d : _g.getE(curkey)) {
                node_data ni = _g.getNode(d.getDest());
                if(ni.getTag() == -1){
                    double distance = d.getWeight() + cur.getWeight();
                    if(ni.getWeight() > distance){
                        ni.setWeight(distance);
                    }
                    if(min > ni.getWeight()){
                        min = ni.getWeight();
                        minNode = ni;
                    }
                }
            }

            cur.setTag(0);
            if(minNode == cur && curkey != dest)
                return -1;
            if(curkey != dest)
                minNode.setInfo(""+curkey);
            cur = minNode;

        }
        return destination.getWeight();
    }

    private void reset() {
        for (node_data n : _g.getV()) {
            n.setWeight(Double.MAX_VALUE);
            n.setTag(-1);
        }
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(this.shortestPathDist(src, dest) == -1){
            return null;
        }
        LinkedList<node_data> res = new LinkedList<>();
        node_data cur = _g.getNode(dest);
        while(cur.getKey() != src){
            res.addFirst(cur);
            cur = _g.getNode(Integer.parseInt(cur.getInfo()));
        }
        res.addFirst(cur);
        return res;
    }

    @Override
    public boolean save(String file) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean load(String file) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
