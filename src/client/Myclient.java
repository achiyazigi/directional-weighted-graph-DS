package client;

import api.*;
import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

public class Myclient implements Runnable {

    public static directed_weighted_graph g;
    public static dw_graph_algorithms ga;
    public static Arena arena;
    private int _level;

    public Myclient(int level_number) {
        _level = level_number;
    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(_level);

        init_g(game.getGraph());

        arena = new Arena(0, game.toString(), game.getPokemons());

        for (Agent agent : arena.get_agents()) {
            game.addAgent(agent.get_current_node().getKey());
        }

        Frame win = new Frame("test", arena);
        win.setSize(700, 500);
        win.setVisible(true);
        win.repaint();

        game.login(205544851);
        game.startGame();
        System.out.println("game started = " + game.isRunning() + ", ends in: " + (game.timeToEnd() / 1000) + "\'s");

        Gson gson = new Gson();
        while (game.isRunning()) {
            JsonObject json_agents = gson.fromJson(game.getAgents(), JsonObject.class);
            JsonObject[] agents_state = gson.fromJson(json_agents.getAsJsonArray("Agents"), JsonObject[].class);
            JsonObject json_pokemons = gson.fromJson(game.getPokemons(), JsonObject.class);
//            ArrayList<Boolean> open = new ArrayList<>();
            for (int i = 0; i < agents_state.length; i++) {
                arena.get_agents().get(i).set_pos(agents_state[i].get("Agent").getAsJsonObject().get("pos").getAsString().split(","));
            }
            arena.set_pokemons(json_pokemons);
            boolean need_to_move = false;
            for (Agent agent : arena.get_agents()) {
                if (!agent.isOnEdge() && !agent.isAvailable()) {
                    game.chooseNextEdge(agent.get_id(), agent.nextNode().getKey());

                    // System.out.println("agent "+agent.get_id()+" turned to node "+agent.get_current_node().getKey());
                }
                else if (agent.isAvailable()) {
                    double min = Double.MAX_VALUE;
                    Pokemon candi = null;
                    for (Pokemon p : arena.get_pokemons()) {
                        boolean taken = false;
                        for (Agent agent2 : arena.get_agents()) {
                            if (!agent2.isAvailable() && agent2.get_target().equals(p)) {
                                taken = true;
                                break;
                            }
                        }
                        if (!taken) {
                            double distance = ga.shortestPathDist(agent.get_current_node().getKey(),p.get_edge().getSrc()) + p.get_edge().getWeight();
                            if( distance < min){
                                candi = p;
                                min = distance;
                            }
                        }
                    }
                    agent.set_target(candi);
                }
                else
                    need_to_move = true;
            }
            if (need_to_move) {
                game.move();
            }
            win.repaint();
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
}
