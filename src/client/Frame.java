package client;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{

    private Arena _arena;
    // private double _xFactor = 0;
    // private double _yFactor = 0;
    private gameClient.util.Range2Range _w2f;


    public Frame(String title, Arena arena){
        super(title);
        _arena = arena;
        updateFrame();
        
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
		Range ry = new Range(this.getHeight()-50,150);
        Range2D frame = new Range2D(rx,ry);
        Range2D world = GraphRange();
		_w2f = new Range2Range(world, frame);
    }

    @Override
    public void paint(Graphics canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        updateFrame();
        Image buffer_image;
        Graphics buffer_graphics;
        // Create a new "canvas"
        buffer_image = createImage(w, h);
        buffer_graphics = buffer_image.getGraphics();

        // Draw on the new "canvas"
        paintComponents(buffer_graphics);

        // "Switch" the old "canvas" for the new one
        canvas.drawImage(buffer_image, 0, 0, this);
       
    }

    @Override
    public void paintComponents(Graphics canvas) {
        int w = this.getWidth();
		int h = this.getHeight();
        canvas.clearRect(0, 0, w, h);
		drawGraph(canvas);
        drawPokemons(canvas);
		drawAgants(canvas);
    }

    private void drawAgants(Graphics canvas) {
        canvas.setColor(Color.red);
        for (Agent agent : _arena.get_agents()) {
            geo_location agent_location = agent.get_pos();
            int r = 8;
            geo_location converted_pos = this._w2f.world2frame(agent_location);
            int x = (int)converted_pos.x()-r;
            int y = (int)converted_pos.y()-r;
            canvas.fillOval(x, y, 2*r, 2*r);
        }
    }
    
    private void drawPokemons(Graphics canvas) {
        //TODO use Myclient.g and _arena.get_pokemons() to draw the pokemons
        for (Pokemon p : _arena.get_pokemons()) {
            Point3D location = p.get_pos();
			int r=10;
			canvas.setColor(Color.green);
			if(p.get_type()<0) {canvas.setColor(Color.orange);}
            geo_location converted_pos = this._w2f.world2frame(location);
            int x = (int)converted_pos.x()-r;
            int y = (int)converted_pos.y()-r;
            canvas.fillOval(x, y, 2*r, 2*r);
        }
    }
    
    private void drawGraph(Graphics canvas) {
        for (node_data v : Myclient.g.getV()) {
            canvas.setColor(Color.BLACK);
            drawNode(v, 5, canvas);
            canvas.setColor(Color.DARK_GRAY);
            for (edge_data e : Myclient.g.getE(v.getKey())) {
                drawEdge(e, canvas);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics canvas) {
        geo_location pos = n.getLocation();
        geo_location converted_pos = this._w2f.world2frame(pos);
        int x = (int)converted_pos.x()-r;
        int y = (int)converted_pos.y()-r;
		canvas.fillOval(x, y, 2*r, 2*r);
		canvas.drawString(""+n.getKey(), (int)converted_pos.x(), (int)converted_pos.y()-4*r);
    }

    private void drawEdge(edge_data e, Graphics canvas) {
		geo_location s = Myclient.g.getNode(e.getSrc()).getLocation();
		geo_location d = Myclient.g.getNode(e.getDest()).getLocation();
		geo_location s0 = this._w2f.world2frame(s);
		geo_location d0 = this._w2f.world2frame(d);
        canvas.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        // Font font = canvas.getFont().deriveFont(40);
        // canvas.setFont(font);

		// canvas.drawString(""+e.getWeight(), (int)(d0.x()/2+s0.x())/2, (int)(d0.y()+s0.y())/2-10);
	}
    
    private static Range2D GraphRange() {
        double x0=0,x1=0,y0=0,y1=0;
        geo_location p = Myclient.g.getV().iterator().next().getLocation();
        x0=p.x(); x1=x0;
        y0=p.y(); y1=y0;
    
		for (node_data v : Myclient.g.getV()) {
			p = v.getLocation();
            if(p.x()<x0) {x0=p.x();}
            if(p.x()>x1) {x1=p.x();}
            if(p.y()<y0) {y0=p.y();}
            if(p.y()>y1) {y1=p.y();}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
    
}
