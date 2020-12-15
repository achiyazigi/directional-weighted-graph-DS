package gameClient;

import com.google.gson.*;
import api.node_data;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private int _level;
    private ArrayList<Agent> _agents;
    private ArrayList<Pokemon> _pokemons;
    private int score = 0;
    private int move = 0;

    public Arena(String string_game, String string_pokemons) {
        _pokemons = new ArrayList<>();
        _agents = new ArrayList<>();
        
        Gson gson = new Gson();
        JsonObject json_game = gson.fromJson(string_game, JsonObject.class);

        JsonObject game_object = json_game.getAsJsonObject("GameServer");
        _level = game_object.get("game_level").getAsInt();
        int num_of_agents = game_object.get("agents").getAsInt();

        JsonObject json_pokemons = gson.fromJson(string_pokemons, JsonObject.class);
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
        double[] max_val = new double[num_of_agents];
        Pokemon[] max_p = new Pokemon[num_of_agents];
        for (Pokemon p : _pokemons) {
            double val = p.get_value();
            for (int j = 0; j < max_p.length; j++) {
                if(val > max_val[j]){
                    max_val[j] = val;
                    max_p[j] = p;
                    break;
                }
            }
        }
        int i;
        for (i=0 ; i < num_of_agents; i++) {
            _agents.add(new Agent(i, max_p[i]));
        }

    }

   
    public void update_agents(boolean[] open, JsonObject json_agents) {
        Gson gson = new Gson();
        JsonObject[] agents_state = gson.fromJson(json_agents.getAsJsonArray("Agents"), JsonObject[].class);
        for (int i = 0; i < agents_state.length; i++) {
            JsonObject agent_object = agents_state[i].get("Agent").getAsJsonObject();
            int id = agent_object.get("id").getAsInt();
            Agent agent = _agents.get(id);
            agent.set_pos(agent_object.get("pos").getAsString().split(","));
            int idx = _pokemons.indexOf(agent.get_target());
            agent.set_speed(agent_object.get("speed").getAsDouble());
            agent.set_value(agent_object.get("value").getAsDouble());
            if (idx > -1) {
                open[idx] = false;
            }
            List<node_data> agent_path = agent.get_path();
            for (int j = 1; j < agent_path.size(); j++) {
                for (Pokemon p : _pokemons) {
                    int src = agent_path.get(j - 1).getKey();
                    int dest = agent_path.get(j).getKey();
                    if (p.get_edge().getSrc() == src && p.get_edge().getDest() == dest) {
                        idx = _pokemons.indexOf(p);
                        if(idx > -1 && idx < open.length){
                            open[idx] = false;
                        }
                    }
                }
            }
        }
    }

    public void set_level(int level){ _level = level; }

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

    public void move_plus1() {
        this.move = move + 1;
    }

}
