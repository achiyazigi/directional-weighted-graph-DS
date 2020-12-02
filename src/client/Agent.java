package client;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.node_data;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private int _id;
    private Pokemon _target;
    private List<node_data> _path;
    private node_data _current_node;
    private dw_graph_algorithms ga;


    public Agent(int id, Pokemon pokemon) {
        ga = new DWGraph_Algo();
        ga.init(Myclient.g);
        _id = id;
        this.set_target(pokemon);
        if(_target != null) {
            _current_node = Myclient.g.getNode(_target.get_edge().getSrc());
        }
        _path = new LinkedList<>();
        _path.add(Myclient.g.getNode(_target.get_edge().getDest()));
//        _path = g1.shortestPath(_current_node.getKey(), Myclient.g.getNode(_target.get_edge().getDest()).getKey());

    }


    public int get_id() {
        return _id;
    }


    public Pokemon get_target() {
        return _target;
    }

    public void set_target(Pokemon _target) {
        this._target = _target;

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
        return _current_node;
    }

    public boolean isMoving() {
        if( this._current_node == null){
            _target = null;
            return false;
        }
        return true;
    }
}
