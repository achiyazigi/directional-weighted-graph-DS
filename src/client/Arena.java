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
    private int score = 0;
    private int move = 0;

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
            if(i<num_of_agents){
                _agents.add(new Agent(i++, p));
        }
        }
        for (int j = i; j < num_of_agents; j++) {
            _agents.add(new Agent(j, null));
        }
    }
    
    public void update_agents(boolean[] open, JsonObject json_agents) {
        Gson gson = new Gson();
        JsonObject[] agents_state = gson.fromJson(json_agents.getAsJsonArray("Agents"), JsonObject[].class);
        for (int i = 0; i < agents_state.length; i++) {
            _agents.get(i).set_pos(agents_state[i].get("Agent").getAsJsonObject().get("pos").getAsString().split(","));
            int idx = _pokemons.indexOf(_agents.get(i).get_target());
            if(idx > -1)
                open[idx] = false;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int get_level() {
        return _level;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }
}
