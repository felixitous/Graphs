package make;

import graph.DirectedGraph;
import graph.Graph;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;


/** Initial class for the 'make' program.
 *  @author Felix Liu
 */
public final class Main {

    /** Entry point for the CS61B make program.  ARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... args) {
        String makefileName;
        String fileInfoName;

        if (args.length == 0) {
            usage();
        }

        makefileName = "Makefile";
        fileInfoName = "fileinfo";

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-f")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    makefileName = args[a];
                }
            } else if (args[a].equals("-D")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    fileInfoName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        ArrayList<String> targets = new ArrayList<String>();

        for (; a < args.length; a += 1) {
            targets.add(args[a]);
        }

        make(makefileName, fileInfoName, targets);
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.exit(1);
    }

    /** String that holds the key of two HashMaps. */
    private static String key = "";

    /** Double that takes in the current time. */
    private static double currTime = 0.0;

    /** HashMap that relates the time to the file. */
    private static HashMap<String, Double> timings
        = new HashMap<String, Double>();

    /** ArrayList which holds the command lines. */
    private static ArrayList<String> commandLines
        = new ArrayList<String>();

    /** ArrayList which holds the keys. */
    private static ArrayList<String> hmm = new ArrayList<String>();

    /** HashMap that relates the target to its parents. */
    private static HashMap<String, ArrayList<String>> parents
        = new HashMap<String, ArrayList<String>>();

    /** HashMap that relates the target to its commands. */
    private static HashMap<String, ArrayList<String>> commands
        = new HashMap<String, ArrayList<String>>();

    /** DirectedGraph which will have the hiearchy of children and parents. */
    private static DirectedGraph<String, String> g
        = new DirectedGraph<String, String>();

    /** HashMap which checks if a vertex is already
     *  in the graph by its label. */
    private static HashMap<String, Graph<String, String>.Vertex> storage
        = new HashMap<String, Graph<String, String>.Vertex>();

    /** Vertex that can be plugged back in for depth-first traversal. */
    private static Graph<String, String>.Vertex traversalIn;

    /** Carry out the make procedure using MAKEFILENAME as the makefile,
     *  taking information on the current file-system state from FILEINFONAME,
     *  and building TARGETS, or the first target in the makefile if TARGETS
     *  is empty.
     */
    private static void make(String makefileName, String fileInfoName,
                             List<String> targets) {
        makeRecord(makefileName);
        timeRecord(fileInfoName);
        makeWhole();
        MakeTraversals<String, String> traverse
            = new MakeTraversals<String, String>(commands, timings, currTime,
                parents);
        for (String s : targets) {
            circularcatch(s);
            traverse.depthFirstTraverse(g, storage.get(s));
            traverse.clearMem();
            traverse.incrementTime();
        }
    }

    /** Checks for circular dependencies. Takes in the string S. */
    static void circularcatch(String s) {
        traversalIn = storage.get(s);
        hierarchy(traversalIn);
    }

    /** Recursive builder. Takes in the vertex IN. */
    static void hierarchy(Graph<String, String>.Vertex in) {
        if (parents.containsKey(in.getLabel())) {
            ArrayList<String> con = parents.get(in.getLabel());
            for (String items : con) {
                if (storage.containsKey(items)) {
                    if (traversalIn.equals(storage.get(items))) {
                        System.err.println("circular catch");
                        System.exit(1);
                    } else {
                        hierarchy(storage.get(items));
                    }
                }
            }
        }
    }

    /** Graph Making function that sets up the graph. */
    static void makeWhole() {
        for (String s : hmm) {
            if (!storage.containsKey(s)) {
                storage.put(s, g.add(s));
            }
            if (parents.containsKey(s)) {
                for (String items : parents.get(s)) {
                    if (!storage.containsKey(items)) {
                        Graph<String, String>.Vertex input = g.add(items);
                        g.add(storage.get(s), input);
                        storage.put(items, input);
                    } else {
                        g.add(storage.get(s), storage.get(items));
                    }
                }
            }
        }
    }

    /** Helps to associate a HashMap with the time built for each file.
     *  Takes in the string FILEINPUT. */
    static void timeRecord(String fileInput) {
        File file = new File(fileInput);
        try {
            Scanner sc = new Scanner(file);
            boolean firstLine = true;
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                input = input.trim();
                if (firstLine) {
                    try {
                        currTime = Double.parseDouble(input);
                        firstLine = false;
                    } catch (NumberFormatException e) {
                        System.err.println("not a valid time");
                        System.exit(1);
                    }
                } else if (!input.equals("")) {
                    String[] info = (input.trim()).split("\\s+");
                    if (info.length == 2) {
                        try {
                            double time = Double.parseDouble(info[1]);
                            timings.put(info[0], time);
                        } catch (NumberFormatException e) {
                            System.err.println("malformed time");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("incorrect amount of arguments");
                        System.exit(1);
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Processes the Make File to structure the
     *  data types. Takes in the string FILEINPUT. */
    static void makeRecord(String fileInput) {
        File file = new File(fileInput);
        try {
            Scanner sc = new Scanner(file);
            int count = 0;
            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                if (input.contains(":")) {
                    sendtoHash(count);
                    parentLine(input);
                } else {
                    commandLine(input);
                }
                count++;
            }
            sendtoHash(count);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Checks if the input or things before the
     *  colon is a valid input. Takes in the string INPUT. */
    static void validTarget(String input) {
        String beforeTrim = input.trim();
        if (beforeTrim.charAt(0) != '#') {
            String[] temp = beforeTrim.split("\\s+");
            if (temp.length != 1) {
                System.err.print("invalid target");
                System.exit(1);
            }
        }
    }

    /** Helps to process the command lines. Takes in the string
     *  INPUT. */
    static void commandLine(String input) {
        if (input.length() != 0) {
            input = input.replaceAll("\t", "    ");
            commandLines.add(input);
        }
    }

    /** Helps to process overhead. Takes in the integer COUNT. */
    static void sendtoHash(int count) {
        if (!key.equals("")) {
            ArrayList<String> temp
                = new ArrayList<String>(commandLines);
            if (commands.containsKey(key)) {
                System.err.println("two commands for one target");
                System.exit(1);
            }
            if (temp.size() != 0) {
                commands.put(key, temp);
                commandLines.clear();
            }
        } else if (count != 0) {
            System.err.println("key error");
            System.exit(1);
        }
    }

    /** Helps to process the parent line. Takes in the string INPUT. */
    static void parentLine(String input) {
        String[] temp = input.split(":");
        validTarget(temp[0]);
        String firstPart = temp[0];
        key = firstPart;
        try {
            String beforeTrim = temp[1].trim();
            String[] temp1 = beforeTrim.split("\\s+");
            ArrayList<String> theReal = new ArrayList<String>();
            for (String item : temp1) {
                theReal.add(item);
            }
            if (parents.containsKey(key)) {
                ArrayList<String> append = parents.get(key);
                for (String item : theReal) {
                    append.add(item);
                }
                parents.put(key, append);
            } else {
                parents.put(key, theReal);
                hmm.add(key);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            hmm.add(key);
        }
    }
}
