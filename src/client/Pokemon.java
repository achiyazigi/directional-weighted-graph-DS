package client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import api.*;
import gameClient.util.Point3D;

public class Pokemon {

    private edge_data _edge;
    private double _value;
    private Point3D _pos;
    private int _type;

    public Pokemon(JsonObject json_pokemon) {
        JsonObject pokemon = json_pokemon.getAsJsonObject("Pokemon");
        _value = pokemon.get("value").getAsInt();
        _type = pokemon.get("type").getAsInt();
        String[] raw_pos = pokemon.get("pos").getAsString().split(",");
        _pos = new Point3D(Double.parseDouble(raw_pos[0]), Double.parseDouble(raw_pos[1]), Double.parseDouble(raw_pos[2]));
        
        geo_location pos = new GeoLocation(_pos.x(), _pos.y(), _pos.z());

        _edge = this.findEdge(pos);
       
    }

    private edge_data findEdge(geo_location pos) {
        for (node_data v : Myclient.g.getV()) {
            for (edge_data e : Myclient.g.getE(v.getKey())) {
                int src = v.getKey();
                int dest = Myclient.g.getNode(e.getDest()).getKey();
                if (_type > 0 && src < dest || _type < 0 && src > dest) {
                    double line = v.getLocation().distance(Myclient.g.getNode(e.getDest()).getLocation());
                    double line_through_pos = v.getLocation().distance(pos) + pos.distance(Myclient.g.getNode(e.getDest()).getLocation());
                    if (line > line_through_pos - 0.00001) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    public edge_data get_edge() {
        return _edge;
    }

    public void set_edge(edge_data _edge) {
        this._edge = _edge;
    }

    public double get_value() {
        return _value;
    }

    public void set_value(double _value) {
        this._value = _value;
    }

    public Point3D get_pos() {
        return _pos;
    }
    
    public int get_type(){
        return _type;
    }
}
