package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DWGraph_DS implements directed_weighted_graph {

    private HashMap<Integer,node_data> _v;
    private HashMap<Integer,Collection<Integer>> _in;
    private HashMap<Integer,HashMap<Integer,edge_data>> _out;
    private int _MC, _edges;

    public DWGraph_DS(){
        _v = new HashMap<>();
        _in = new HashMap<>();
        _out = new HashMap<>();
    }

    public DWGraph_DS(directed_weighted_graph g) {
        _v = new HashMap<>();
        _in = new HashMap<>();
        _out = new HashMap<>();

        for (node_data n : g.getV()) {
            this.addNode(new NodeData(n));
        }
        for (node_data n : g.getV()) {
            int nkey = n.getKey();
            if(g.getE(nkey) != null)
                for (edge_data d : g.getE(nkey)) {
                    this.connect(nkey, d.getDest(), d.getWeight());
                }
        }
	}

	
    /** 
     * retrieve a node by it's key
     * (if key doesn't exsist return null)
     * @param key
     * @return node_data
     */
    @Override
    public node_data getNode(int key) {
        return _v.get(key);
    }

    
    /** retrieve edge by it's vertex
     * (if the edge doesn't exsist return null)
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(!_out.containsKey(src)) {return null;}
        return _out.get(src).get(dest);
    }

    
    /** 
     * adding a node to the graph
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        int nKey = n.getKey();
        if(_v.put(nKey, n) == null) {_MC++;}

    }

    
    /** 
     * connects between two nodes and updates the weight of the new edge.
     * the implementation is two maps, one for outgoing edges, and the second for incoming edges.
     * the second one is being used in removeNode() only.
     * @param src
     * @param dest
     * @param w
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(src == dest || this.getNode(src) == null || this.getNode(dest) == null){
            return;
        }

        edge_data d = new EdgeData(src, dest, w);
        if(!_out.containsKey(src)){
            _out.put(src, new HashMap<Integer, edge_data>());
        }
        if(!_in.containsKey(dest)){
            _in.put(dest, new HashSet<>());
        }
        _out.get(src).put(dest, d);
        if(_in.get(dest).add(src)){
            _edges ++;
        }
        _MC++;
    }

    
    /** 
     * retrieve a collection of nodes in the graph.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return _v.values();
    }

    
    /** 
     * retrieve a collection of outgoing edges from node_id.
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if(this.getNode(node_id) == null)
            return null;
        if (_out.containsKey(node_id))
            return _out.get(node_id).values();
        return new LinkedList<edge_data>();
    }

    
    /** 
     * iteratiing over outgoing neighbors of the node,
     * removing the edge from their incoming edges
     * and removing the node key from the outgoing map.
     * lastly removing the node from the map of the nodes and returning it.
     * (if  doesnt exsist return null)
     * @param key
     * @return node_data
     */
    @Override
    public node_data removeNode(int key) {
        node_data removed = _v.remove(key);
        if(removed != null){
            if(_in.containsKey(key)){
                for (int ni : _in.get(key)) {
                    _out.get(ni).remove(key);
                }
                _edges -= _in.get(key).size();
                _in.remove(key);
            }
            _edges -= _out.get(key).size();
            _out.remove(key);
            _MC++;
        }
        return removed;
    }

    
    /**
     * remove edge from the graph
     * (if the edge doesn't exsist return null) 
     * @param src
     * @param dest
     * @return edge_data
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!_v.containsKey(src) || !_v.containsKey(dest)){
            return null;
        }
        edge_data removed = _out.get(src).remove(dest);
        if(removed != null) {
            _edges--;
            _MC++;
        }
        _in.remove(dest);
        return removed;
    }

    
    /** 
     * return the ammount of nodes in the graph
     * @return int
     */
    @Override
    public int nodeSize() {
        return _v.size();
    }

    
    /** 
     * return the ammount of edges in the graph
     * @return int
     */
    @Override
    public int edgeSize() {
        return _edges;
    }

    
    /** 
     * return the amount of changes made in the graph
     * @return int
     */
    @Override
    public int getMC() {
        return _MC;
    }

}
