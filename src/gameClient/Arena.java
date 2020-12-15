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

    
    /** 
     * returns a list of the agents in the game.
     * @return ArrayList<Agent>
     */
    public ArrayList<Agent> get_agents() {
        return _agents;
    }

    
    /** 
     * returns a list of the Pokemons in the game.
     * @return ArrayList<Pokemon>
     */
    public ArrayList<Pokemon> get_pokemons() {
        return _pokemons;
    }

    
    /** 
     * updating the pokemons list by replacing it with a new updated list so the list will never be empty.
     * @param json_pokemons
     */
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

    
    /** 
     * initializing the first position and targets for the agents.
     * if there are more agents then pokemons, some of the agents tagets will be set to null.
     * those who didnt get a target will wait for an update and won't move until then. 
     * @param num_of_agents
     */
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

   
    
    /** 
     * updating the agent's data from the server.
     * also, each agent mark its target by changing open[target id] to false so others wont declare it.
     * additionaly, iterating over each edge in each agent path,
     * pokemons who poped after init of the agents target will be declared too by the agent since he's going to collect it on its way.
     * @param open
     * @param json_agents
     */
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
            agent.set_src(agent_object.get("src").getAsInt());
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

    
    /** 
     * get the total game score.
     * @return int
     */
    public int getScore() {
        return score;
    }

    
    /** set the total game score.
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    
    /** 
     * get the game's level.
     * @return int
     */
    public int get_level() {
        return _level;
    }

    
    /** 
     * return the moves count. being used by Frame.
     * @return int
     */
    public int getMove() {
        return move;
    }

    /**
     * increases move count by 1.
     */
    public void move_plus1() {
        this.move = move + 1;
    }

}
