package gameClient;

import api.GeoLocation;
import api.geo_location;
import api.node_data;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private int _id;
    private Pokemon _target;
    private List<node_data> _path;
    private node_data _current_node;
    private geo_location _pos;
    private boolean _available = true;
    private double _speed;
    private double _value;

    public Agent(int id, Pokemon pokemon) {
        _speed = 1;
        _id = id;
        _target = pokemon;
        _path = new LinkedList<>();
        if (_target != null) {
            _current_node = Myclient.g.getNode(_target.get_edge().getSrc());
            _pos = _current_node.getLocation();
            _available = false;
            _path.add(Myclient.g.getNode(_target.get_edge().getDest()));
        } else {
            _current_node = Myclient.g.getV().iterator().next();
            _pos = _current_node.getLocation();
        }
    }


    public int get_id() {
        return _id;
    }


    public Pokemon get_target() {
        return _target;
    }

    public void set_target(Pokemon target) {
        _target = target;
        if (_target == null) {
            _available = true;
        } 
        else {   // cur -> pathToTargetSrc... -> targetDest
            _available = false;
            _path = Myclient.ga.shortestPath(_current_node.getKey(), Myclient.g.getNode(_target.get_edge().getSrc()).getKey());
            _path.add(Myclient.g.getNode(_target.get_edge().getDest()));
        }
    }


    public node_data get_current_node() {
        return _current_node;
    }

    public List<node_data> get_path() {
        return _path;
    }

    public node_data nextNode() {
        if (_path.isEmpty() || !Myclient.arena.get_pokemons().contains(_target)) {
            _available = true;
        } else {
            _current_node = _path.remove(0);
        }
        return _current_node;
    }

    public boolean isAvailable() {
        return _available;
    }

    public boolean isOnEdge() {
        return _pos.x() != _current_node.getLocation().x() ||
               _pos.y() != _current_node.getLocation().y() ||
               _pos.z() != _current_node.getLocation().z() ;

    }

    public geo_location get_pos() {
        return _pos;
    }

    public void set_pos(String[] split) {
        _pos = new GeoLocation(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    public double get_speed() {
        return _speed;
    }

    public void set_speed(double speed) {
        _speed = speed;
    }

    public double get_value() {
        return _value;
    }

    public void set_value(double value) {
        _value = value;
    }
}
