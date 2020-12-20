package gameClient;

import java.util.Scanner;

public class Engine {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc= new Scanner(System.in);
        while(args.length == 0){
            System.out.println("enter id and level: ");
            args = sc.nextLine().split(" ");
        }
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