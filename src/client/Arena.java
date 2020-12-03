package client;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gameClient.util.Range2D;

import java.util.ArrayList;

public class Arena {
    private int _level;
    private ArrayList<Agent> _agents;
    private ArrayList<Pokemon> _pokemons;

//    {"Pokemons":[{"Pokemon":{"value":5.0,"type":-1,"pos":"35.197656770719604,32.10191878639921,0.0"}}]}

    public Arena(int level, String string_game, String string_pokemons) {
        _pokemons = new ArrayList<>();
        _agents = new ArrayList<>();
        _level = level;

        Gson gson = new Gson();
        JsonObject json_game = gson.fromJson(string_game,JsonObject.class);
        int num_of_agents = json_game.getAsJsonObject("GameServer").get("agents").getAsInt();
        JsonObject json_pokemons = gson.fromJson(string_pokemons,JsonObject.class);
        // JsonObject[] pokemon_array_jason = gson.fromJson(json_pokemons.getAsJsonArray("Pokemons"), JsonObject[].class);
        set_pokemons(json_pokemons);
        set_agents(num_of_agents);
    }

    public ArrayList<Agent> get_agents() {
        return _agents;
    }

    public ArrayList<Pokemon> get_pokemons() {
        return _pokemons;
    }

    public void set_pokemons(JsonObject json_pokemons) {
        ArrayList<Pokemon> new_pokemons = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject[] pokemon_array_jason = gson.fromJson(json_pokemons.getAsJsonArray("Pokemons"), JsonObject[].class);

        for (JsonObject p : pokemon_array_jason) {
            Pokemon pokemon = new Pokemon(p);
            new_pokemons.add(pokemon);
        }
        _pokemons = new_pokemons;
    }

    public void set_agents(int num_of_agents) {
        int i = 0;
        for (Pokemon p : _pokemons) {
            _agents.add(new Agent(i++, p));
        }
        for (int j = i; j < num_of_agents; j++) {
            _agents.add(new Agent(j, null));
        }
    }
}
