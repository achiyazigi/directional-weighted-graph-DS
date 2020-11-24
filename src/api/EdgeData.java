package api;

import java.util.Objects;

public class EdgeData implements edge_data {

    private int _src, _dest, _tag;
    private double _weight;
    private String _info;

    public EdgeData(int src, int dest, double w) {
        _src = src;
        _dest = dest;
        _weight = w;
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
        if(obj == null) return false;
        edge_data other = (edge_data)obj;
        if(_src == other.getSrc() && _dest == other.getDest() && _weight == other.getWeight())
            return true;
        return false;
    }
    
}