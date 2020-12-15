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

    
    /** 
     * get the source key.
     * @return int
     */
    @Override
    public int getSrc() {
        return _src;
    }

    
    /** 
     * get the destination key.
     * @return int
     */
    @Override
    public int getDest() {
        return _dest;
    }

    
    /** 
     * get the edge's weight.
     * @return double
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    
    /**
     * get the edge's metadata.
     * @return String
     */
    @Override
    public String getInfo() {
        return _info;
    }

    
    /**
     * set the edge's metadata.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        _info = s;
    }

    
    /** 
     * get the edge's tag.
     * @return int
     */
    @Override
    public int getTag() {
        return _tag;
    }

    
    /** 
     * set the edge's tag.
     * @param t
     */
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