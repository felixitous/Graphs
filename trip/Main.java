package trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.math.BigDecimal;
import graph.Graph;
import graph.Weighting;
import graph.Weighter;
import graph.Distancer;
import graph.DirectedGraph;
import graph.Graphs;

/** Initial class for the 'trip' program.
 *  @author Felix Liu
 */
public final class Main {

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                                              true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                                  outFileName);
                System.exit(1);
            }
        }

        request(requestFileName);
        trip(mapFileName);
    }

    /** Helps to process the requests that will be put into the
     *  trip function. Takes in the request REQUESTFILENAME. */
    private static void request(String requestFileName) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            input = input.trim();
            if (!input.equals("")) {
                directions.add(input);
            }
        }
        sc.close();
    }


    /** Print a trip for the request on the standard input to the stsndard
     *  output, using the map data in MAPFILENAME.
     */
    private static void trip(String mapFileName) {
        File file = new File(mapFileName);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String i = sc.nextLine();
                if (i.equals("")) {
                    continue;
                } else {
                    char c = i.charAt(0);
                    if (c == 'L') {
                        processVert(i);
                    } else if (c == 'R') {
                        processEdge(i);
                    } else {
                        System.out.println("not a valid entry");
                        System.exit(0);
                    }
                }
            }
            sc.close();
            for (String items : directions) {
                String[] input = items.split("\\,*\\s+");
                printHeader(input[0]);
                int counter = 0;
                while (counter < input.length - 1) {
                    running(input[counter], input[counter + 1]);
                    counter++;
                }
                count = 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Calculates how many times trip planning is supposed to be done.
     *  Takes in the parameters START1 and END1. */
    static void running(String start1, String end1) {
        Graph<String, String>.Vertex start = concreteV.get(start1);
        Graph<String, String>.Vertex end = concreteV.get(end1);
        ArrayList<Graph<String, String>.Edge> bestPath
            = (ArrayList<Graph<String, String>.Edge>)
                Graphs.shortestPath(world, start, end,
                CROWFLYER, VERTEX, EDGE);
        tripPlanner(bestPath);
        printFooter(end);
    }

    /** Does the rounding for a double. Takes in the parameters
     *  VALUE and PLACES and returns a double. */
    static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /** Prints the header acccording to the vertex. Takes in the
     *  string INPUT. */
    static void printHeader(String input) {
        String title = input;
        System.out.print("From " + title + ":" + "\n\n");
    }

    /** Prints the end according to the vertex. Takes in the
     *  vertex INPUT. */
    static void printFooter(Graph<String, String>.Vertex input) {
        String title = input.getLabel();
        System.out.print(" to " + title + "." + "\n");
    }

    /** Function helps to analyze the ArrayList and print the paths. Takes in
     *  the path BESTPATH. */
    static void tripPlanner(ArrayList<Graph<String, String>.Edge> bestPath) {
        Iterator<Graph<String, String>.Edge> iterator = bestPath.iterator();
        Graph<String, String>.Edge first;
        first = iterator.next();
        current = first;
        while (iterator.hasNext()) {
            Graph<String, String>.Edge second;
            second = iterator.next();
            if (verifySimilar(first, second)) {
                sameTrip(first, second);
                first = second;
            } else {
                String ready = stringBuilder(current);
                System.out.println(ready);
                first = second;
                current = first;
                total = 0;
            }
        }
        String ready = stringBuilder(current);
        int total1 = ready.length();
        String newready = ready.substring(0, total1 - 1);
        total = 0;
        System.out.print(newready);
    }

    /** Checks if the edges are similar. Takes in the the edge
     *  FIRST and SECOND and returns a boolean. */
    static boolean verifySimilar(Graph<String, String>.Edge first,
                                Graph<String, String>.Edge second) {
        Graph<String, String>.Edge tmp1 = concreteE.get(first.getLabel());
        String[] firstInfo = eR.get(tmp1);
        Graph<String, String>.Edge tmp2 = concreteE.get(second.getLabel());
        String[] secondInfo = eR.get(tmp2);
        String[] firstLabel = first.getLabel().split("\\s+");
        String[] secondLabel = second.getLabel().split("\\s+");
        if (firstLabel[0].equals(secondLabel[0])) {
            if (firstInfo[1].equals(secondInfo[1])
                    || firstInfo[1].equals(opposite(secondInfo[1]))) {
                return true;
            }
        }
        return false;
    }


    /** Helps to add up the distance so it can be set up for printing. Takes
     *  in the edges FIRST and SECOND. */
    static void sameTrip(Graph<String, String>.Edge first,
                        Graph<String, String>.Edge second) {
        Graph<String, String>.Edge first1 = concreteE.get(first.getLabel());
        String[] firstInfo = eR.get(first1);
        Graph<String, String>.Edge second2 = concreteE.get(second.getLabel());
        String[] secondInfo = eR.get(second2);
        double tmp1 = Double.parseDouble(firstInfo[0]);
        double tmp2 = Double.parseDouble(secondInfo[0]);
        if (total == 0.0) {
            total += tmp1 + tmp2;
        } else {
            total += tmp2;
        }
    }

    /** Helps to build string necessary to be print.
     *  @param: an edge, INPUT.
     *  @return: Returns the actual string. */
    static String stringBuilder(Graph<String, String>.Edge input) {
        String beforebegin = Integer.toString(count);
        count++;
        String begin = "Take";
        Graph<String, String>.Edge tmp = concreteE.get(input.getLabel());
        String[] firstInfo = eR.get(tmp);
        String[] firstLabel = input.getLabel().split("\\s+");
        String next1 = firstLabel[0];
        String next2;
        if (eR.containsKey(input)) {
            next2 = actualD(firstInfo[1]);
        } else {
            next2 = nuhuh(actualD(firstInfo[1]));
        }
        String next3 = "for";
        String next4;
        if (total == 0.0) {
            total += Double.parseDouble(firstInfo[0]);
            total = round(total, 1);
            next4 = Double.toString(total);
        } else {
            total = round(total, 1);
            next4 = Double.toString(total);
        }
        String next5 = "miles.";
        String complete = beforebegin + ". " + begin
                            + " " + next1 + " " + next2
                            + " " + next3 + " " + next4 + " " + next5;
        return complete;
    }

    /** Takes in the string IN and helps to
     *  switch the input. Returns a string. */
    static String nuhuh(String in) {
        switch(in) {
        case "north":
            return "south";
        case "south":
            return "north";
        case "west":
            return "east";
        case "east":
            return "west";
        default:
            return null;
        }
    }

    /** Helps to switch the direction labels. Takes in
     *  the string IN and returns a string that's switched. */
    static String opposite(String in) {
        switch(in) {
        case "NS":
            return "SN";
        case "SN":
            return "NS";
        case "WE":
            return "EW";
        case "EW":
            return "WE";
        default:
            return null;
        }
    }

    /** Takes in the string IN and helps to
     *  switch the input. Returns the string. */
    static String actualD(String in) {
        switch(in) {
        case "NS":
            return "south";
        case "SN":
            return "north";
        case "WE":
            return "east";
        case "EW":
            return "west";
        default:
            return null;
        }
    }

    /** Takes in LINE and helps to process the string
     *  and relate the vertex to the coordinates. */
    static void processVert(String line) {
        String[] hold = line.split("\\s+");
        if (hold.length == 4) {
            String label = hold[1];
            double[] coor = new double[2];
            coor[0] = Double.parseDouble(hold[2]);
            coor[1] = Double.parseDouble(hold[3]);
            Graph<String, String>.Vertex tmp = world.add(label);
            vR.put(tmp, coor);
            concreteV.put(label, tmp);
        } else {
            System.out.println("incorrect input for vertex");
            System.exit(0);
        }
    }

    /** Takes in LINE and helps to process the string
     *  and relate the information on edges
     *  to the coordinates. */
    static void processEdge(String line) {
        String[] hold = line.split("\\s+");
        if (hold.length == 6) {
            String label = hold[2] + " " + hold[1]
                            + " " + hold[3] + " " + hold[5];
            String[] dir = new String[2];
            dir[0] = hold[3];
            dir[1] = hold[4];
            Graph<String, String>.Vertex tmp
                = concreteV.get(hold[1]);
            Graph<String, String>.Vertex tmp1
                = concreteV.get(hold[5]);
            Graph<String, String>.Edge tmpE = world.add(tmp, tmp1, label);
            Graph<String, String>.Edge othertmpE = world.add(tmp1, tmp, label);
            eR.put(tmpE, dir);
            concreteE.put(label, tmpE);
        }
    }


    /** Weighting that weighs the edges for
     *  Integers. */
    public static final Weighting<String> EDGE =
        new Weighting<String>() {

            @Override
            public double weight(String x) {
                Graph<String, String>.Edge process = concreteE.get(x);
                String[] info = eR.get(process);
                try {
                    double value = Double.parseDouble(info[0]);
                    return value;
                } catch (NumberFormatException e) {
                    System.out.println("bad value for edge");
                    System.exit(1);
                }
                return 0.0;
            }
        };

    /** Weighter that weights the vertices for Integers. */
    public static final Weighter<String> VERTEX =
        new Weighter<String>() {
            private HashMap<String, Double> weights
                = new HashMap<String, Double>();

            @Override
            public double weight(String x) {
                return weights.get(x);
            }

            @Override
            public void setWeight(String x, double v) {
                weights.put(x, v);
            }
        };

    /** Distancer that calculates the heuristic for the shortest path. */
    public static final Distancer<String> CROWFLYER =
        new Distancer<String>() {
            double distance(double x1, double y1, double x2, double y2) {
                return Math.sqrt((x1 - x2) * (x1 - x2)
                    + (y1 - y2) * (y1 - y2));
            }

            @Override
            public double dist(String v0, String v1) {
                Graph<String, String>.Vertex first = concreteV.get(v0);
                Graph<String, String>.Vertex second = concreteV.get(v1);
                double[] firstValue = vR.get(first);
                double[] secondValue = vR.get(second);
                return distance(firstValue[0], firstValue[1],
                    secondValue[0], secondValue[1]);
            }
        };

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.exit(1);
    }

    /** Counter that holds the number to count. */
    private static int count = 1;

    /** Universal double that holds the distance of the same vertex. */
    private static double total = 0.0;

    /** ArrayList that contains Strings for trip processing. */
    private static ArrayList<String> directions
        = new ArrayList<String>();

    /** Current Edge which is similar. */
    private static Graph<String, String>.Edge current;

    /** HashMap to store Vertices and their given coordinates. */
    private static HashMap<Graph<String, String>.Vertex, double[]> vR
        = new HashMap<Graph<String, String>.Vertex, double[]>();

    /** HashMap to store Edges and their given statuses. */
    private static HashMap<Graph<String, String>.Edge, String[]> eR
        = new HashMap<Graph<String, String>.Edge, String[]>();

    /** HashMap to store Labels and their respective Vertices. */
    private static HashMap<String, Graph<String, String>.Vertex> concreteV
        = new HashMap<String, Graph<String, String>.Vertex>();

    /** HashMap to store Labels and their respective Edges. */
    private static HashMap<String, Graph<String, String>.Edge> concreteE
        = new HashMap<String, Graph<String, String>.Edge>();

    /** DirectedGraph that provides a graph to be built by the
     *  trip file. */
    private static DirectedGraph<String, String> world
        = new DirectedGraph<String, String>();

}
