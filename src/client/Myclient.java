package client;

import api.*;
import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLOutput;

import javax.imageio.ImageIO;

public class Myclient implements Runnable{

    public static  directed_weighted_graph g;
    public static Arena arena;

    @Override
    public void run() {

        int level_number = 0;
        game_service game = Game_Server_Ex2.getServer(level_number);
        
        dw_graph_algorithms ga = new DWGraph_Algo();
        
        try (Writer writer = new FileWriter("out\\g5.json")) {
            writer.write(game.getGraph());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ga.load("out\\g5.json");
        g = ga.getGraph();
        
        arena = new Arena(0, game.toString(),game.getPokemons());

        for (Agent agent : arena.get_agents()) {
            game.addAgent(agent.get_current_node().getKey());
        }
        Frame win = new Frame("test", arena);
        win.setSize(700,500);
        win.setVisible(true);
        win.repaint();
        

        game.login(316071349);
        game.startGame();
        Gson gson = new Gson();
        while(game.isRunning()){
            JsonObject json_agents = gson.fromJson(game.getAgents(),JsonObject.class);
            JsonObject[] agents_state = gson.fromJson(json_agents.getAsJsonArray("Agents"), JsonObject[].class);
            JsonObject json_pokemons = gson.fromJson(game.getPokemons(),JsonObject.class);
            for (int i = 0; i < agents_state.length; i++) {
                arena.get_agents().get(i).set_pos(agents_state[i].get("Agent").getAsJsonObject().get("pos").getAsString().split(","));
            }
            arena.set_pokemons(json_pokemons);//lets rethink!!!
            for (Pokemon p : arena.get_pokemons()) {
                for (Agent agent : arena.get_agents()) {
                    if(!agent.isOnEdge()){//this is working great! dont touch.
                        game.chooseNextEdge(agent.get_id(),agent.nextNode().getKey());
                        System.out.println("agent "+agent.get_id()+" turned to node "+agent.get_current_node().getKey());
                    }
                    else if(!agent.isMoving()){
                        agent.set_target(p);//here also. we need to come up with a better structure
                    }
                    else agent.set_target(p);
                    win.repaint();
                   
                }

            }

            game.move();

        }
    }
}
