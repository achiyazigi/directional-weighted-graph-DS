import api.*;
import static org.junit.jupiter.api.Assertions.*;
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
    
    
}
