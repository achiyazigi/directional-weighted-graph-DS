package client;

import api.DWGraph_Algo;
import api.GeoLocation;
import api.dw_graph_algorithms;
import api.geo_location;
import api.node_data;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private int _id;
    private Pokemon _target;
    private List<node_data> _path;
    private node_data _current_node;
    private dw_graph_algorithms ga;
    private geo_location _pos;


    public Agent(int id, Pokemon pokemon) {
        ga = new DWGraph_Algo();
        ga.init(Myclient.g);
        _id = id;
        this.set_target(pokemon);
        if(_target != null) {
            _current_node = Myclient.g.getNode(_target.get_edge().getSrc());

        }
        else{
            _current_node = Myclient.g.getV().iterator().next();
            _pos = _current_node.getLocation();
        }
        _path = new LinkedList<>();
        if(_target != null)
            _path.add(Myclient.g.getNode(_target.get_edge().getDest()));
    }


    public int get_id() {
        return _id;
    }


    public Pokemon get_target() {
        return _target;
    }

    public void set_target(Pokemon _target) {
        this._target = _target;
        if(_target != null && _current_node != null)
            _path = ga.shortestPath(_current_node.getKey(), Myclient.g.getNode(_target.get_edge().getDest()).getKey());

    }

    public node_data get_current_node() {
        return _current_node;
    }

    public void set_current_node(node_data _current_node) {
        this._current_node = _current_node;
    }

    public List<node_data> get_path() {
        return _path;
    }

    public void set_path(List<node_data> _path) {
        this._path = _path;
    }

    public node_data nextNode(){
        _current_node = _path.remove(0); // might be an exception!!
        if(_current_node == null)
            Myclient.arena.get_pokemons().remove(_target);
        return _current_node;
    }

    public boolean isMoving() {
        return _current_node != null;
    }

    public boolean isOnEdge() {
        return isMoving() &&
               (_pos.x()!= _current_node.getLocation().x() ||
               _pos.y() != _current_node.getLocation().y() ||
               _pos.z() != _current_node.getLocation().z());
    }

	public void set_pos(String[] split) {
        _pos = new GeoLocation(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
	}
}
