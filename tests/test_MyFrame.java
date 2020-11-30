import api.*;
import gameClient.*;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;


public class test_MyFrame {
    
    MyFrame win = new MyFrame("check");

    @Test
    public void new_window() {
        win.setTitle("achiya & ravid");
        win.setSize(1000,400);
        win.setVisible(true);
        long x = System.currentTimeMillis();
        while(System.currentTimeMillis()-x < 10000){;}
    }
}
