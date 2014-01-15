package make;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import graph.Traversal;
import graph.Graph;

/** Class extends the Traversals in graph for customization
 *  in the design of make.
 *  @author Felix Liu */
public class MakeTraversals<VLabel, ELabel>
    extends graph.Traversal<VLabel, ELabel> {
    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */

    /** HashMap of keys from Map. */
    private static HashMap<String, ArrayList<String>> commands;

    /** HashMap of Dependencies. */
    private static HashMap<String, ArrayList<String>> parents;

    /** HashMap of Double according to keys from Map. */
    private static HashMap<String, Double> times;

    /** Double which represents the current time. */
    private static double currTime = 0.0;

    /** Double which represents the time as visited node. */
    private static double visitTime = 0.0;

    /** Instantiates the traversal according to a particular HashMap
     *  for printing. Takes in the hashmap FROMMAKE and FROMMAKE1 and
     *  the double NOW and DADDY or parents. */
    MakeTraversals(HashMap<String, ArrayList<String>> fromMake,
                    HashMap<String, Double> fromMake1, double now,
                    HashMap<String, ArrayList<String>> daddy) {
        commands = fromMake;
        times = fromMake1;
        currTime = now;
        parents = daddy;
    }

    /** ArrayList that contains which files not to compile. */
    private ArrayList<VLabel> taboo = new ArrayList<VLabel>();

    /** ArrayList which holds visited and previsited vertices. */
    private ArrayList<VLabel> traversed = new ArrayList<VLabel>();

    /** Method that aids in the printing of the arraylist. */
    void printArray() {
        System.out.println(taboo);
    }

    /** Method that helps to clear the array after each performed target. */
    void clearMem() {
        traversed.clear();
    }

    /** Method that increments the current time. */
    void incrementTime() {
        currTime++;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
        Graph<VLabel, ELabel>.Vertex v1 = e.getV(v0);
        if (taboo.contains(v1.getLabel())) {
            System.err.println("circular path found");
            System.exit(1);
        }
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
        taboo.add(v.getLabel());
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
        if (times.containsKey(v.getLabel())) {
            if (parents.containsKey(v.getLabel())) {
                ArrayList<String> dependencies
                    = parents.get(v.getLabel());
                for (String item : dependencies) {
                    try {
                        if (times.get(item) > times.get(v.getLabel())) {
                            compile(v);
                        }
                    } catch (NullPointerException e) {
                        System.err.println("issues");
                        System.exit(1);
                    }
                }
            }
        } else {
            compile(v);
        }
    }

    /** Compile. Takes in the vertex V */
    void compile(Graph<VLabel, ELabel>.Vertex v) {
        if (commands.containsKey(v.getLabel())) {
            ArrayList<String> temp = commands.get(v.getLabel());
            for (String s : temp) {
                System.out.println(s);
            }
            times.put((String) v.getLabel(), currTime);
        }
        taboo.remove(v.getLabel());
        if (parents.containsKey(v.getLabel())) {
            ArrayList<String> remover
                = parents.get(v.getLabel());
            for (String items : remover) {
                taboo.remove(items);
            }
        }
    }

    /** Comparator that looks at the time value of the
     *  particular double. */
    private final Comparator<Double> timer =
        new Comparator<Double>() {
            @Override
            public int compare(Double x1, Double x2) {
                if (x2 == -1.0) {
                    return -1;
                } else if (x1 > x2 || x1 == x2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
}
