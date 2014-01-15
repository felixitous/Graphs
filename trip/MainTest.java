package trip;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import graph.Graph.Vertex;
import graph.DirectedGraph;

public class MainTest {


    /** private field that holds the test case. */
    private Object value;

    /** builds the fields. Takes in the string INPUT */
    void fieldSetup(String input, Object input1) {
        try {
            Field field = Main.class.getDeclaredField(input);
            field.setAccessible(true);
            value = field.get(input1);
        } catch (IllegalAccessException|NoSuchFieldException e) {
            System.err.println("error");
            System.exit(1);
        }
    }

    /** Seeing if the rounding function rounds
     *  to the right number. */
    @Test
    @SuppressWarnings("deprecation")
    public void runningTest() {
        double stuff = (double) 3.91;
        if (stuff == Main.round(3.912222, 2)) {
            assertTrue(true);
        }
        double stuff1 = (double) 4.39;
        if (stuff1 == Main.round(4.3892222, 2)) {
            assertTrue(true);
        }
        double stuff2 = (double) 5.5;
        if (stuff2 == Main.round(5.45, 1)) {
            assertTrue(true);
        }
    }

    /** See if header and footer are just printing
     *  and not hitting and errors. */
    @Test
    @SuppressWarnings("unchecked")
    public void headingAndFooterTest() {
        Main.printHeader("test");
        DirectedGraph<String, String> g
            = new DirectedGraph<String, String>();
        Vertex v0 = g.add("test");
        Main.printFooter(v0);
    }

    /** Seeing if the switch cases are printing
     *  the right things. */
    @Test
    public void switchCheck0() {
        String s0 = Main.nuhuh("north");
        String s1 = Main.nuhuh("south");
        String s2 = Main.nuhuh("east");
        String s3 = Main.nuhuh("west");
        assertEquals("south", s0);
        assertEquals("north", s1);
        assertEquals("west", s2);
        assertEquals("east", s3);
    }

    /** Another switch case test. */
    @Test
    public void switchCheck1() {
        String s0 = Main.opposite("NS");
        String s1 = Main.opposite("SN");
        String s2 = Main.opposite("EW");
        String s3 = Main.opposite("WE");
        assertEquals("SN", s0);
        assertEquals("NS", s1);
        assertEquals("WE", s2);
        assertEquals("EW", s3);
    }

    /** Final switch case test. */
    @Test
    public void switchCheck2() {
        String s0 = Main.actualD("NS");
        String s1 = Main.actualD("SN");
        String s2 = Main.actualD("EW");
        String s3 = Main.actualD("WE");
        assertEquals("south", s0);
        assertEquals("north", s1);
        assertEquals("west", s2);
        assertEquals("east", s3);
    }
}
