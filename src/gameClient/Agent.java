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
    private int _src;

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


    
    /**
     * returns agent id
     * @return int
     */
    public int get_id() {
        return _id;
    }


    
    /** 
     * returns agent's pokemon target
     * @return Pokemon
     */
    public Pokemon get_target() {
        return _target;
    }

    
    /** 
     * set new agent's pokemon target
     * @param target
     */
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


    
    /** 
     * returns the node that the agent is going to.
     * (the agent dest in the server)
     * @return node_data
     */
    public node_data get_current_node() {
        return _current_node;
    }

    
    /** 
     * returns the planed shortest path to the target.
     * @return List<node_data>
     */
    public List<node_data> get_path() {
        return _path;
    }

    
    /** 
     * setting the next node in the agent's path to current agent's node,
     * removing it from the path and returning it.
     * @return node_data
     */
    public node_data nextNode() {
        if (_path.isEmpty() || !Myclient.arena.get_pokemons().contains(_target)) {
            _available = true;
            _path.clear();
            _target = null;
        } else {
            _current_node = _path.remove(0);
        }
        return _current_node;
    }

    
    /** 
     * check if the agent has a target
     * @return boolean
     */
    public boolean isAvailable() {
        return _available;
    }

    
    /** 
     * check if the agent is on edge
     * @return boolean
     */
    public boolean isOnEdge() {
        return _pos.x() != _current_node.getLocation().x() ||
               _pos.y() != _current_node.getLocation().y() ||
               _pos.z() != _current_node.getLocation().z() ;

    }

    
    /** 
     * returns agent's position
     * @return geo_location
     */
    public geo_location get_pos() {
        return _pos;
    }

    
    /**
     * set agent's position
     * @param split
     */
    public void set_pos(String[] split) {
        _pos = new GeoLocation(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    
    /** 
     * get agent's speed
     * @return double
     */
    public double get_speed() {
        return _speed;
    }

    
    /** 
     * set agent's speed
     * @param speed
     */
    public void set_speed(double speed) {
        _speed = speed;
    }

    
    /** 
     * get agent's collected scores
     * @return double
     */
    public double get_value() {
        return _value;
    }

    
    /** 
     * set agent's collected scores
     * @param _value
     */
    public void set_value(double value) {
        _value = value;
    }

    /**
     * returns the node key that the agent came from
     * @return int
     */
    public int get_src() {
        return _src;
    }

    /**
     * sets the node key that the agent came from
     */
    public void set_src(int src) {
        _src = src;
    }
}
