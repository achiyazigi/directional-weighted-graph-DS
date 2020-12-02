package client;
import api.*;
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{

    private Arena _arena;

    public Frame(String title, Arena arena){
        super(title);
        _arena = arena;
    }

    @Override
    public void paint(Graphics canvas) {
        int w = this.getWidth();
		int h = this.getHeight();
        canvas.clearRect(0, 0, w, h);
		drawGraph(canvas);
        drawPokemons(canvas);
		drawAgants(canvas);
    }

    private void drawAgants(Graphics canvas) {
        //TODO use Myclient.g and _arena.get_agents() to draw the agents
    }
    
    private void drawPokemons(Graphics canvas) {
        //TODO use Myclient.g and _arena.get_pokemons() to draw the pokemons
    }
    
    private void drawGraph(Graphics canvas) {
        //TODO use Myclient.g to draw the graph
        for (node_data v : Myclient.g.getV()) {
            drawNode(v, 10, canvas);
        }
    }
    private void drawNode(node_data n, int r, Graphics canvas) {


//         geo_location pos = n.getLocation();
//		 geo_location fp = this._w2f.world2frame(pos);
//		 g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
//		 g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
	}
    
}
