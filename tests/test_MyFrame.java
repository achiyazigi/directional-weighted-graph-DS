import api.*;
import gameClient.*;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.JPanel;


public class test_MyFrame {
    
    MyFrame win = new MyFrame("check");

    @Test
    public void new_window() {
        win.setTitle("achiya & ravid");
        win.setSize(1000,400);
        win.show();
        long x = System.currentTimeMillis();
        while(System.currentTimeMillis()-x < 10000){;}
    }
    
    @Test
    public void draw() {
        JPanel panel = new JPanel();
        win.setSize(1000,400);
        panel.repaint();
        win.show();
        long x = System.currentTimeMillis();
        while(System.currentTimeMillis()-x < 10000){;}
    }
}
