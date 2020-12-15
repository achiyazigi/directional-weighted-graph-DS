package gameClient;

public class Engine {

    public static void main(String[] args) throws InterruptedException {
    //    args = new String[]{"316071349","0"};
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