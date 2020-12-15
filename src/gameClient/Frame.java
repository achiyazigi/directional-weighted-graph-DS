package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private Arena _arena;
    private MenuBar _menuBar;
    private gameClient.util.Range2Range _w2f;
    private JTextField field = new JTextField(10);


    public Frame(String title, Arena arena) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JButton button = new JButton("Enter");
        button.addActionListener(this);
        panel.add(field, BorderLayout.SOUTH);
        panel.add(button, BorderLayout.EAST);
        this.add(panel);

        _arena = arena;
        updateFrame();
        Menu game_menu = new Menu("Game");
        MenuItem stop = new MenuItem("Stop Game");
        stop.addActionListener(this);
        game_menu.add(stop);
        _menuBar = new MenuBar();
        _menuBar.add(game_menu);
        this.setMenuBar(_menuBar);
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 50, 150);
        Range2D frame = new Range2D(rx, ry);
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

    
    /** 
     * painting the graph, pokemons, agent and stats.
     * @param canvas
     */
    @Override
    public void paintComponents(Graphics canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.setColor(new Color(255, 242, 249));
        canvas.fillRect(0, 0, w, h);
        Font font = new Font("Monospaced", Font.BOLD | Font.ITALIC, 17);
        canvas.setFont(font);
        canvas.setColor(Color.BLUE);
        geo_location converted_pos = _w2f.getFrame().fromPortion(new GeoLocation(0, 0, 0));
        int x = (int) converted_pos.x();
        int y = (int) converted_pos.y();
        //  ---game stats---
        canvas.drawString("level: " + _arena.get_level() +
                     "     Score: " + _arena.getScore() +
                     "     moves: " + _arena.getMove() + 
                   "   time left: " + Myclient.game.timeToEnd()/1000.0, x, y);
        //  ---end of stats---
        canvas.setFont(canvas.getFont().deriveFont(Font.PLAIN, 14));
        drawGraph(canvas);
        drawPokemons(canvas);
        drawAgants(canvas);

    }

    
    /** 
     * draw all agents.
     * @param canvas
     */
    private void drawAgants(Graphics canvas) {
        for (Agent agent : _arena.get_agents()) {
            canvas.setColor(Color.red);
            geo_location agent_location = agent.get_pos();
            int r = 8;
            geo_location converted_pos = _w2f.world2frame(agent_location);
            int x = (int) converted_pos.x() - r;
            int y = (int) converted_pos.y() - r;
            canvas.fillOval(x, y, 2 * r, 2 * r);
            canvas.setColor(Color.black);
            canvas.drawString("" + agent.get_id(), x + 5, y + 13);

            canvas.setColor(Color.BLUE);
            converted_pos = _w2f.getFrame().fromPortion(new GeoLocation(0, 1.1 - agent.get_id()/10.0 , 0));
            x = (int) converted_pos.x();
            y = (int) converted_pos.y() ;
            canvas.drawString("Agent "+ agent.get_id() + ":"+ agent.get_value(), x, y);

        }
    }

    
    /** 
     * draw all pokemons.
     * @param canvas
     */
    private void drawPokemons(Graphics canvas) {
        for (Pokemon p : _arena.get_pokemons()) {
            Point3D location = p.get_pos();
            int r = 10;
            canvas.setColor(Color.green);
            if (p.get_type() < 0) {
                canvas.setColor(Color.orange);
            }
            geo_location converted_pos = _w2f.world2frame(location);
            int x = (int) converted_pos.x() - r;
            int y = (int) converted_pos.y() - r;
            canvas.fillOval(x, y, 2 * r, 2 * r);
            canvas.setColor(Color.blue);
            canvas.drawString("" + (int)p.get_value(), x+4, y+13);
        }
    }

    
    /** 
     * draw the graph
     * @param canvas
     */
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

    
    /** 
     * draw a single node
     * @param n
     * @param r
     * @param canvas
     */
    private void drawNode(node_data n, int r, Graphics canvas) {
        geo_location pos = n.getLocation();
        geo_location converted_pos = this._w2f.world2frame(pos);
        int x = (int) converted_pos.x() - r;
        int y = (int) converted_pos.y() - r;
        canvas.fillOval(x, y, 2 * r, 2 * r);
        canvas.drawString("" + n.getKey(), (int) converted_pos.x(), (int) converted_pos.y() - 4 * r);

    }

    
    /** 
     * draw a single edge
     * @param e
     * @param canvas
     */
    private void drawEdge(edge_data e, Graphics canvas) {
        geo_location s = Myclient.g.getNode(e.getSrc()).getLocation();
        geo_location d = Myclient.g.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        canvas.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

        /*
        ---print the edge's weight - for future use---
            Font font = canvas.getFont().deriveFont(40);
            canvas.setFont(font);
            canvas.drawString(""+e.getWeight(), (int)(d0.x()/2+s0.x())/2,
            (int)(d0.y()+s0.y())/2-10);
        */
    }

    
    /** 
     * computing the farest pair of points and creating a range2D based on them.
     * @return Range2D
     */
    private static Range2D GraphRange() {
        double x0, x1, y0, y1;
        geo_location p = Myclient.g.getV().iterator().next().getLocation();
        x1 = x0 = p.x();
        y1 = y0 = p.y();

        for (node_data v : Myclient.g.getV()) {
            p = v.getLocation();
            if (p.x() < x0) {
                x0 = p.x();
            }
            if (p.x() > x1) {
                x1 = p.x();
            }
            if (p.y() < y0) {
                y0 = p.y();
            }
            if (p.y() > y1) {
                y1 = p.y();
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    /**
     * this should be in a new class called Controller.java... but since there's
     * only one menuItem, it's temporary here...
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Stop Game")) {
            Myclient.game.stopGame();
        }
    }

}
