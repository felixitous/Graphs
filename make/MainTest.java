package make;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;

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

    /** See if getting from record file actually
     *  throws an error. */
    @Test
    public void validTargetInput() {
        Main.validTarget("#thecat");
        Main.validTarget("cat");
        Main.validTarget("cat    ");
    }

    /** See if the command line is printing
     *  right. */
    @Test
    public void commandLineTest() {
        Main.commandLine("i don't know if"
                        + "this works.");
    }

    /** See if the parent line is printing
     * right. */
    @Test
    public void parentLineTest() {
        Main.parentLine("the: aefj tewf");
        Main.parentLine("awoeijj");
        try {
            Main.parentLine("");
        } catch (StringIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /** See if make whole does anything and processes
     *  a graph correctly. */
    @Test
    public void makeWholeTest() {
        Main.makeWhole();
    }
}
