package api;

public class NodeData implements node_data{

    private int _key, _tag;
    private String _info;
    private double _weight;

    public NodeData(int key) {
        _key = key;
    }

    @Override
    public int getKey() {
        return _key;
    }

    @Override
    public geo_location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLocation(geo_location p) {
        // TODO Auto-generated method stub

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
}
