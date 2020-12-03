package client;

public class Engine {
    public static void main(String[] args) {


            Thread code = new Thread(new Myclient(5));
            code.start();
            try {
                code.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}