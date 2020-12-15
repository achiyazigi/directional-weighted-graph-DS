package api;

public class NodeData implements node_data, Comparable {

    private int _key, _tag = -1;
    private String _info;
    private double _weight;
    private geo_location _geo;

    public NodeData(int key) {
        _key = key;
        _geo = new GeoLocation(0.0, 0.0, 0.0);
    }

    public NodeData(node_data n) {
        _key = n.getKey();
        _info = n.getInfo();
        _tag = n.getTag();
        _weight = n.getWeight();
        _geo = new GeoLocation(n.getLocation());
    }

    
    /**
     * returns the key 
     * @return int
     */
    @Override
    public int getKey() {
        return _key;
    }

    
    /** 
     * returns the position of the node 
     * @return geo_location
     */
    @Override
    public geo_location getLocation() {
        if (_geo != null) {
            return _geo;
        }
        return null;
    }

    
    /**
     * set the position of the node.
     * @param p
     */
    @Override
    public void setLocation(geo_location p) {
        this._geo = p;
    }

    
    /** 
     * get the node's weight.
     * @return double
     */
    @Override
    public double getWeight() {
        return _weight;
    }

    
    /** 
     * set the node's weight.
     * @param w
     */
    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    
    /** 
     * get node's matadata
     * @return String
     */
    @Override
    public String getInfo() {
        return _info;
    }

    
    /** 
     * set node's matadata
     * @param s
     */
    @Override
    public void setInfo(String s) {
        _info = s;
    }

    
    /** 
     * get the node's tag
     * @return int
     */
    @Override
    public int getTag() {
        return _tag;
    }

    
    /** 
     * set the node's tag
     * @param t
     */
    @Override
    public void setTag(int t) {
        _tag = t;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(_key);
    }

    @Override
    public int compareTo(Object o) {
        NodeData n = (NodeData) o;
        return (int) (_weight - n._weight);
    }
}
