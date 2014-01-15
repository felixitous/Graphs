package trip;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    trip.MainTest.class,
})

/** Collects all the UnitTestClasses so RunTests can run
 *  everything as a suite.
 *  @author Felix Liu
 */
public class UnitTestSuite {
}
