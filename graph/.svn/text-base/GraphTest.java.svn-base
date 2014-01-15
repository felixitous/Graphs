package graph;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import graph.Graph.Vertex;
import graph.Graph.Edge;

/** Junit Tests for Graph.java
 *  @author Felix Liu. */

public class GraphTest {

    /** private field that holds the test case. */
    private Object value;


    /** builds the fields. Takes in the string INPUT */
    void fieldSetup(String input, Object input1) {
        try {
            Field field = Graph.class.getDeclaredField(input);
            field.setAccessible(true);
            value = field.get(input1);
        } catch (IllegalAccessException|NoSuchFieldException e) {
            System.err.println("error");
            System.exit(1);
        }
    }

    /** Weighting that weighs the edges for
     *  Integers. */
    public static final Weighting<Integer> EDGE =
        new Weighting<Integer>() {
            @Override
            public double weight(Integer x) {
                double tmp = (double) x;
                return tmp;
            }
        };

    /** Weighter that weights the vertices for Integers. */
    public static final Weighter<Integer> VERTEX =
        new Weighter<Integer>() {

            @Override
            public double weight(Integer x) {
                double tmp = (double) x;
                return tmp;
            }

            @Override
            public void setWeight(Integer x, double v) {
                double tmp = (double) x + v;
            }
        };

