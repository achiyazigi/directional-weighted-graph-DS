package gameClient;

public class Engine {
    public static void main(String[] args) {

        MyFrame win = new MyFrame("test");
        win.setSize(1000,1000);
        win.setVisible(true);
        Thread code = new Thread(new Myclient());
        code.start();
        try {
            code.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}