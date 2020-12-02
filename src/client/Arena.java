package client;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Arena {
    private int _level;
    private ArrayList<Agent> _agents;
    private ArrayList<Pokemon> _pokemons;

//    {"Pokemons":[{"Pokemon":{"value":5.0,"type":-1,"pos":"35.197656770719604,32.10191878639921,0.0"}}]}

    public Arena(int level, String string_game, String string_pokemons) {
        _level = level;
        try {
            Gson gson = new Gson();
            JSONObject json_game = gson.fromJson(string_game,JSONObject.class);
            int num_of_agents = json_game.getJSONObject("GameServer").getInt("agents");
            JsonObject json_pokemons = gson.fromJson(string_game,JsonObject.class);
            JSONObject[] pokemon_array_jason = gson.fromJson(json_pokemons.getAsJsonArray("Pokemons"), JSONObject[].class);
            for (JSONObject p : pokemon_array_jason) {
                Pokemon pokemon = new Pokemon(p);
                _pokemons.add(pokemon);
                if (num_of_agents > 0) {
                    _agents.add(new Agent(num_of_agents, pokemon));
                    num_of_agents--;
                }
            }
            while (num_of_agents > 0) {
                _agents.add(new Agent(num_of_agents, null));
                num_of_agents--;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Agent> get_agents() {
        return _agents;
    }

    public ArrayList<Pokemon> get_pokemons() {
        return _pokemons;
    }

    public void set_pokemons(JsonObject json_pokemons) {
        _pokemons.clear();
        Gson gson = new Gson();
        JSONObject[] pokemon_array_jason = gson.fromJson(json_pokemons.getAsJsonArray("Pokemons"), JSONObject[].class);
        for (JSONObject p : pokemon_array_jason) {
            Pokemon pokemon = new Pokemon(p);
            _pokemons.add(pokemon);
            this._pokemons = _pokemons;
        }
    }
}
