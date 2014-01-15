package graph;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import graph.Graph.Vertex;
import graph.Graph.Edge;
import java.util.ArrayList;

/** Junit Tests for Traversal.java
 *  @author Felix Liu. */

public class TraversalTest {

    /** private field that holds the test case. */
    private Object value;

    public class Definite<VLabel, ELabel>
        extends Traversal<VLabel, ELabel> {

        ArrayList<String> test = new ArrayList<String>();

        /** Constructor. */
        Definite() {
        };

        void printArray() {
            System.out.println(test);
        }

        /** Previsit Extension for graph E and vertex V0. */
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v0) {
            System.out.println("preVisit");
        }

        /** Visit Extension for vertex V. */
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            System.out.println("visit");
        }

        /** PostVisit extension for vertex V. */
        protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
            System.out.println("postvisit");
        }
    }

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

    /** Creating graph and running test for depthfirst traversals. */
    @Test
    @SuppressWarnings("unchecked")
    public void depthfirst() {
        DirectedGraph<String, String> g = new DirectedGraph<String, String>();
        String tmp = "v0";
        String tmp1 = "v1";
        String tmp2 = "v2";
        Vertex v0 = g.add(tmp);
        Vertex v1 = g.add(tmp1);
        Vertex v2 = g.add(tmp2);
        Edge e0 = g.add(v0, v1);
        Edge e1 = g.add(v1, v2);
        Edge e2 = g.add(v2, v0);
        Definite<String, String> test
            = new Definite<String, String>();
        System.out.println("my depth");
        test.depthFirstTraverse(g, v0);
        test.printArray();
    }

    /** Create and run tests for breadthfirst traversal. */
    @Test
    @SuppressWarnings("unchecked")
    public void breadthfirst() {
        DirectedGraph<String, String> g
            = new DirectedGraph<String, String>();
        String tmpA = "A";
        String tmpB = "B";
        String tmpC = "C";
        String tmpD = "D";
        String tmpE = "E";
        String tmpF = "F";
        String tmpG = "G";
        String tmpH = "H";
        Vertex vA = g.add(tmpA);
        Vertex vB = g.add(tmpB);
        Vertex vC = g.add(tmpC);
        Vertex vD = g.add(tmpD);
        Vertex vE = g.add(tmpE);
        Vertex vF = g.add(tmpF);
        Vertex vG = g.add(tmpG);
        Vertex vH = g.add(tmpH);
        Edge e0 = g.add(vA, vB);
        Edge e1 = g.add(vA, vC);
        Edge e2 = g.add(vB, vD);
        Edge e3 = g.add(vB, vE);
        Edge e4 = g.add(vE, vH);
        Edge e5 = g.add(vC, vF);
        Edge e6 = g.add(vC, vG);
        Definite<String, String> test
            = new Definite<String, String>();
        test.breadthFirstTraverse(g, vA);
    }



}


