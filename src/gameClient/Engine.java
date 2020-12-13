package gameClient;

import Server.Game_Server_Ex2;

import java.util.Scanner;

public class Engine {

    public static void main(String[] args) throws InterruptedException {
//        args = new String[]{"3","3"};
        Painter painter = new Painter();
        Thread graphics = new Thread(painter);
        graphics.start();
        Myclient mc = new Myclient(Long.parseLong(args[0]),Integer.parseInt(args[1]), painter);
        Thread code = new Thread(mc);
        code.start();
        code.join();
        graphics.join();

    }
}