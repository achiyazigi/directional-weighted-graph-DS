package client;

import api.*;
import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class Myclient implements Runnable {

    public static directed_weighted_graph g;
    public static dw_graph_algorithms ga;
    public static game_service game;
    public static Arena arena;
    private int _level;
    private boolean[] open;
    private Painter _painter;

    public Myclient(int level_number, Painter painter) {

        _level = level_number;
        _painter = painter;
    }

    @Override
    public void run() {
        game = Game_Server_Ex2.getServer(_level);

        init_g(game.getGraph());

        arena = new Arena(game.toString(), game.getPokemons());

        for (Agent agent : arena.get_agents()) {
            game.addAgent(agent.get_current_node().getKey());
        }
        synchronized (_painter) {
            _painter.notifyAll();
        }

        game.login(316071349);
        game.startGame();

        System.out.println("game started = " + game.isRunning() + ", ends in: " + (game.timeToEnd() / 1000) + "\'s");


        Thread move = new Thread(new Move());
        move.start();

        
        Gson gson = new Gson();
        JsonObject  json_agents = gson.fromJson(game.getAgents(), JsonObject.class);
        JsonObject json_pokemons = gson.fromJson(game.getPokemons(), JsonObject.class);
        int ntmFilter = 0; // unsolved feature, needs to be replaced with sleep
        while (game.isRunning()) {

            try{
                json_agents = gson.fromJson(game.getAgents(), JsonObject.class);
                json_pokemons = gson.fromJson(game.getPokemons(), JsonObject.class);

            }
            catch(IndexOutOfBoundsException e){}
            open = new boolean[arena.get_pokemons().size()];
            Arrays.fill(open, true);
            arena.set_pokemons(json_pokemons);
            arena.update_agents(open, json_agents);

            for (Agent agent : arena.get_agents()) {

                if (!agent.isOnEdge() && !agent.isAvailable()) {
                    game.chooseNextEdge(agent.get_id(), agent.nextNode().getKey());
                } else if (agent.isAvailable()) {
                    double min = Double.MAX_VALUE;
                    Pokemon candi = null;
                    for (int i = 0; i < open.length; i++) {

                        if (open[i]) {
                            Pokemon p = arena.get_pokemons().get(i);
                            double distance = ga.shortestPathDist(agent.get_current_node().getKey(),
                                    p.get_edge().getSrc()) + p.get_edge().getWeight();
                            if (distance < min) {
                                candi = p;
                                min = distance;
                            }
                        }
                    }
                    agent.set_target(candi);
                    int idx = arena.get_pokemons().indexOf(candi);
                    if (idx != -1) {
                        open[idx] = false;
                    }
                }

            }

            arena.setScore(gson.fromJson(game.toString(), JsonObject.class).getAsJsonObject("GameServer")
                    .getAsJsonObject().get("grade").getAsInt());

            synchronized (_painter) {
                _painter.notifyAll();// refreshing the window
            }
        }
        System.out.println(game.toString());
        System.exit(0);
    }

    private void init_g(String str_graph) {
        ga = new DWGraph_Algo();

        try (Writer writer = new FileWriter("out\\g5.json")) {
            writer.write(str_graph);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ga.load("out\\g5.json");
        g = ga.getGraph();
    }

    private class Move implements Runnable {

        public void run() {
            while (Myclient.game.isRunning()) {
                Myclient.game.move();
                Myclient.arena.move_plus1();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}