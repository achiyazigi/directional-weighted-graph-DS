package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class DWGraph_DS implements directed_weighted_graph {

    
    private class EdgeData implements edge_data{

        private int _src, _dest, _tag;
        private double _weight;
        private String _info;
    
        public EdgeData(int src, int dest, double w) {
            _src = src;
            _dest = dest;
            _weight = w;
            // maybe we should reset tag = 0 && info = ""
        }

        @Override
        public int getSrc() {
            return _src;
        }
    
        @Override
        public int getDest() {
            return _dest;
        }
    
        @Override
        public double getWeight() {
            return _weight;
        }
    
        @Override
        public String getInfo() {
            return _info;
        }
    
        @Override
        public void setInfo(String s) {
            _info = s;
        }
    
        @Override
        public int getTag() {
            return _tag;
        }
    
        @Override
        public void setTag(int t) {
            _tag = t;
        }

        @Override
        public int hashCode() {
            return Objects.hash(_src, _dest, _weight);
        }
        @Override
        public boolean equals(Object obj) {
            if(obj == null) {return false;}
            edge_data other = (edge_data)obj;
            if(_src == other.getSrc() && _dest == other.getDest() && _weight == other.getWeight()){
                return true;}
            return false;
        }
        
    }
    //============================================================================

    private HashMap<Integer,node_data> _v;
    private HashMap<Integer,Collection<Integer>> _in;
    private HashMap<Integer,HashMap<Integer,edge_data>> _out;
    private int _MC;

    public DWGraph_DS(){
        _v = new HashMap<>();
        _in = new HashMap<>();
        _out = new HashMap<>();
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
        if(_v.put(nKey, n) == null) {_MC++;} // it should be if != null...?

    }

    @Override
    public void connect(int src, int dest, double w) {
        if(src == dest || this.getNode(src) == null || this.getNode(dest) == null){
        return;}

        edge_data d = new EdgeData(src, dest, w);
        if(!_out.containsKey(src)){
            _out.put(src, new HashMap<Integer, edge_data>());
        }
        if(!_in.containsKey(dest)){
            _in.put(dest, new LinkedList<>());
        }
        _out.get(src).put(dest, d);
        _in.get(dest).add(src);
        _MC++;
    }

    @Override
    public Collection<node_data> getV() {
        return _v.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return _out.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        _out.remove(key);
        if(_in.containsKey(key)){
            for (int ni : _in.get(key)) {
                _out.get(ni).remove(key);
            }
            _in.remove(key);
        }
        node_data removed = _v.remove(key);
        if(removed != null){
            _MC++;}
        return removed;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data removed = _out.get(src).remove(dest);
        if(removed != null){
            _MC++;}
        _in.remove(dest);
        return removed;
    }

    @Override
    public int nodeSize() {
        return _v.size();
    }

    @Override
    public int edgeSize() {
        return _out.values().size();
    }

    @Override
    public int getMC() {
        return _MC;
    }
    
}