    /** the most basic testing for graphs, seeing
     *  how many vertices and edges a base graph should
     *  have. */
    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    /** Test to see if things are added correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void addtograph() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        Vertex testingV = g.add(tmp);
        fieldSetup("masterVert", g);
        ArrayList<Vertex> test = (ArrayList<Vertex>) value;
        assertEquals(true, test.contains(testingV));
    }

    /** Test to see if edges are being correctly designated. */
    @Test
    @SuppressWarnings("unchecked")
    public void edgeingraph() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmpt2 = "testing2";
        String tmp2 = "edgetesting";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Edge testingE = g.add(v0, v1, tmp2);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> test = (ArrayList<Edge>) value;
        assertEquals(true, test.contains(testingE));
    }

    /** EdgeTesting for null labels and seeing if partners
     *  are actually taken in correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void edgenullandpartner() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Edge testingE = g.add(v0, v1);
        assertEquals(null, testingE.getLabel());
        ArrayList<Vertex> hold = v0.getList("com");
        assertEquals(true, hold.contains(v1));
    }

    /** Checks if the sizes are displaying correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void sizetesting() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Edge testingE = g.add(v0, v1);
        assertEquals(2, g.vertexSize());
        assertEquals(1, g.edgeSize());
    }

    /** Checks to see if degree methods are working properly. */
    @Test
    @SuppressWarnings("unchecked")
    public void degreetesting() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        String e3label = "e3labeltesting";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v0, v2);
        Edge e2 = g.add(v0, v3);
        Edge e3 = g.add(v1, v2, e3label);
        assertEquals(3, g.outDegree(v0));
        assertEquals(0, g.inDegree(v0));
        assertEquals(true, g.contains(v0, v1));
        assertEquals(true, g.contains(v0, v2));
        assertEquals(true, g.contains(v1, v2, e3label));
    }

    /** Checks to see if things are removed correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void removetest() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        String e3label = "e3labeltesting";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v0, v2);
        Edge e2 = g.add(v0, v3);
        Edge e3 = g.add(v1, v0);
        Edge e4 = g.add(v1, v2);
        Edge e5 = g.add(v1, v0);
        ArrayList<Vertex> v1communion = v1.getList("com");
        g.remove(v0);
        fieldSetup("masterVert", g);
        ArrayList<Vertex> masterVert = (ArrayList<Vertex>) value;
        assertEquals(false, v1communion.contains(v0));
        assertEquals(false, masterVert.contains(v0));
        fieldSetup("masterEdge", g);
        ArrayList<Edge> masterEdge = (ArrayList<Edge>) value;
        for (Edge items : masterEdge) {
            Vertex erasev0 = items.getV0();
            Vertex erasev1 = items.getV1();
            assertEquals(false, v0.equals(erasev0));
            assertEquals(false, v0.equals(erasev1));
        }
    }

    /** Checks to see if a particular edge is removed correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void removeedgetest() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v0, v2);
        Edge e2 = g.add(v0, v3);
        Edge e3 = g.add(v1, v0);
        Edge e4 = g.add(v1, v2);
        Edge e5 = g.add(v1, v0);
        g.remove(e0);
        ArrayList<Vertex> hold = v0.getList("com");
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold1 = (ArrayList<Edge>) value;
        ArrayList<Vertex> hold2 = v1.getList("com");
        assertEquals(false, hold.contains(v1));
        assertEquals(false, hold1.contains(e0));
        assertEquals(true, hold2.contains(v0));
        g.remove(v1, v0);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold3 = (ArrayList<Edge>) value;
        ArrayList<Vertex> hold4 = v1.getList("com");
        assertEquals(false, hold4.contains(v0));
    }

    /** Another test for removal to get the right edges
     *  removed. */
    @Test
    @SuppressWarnings("unchecked")
    public void removeundirect() {
        UndirectedGraph<String, String> g
            = new UndirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        String e3label = "e3labeltesting";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v1, v2);
        Edge e2 = g.add(v2, v3);
        Edge e3 = g.add(v3, v0);
        g.remove(v0);
        Iteration<Graph<String, String>.Edge> test = g.edges();
        int count = 0;
        while (test.hasNext()) {
            test.next();
            count++;
        }
        assertEquals(2, count);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold1 = (ArrayList<Edge>) value;
        assertEquals(4, hold1.size());
    }



    /** Tests for an undirected graph */
    @Test
    @SuppressWarnings("unchecked")
    public void undirectedgraphcheck() {
        UndirectedGraph<String, String> g
            = new UndirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        String tmp4 = "edgelabel";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1, tmp4);
        Edge e1 = g.add(v1, v2);
        g.remove(e0);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold1 = (ArrayList<Edge>) value;
        assertEquals(false, hold1.contains(e0));
        g.remove(v1, v2);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold2 = (ArrayList<Edge>) value;
        assertEquals(false, hold2.contains(e1));
        ArrayList<Vertex> hold3 = v1.getList("com");
        assertEquals(false, hold3.contains(v2));
        ArrayList<Vertex> hold4 = v2.getList("com");
        assertEquals(false, hold4.contains(v1));
    }

    /** Test to see if predecessors of working correctly. */
    @Test
    @SuppressWarnings("unchecked")
    public void predecessorsuccessor() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "testing";
        String tmp1 = "testing1";
        String tmp2 = "testing2";
        String tmp3 = "testing3";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v1, v0);
        g.remove(e1);
        ArrayList<Vertex> hold = v0.getList("pred");
        ArrayList<Vertex> hold1 = v1.getList("pred");
        ArrayList<Vertex> hold2 = v1.getList("com");
        assertEquals(false, hold.contains(v1));
        assertEquals(false, hold2.contains(v0));
    }

    /** Test for orderingedges. */
    @Test
    @SuppressWarnings("unchecked")
    public void orderingedgestest() {
        DirectedGraph<Integer, Integer> g
            = new DirectedGraph<Integer, Integer>();
        Comparator<Integer> comp = g.naturalOrder();
        int tmp = 0;
        int tmp1 = 1;
        int tmp2 = 2;
        int tmp3 = 3;
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Edge e0 = g.add(v0, v1, 1);
        Edge e1 = g.add(v1, v2, 0);
        Edge e2 = g.add(v2, v3, 2);
        Edge e3 = g.add(v0, v3, 3);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold = (ArrayList<Edge>) value;
        Edge test0 = hold.get(0);
        assertEquals(true, test0.equals(e0));
        g.orderEdges(comp);
        fieldSetup("masterEdge", g);
        ArrayList<Edge> hold1 = (ArrayList<Edge>) value;
        Edge test1 = hold.get(0);
        assertEquals(true, test1.equals(e1));
    }

    /** Iterator Checker. */
    @Test
    @SuppressWarnings("unchecked")
    public void iteration() {
        DirectedGraph<Integer, Integer> g
            = new DirectedGraph<Integer, Integer>();
        int tmp = 0;
        int tmp1 = 1;
        int tmp2 = 2;
        int tmp3 = 3;
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Vertex v3 = g.add(tmp3);
        Iteration<DirectedGraph<Integer, Integer>.Vertex> ran
            = g.vertices();
        Vertex test = ran.next();
        int fintest = (int) test.getLabel();
        assertEquals("basic vertex testing fails",
            0, fintest);
    }

    /** A* Search Tree Tests. */
    @Test
    @SuppressWarnings("unchecked")
    public void simpleShort() {
        DirectedGraph<Integer, Integer> g
            = new DirectedGraph<Integer, Integer>();
        int tmp = 7;
        int tmp1 = 6;
        int tmp2 = 4;
        int tmp3 = 2;
        int tmp4 = 0;
        Vertex S = g.add(tmp);
        Vertex A = g.add(tmp1);
        Vertex B = g.add(tmp2);
        Vertex C = g.add(tmp3);
        Vertex G = g.add(tmp4);
        Edge e0 = g.add(S, A, 1);
        Edge e1 = g.add(S, B, 4);
        Edge e2 = g.add(A, B, 2);
        Edge e3 = g.add(B, C, 2);
        Edge e4 = g.add(A, C, 5);
        Edge e5 = g.add(A, G, 12);
        Edge e6 = g.add(C, G, 3);
        Graphs.shortestPath(g, S, G, Graphs.ZERO_DISTANCER, VERTEX, EDGE);
    }
}
