package gameClient;

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
    @Override
    public void run() {
        int level_number = 0;
        game_service game = Game_Server_Ex2.getServer(level_number);

        dw_graph_algorithms g = new DWGraph_Algo();

        try (Writer writer = new FileWriter("out\\g5.json")) {
            writer.write(game.getGraph());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.load("out\\g5.json");
        directed_weighted_graph g0 = g.getGraph();
        System.out.println(g0.getV());

        game.addAgent(0);
        String pok = game.getPokemons();
        JSONObject j = null;
        try {
            j = new JSONObject(pok);
            JSONArray Pokemons = j.getJSONArray("Pokemons");
            JSONObject p = Pokemons.getJSONObject(0);
            String []xyz = p.getString("pos").split(",");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(game.getAgents());
        game.login(316071349);
        game.startGame();
        while(game.isRunning()){
            game.chooseNextEdge(0,g0.getE(0).iterator().next().getDest());
            System.out.println(game.getPokemons());
            game.move();
            System.out.println(game.toString());
        }
    }
}
