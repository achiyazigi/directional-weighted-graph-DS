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

	@Override
    public node_data getNode(int key) {
        return _v.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(!_out.containsKey(src)) {return null;}
        return _out.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        int nKey = n.getKey();
        if(_v.put(nKey, n) == null) {_MC++;}

    }

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

    @Override
    public Collection<node_data> getV() {
        return _v.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(this.getNode(node_id) == null)
            return null;
        if (_out.containsKey(node_id))
            return _out.get(node_id).values();
        return new LinkedList<edge_data>();
    }

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

    @Override
    public int nodeSize() {
        return _v.size();
    }

    @Override
    public int edgeSize() {
        return _edges;
    }

    @Override
    public int getMC() {
        return _MC;
    }
    
}
