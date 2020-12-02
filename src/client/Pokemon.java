package client;

import org.json.JSONException;
import org.json.JSONObject;

import api.*;

public class Pokemon {

    private edge_data _edge;
    private double _value;

    public Pokemon(JSONObject json_pokemon) {
        try {
            _value = json_pokemon.getInt("value");
            int type = json_pokemon.getInt("type");
            String[] raw_pos = json_pokemon.getString("pos").split(",");
            geo_location pos = new GeoLocation(Double.parseDouble(raw_pos[0]), Double.parseDouble(raw_pos[1]), Double.parseDouble(raw_pos[2]));

            _edge = this.findEdge(pos, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private edge_data findEdge(geo_location pos, int type) {

        for (node_data v : Myclient.g.getV()) {
            for (edge_data e : Myclient.g.getE(v.getKey())) {
                int src = v.getKey();
                int dest = Myclient.g.getNode(e.getDest()).getKey();
                if (type > 0 && src < dest || type < 0 && src > dest) {
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
}
