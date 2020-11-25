package api;

public class NodeData implements node_data, Comparable {

    private int _key, _tag;
    private String _info;
    private double _weight;
    private geo_location g;

    public NodeData(int key) {
        _key = key;
    }

    public NodeData(node_data n) {
        _key = n.getKey();
        _info = n.getInfo();
        _tag = n.getTag();
        _weight = n.getWeight();
	}

	@Override
    public int getKey() {
        return _key;
    }

    @Override
    public geo_location getLocation() {
        if (g != null) {
            return g;
        }
        return null;
    }

    @Override
    public void setLocation(geo_location p) {
        this.g = p;
    }

    @Override
    public double getWeight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
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
        return Integer.hashCode(_key);
    }

    @Override
    public int compareTo(Object o) {
        NodeData n = (NodeData)o;
        return (int)(_weight-n._weight);
    }
}