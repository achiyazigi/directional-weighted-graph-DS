package gameClient;

import java.util.Scanner;

public class Engine {

    public static void main(String[] args) throws InterruptedException {
    //    args = new String[]{"316071349","0"};
        Scanner sc= new Scanner(System.in);
        if(args.length == 0){
            System.out.println("enter id and level: ");
            args = new String[2];
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