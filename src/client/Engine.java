package client;

public class Engine {

    public static void main(String[] args) throws InterruptedException {
        Painter painter = new Painter();
        Thread code = new Thread(new Myclient(23, painter));
        code.start();
        Thread graphics = new Thread(painter);
        graphics.start();
        code.join();
        graphics.join();
    }
}