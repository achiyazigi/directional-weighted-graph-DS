package client;

public class Painter implements Runnable {
    @Override
    public void run() {
        try {
            synchronized(this){
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Frame win = new Frame("title", Myclient.arena);
        win.setSize(700, 500);
        win.setVisible(true);
        while (true) {
            win.repaint();
            try {
                Thread.sleep(1000/144); //refresh rate
                synchronized(this){
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
    }

}
