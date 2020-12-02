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

public class Myclient implements Runnable{

    public static  directed_weighted_graph g;

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
        System.out.println(g.getV());

        game.addAgent(0);

        Arena arena = new Arena(0, game.toString(),game.getPokemons());

        game.login(316071349);
        game.startGame();
        System.out.println(game.getAgents());
        Gson gson = new Gson();
        while(game.isRunning()){
            JsonObject json_agents = gson.fromJson(game.getAgents(),JsonObject.class);
            JsonObject[] agents_state = gson.fromJson(json_agents.getAsJsonArray("Agents"), JsonObject[].class);
            for (Pokemon p : arena.get_pokemons()) {
                for (Agent agent : arena.get_agents()) {
                    if(!agent.isMoving()){
                        agent.set_target(p);
                    }
                    //get care of moving agents!
//                    else if(agents_state[agent.get_id()].){
//
//                    }
                }

            }
            game.chooseNextEdge(0,g.getE(0).iterator().next().getDest());

            game.move();

        }
    }
}
