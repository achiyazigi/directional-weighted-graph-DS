import api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

public class test_DWGraph_DS {

    @Test
    public void addNode_basic() {
        directed_weighted_graph g = new DWGraph_DS();
        node_data n = new NodeData(0);
        g.addNode(n);
        assertEquals(0, g.getNode(0).getKey());
    }

    @Test
    public void addNode_same() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(0));
        assertEquals(0, g.getNode(0).getKey());
        assertEquals(1, g.nodeSize());
    }

    @Test
    public void get_node_not_ex(){
        directed_weighted_graph g = new DWGraph_DS();
        assertEquals(null, g.getNode(0));
    }

    @Test
    public void connect_basic() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0, 1, 50);
        assertEquals(50, g.getEdge(0, 1).getWeight());
    }

    @Test
    public void connect_same() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0, 1, 50);
        g.connect(0, 1, 50);
        System.out.println(g.getE(0));
        assertEquals(1, g.edgeSize());
    }

    @Test
    public void self_connect() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.connect(0, 0, 50);
        assertEquals(null, g.getEdge(0, 0));
    }
    
    @Test
    public void remove_edge_basic() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0, 1, 50);
        assertEquals(50, g.removeEdge(0, 1).getWeight());
    }
    
    @Test
    public void remove_edge_get_reversed_edge() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.connect(0, 1, 50);
        g.connect(1, 0, 50);
        assertEquals(2, g.edgeSize());
        g.removeEdge(0, 1);
        assertNotEquals(null, g.getEdge(1, 0));
        assertEquals(null, g.getEdge(0, 1));
        
    }
    
    @Test
    public void get_e() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        for (int i = 1; i < 1000000; i++) {
            g.addNode(new NodeData(i));
            g.connect(0, i, i);
        }
        boolean [] b = new boolean[1000000];
        Collection<edge_data> col = g.getE(0);
        int dest = 0;
        for (edge_data d : col) {
            dest = d.getDest();
            b[dest] = !b[dest];
        }
        for (int i = 1; i < b.length; i++) {
            if(!b[i]) {
                System.out.println(i);
                fail();
            } 
        }
        assertEquals(1000000,col.size()+1);
    }

    @Test
    public void remove_node_basic() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        for (int i = 1; i < 1000000; i++) {
            g.addNode(new NodeData(i));
            g.connect(0, i, i);
        }
        g.removeNode(0);
        for (int i = 1; i < 1000000; i++) {
            if(g.getEdge(0, i) != null) {
                System.out.println(i);
                fail();
            } 
        }
        assertEquals(null, g.getNode(0));
    }

    @Test
    public void remove_node_2_ways() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new NodeData(0));
        for (int i = 1; i < 1000000; i++) {
            g.addNode(new NodeData(i));
            g.connect(0, i, i);
            g.connect(i, 0, i);
        }
        g.removeNode(0);
        for (int i = 1; i < 1000000; i++) {
            if(g.getEdge(0, i) != null || g.getEdge(i, 0) != null) {
                System.out.println(i);
                fail();
            } 
        }
        assertEquals(null, g.getNode(0));
    }
}
