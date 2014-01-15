package graph;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;


/** Assorted graph algorithms.
 *  @author Felix Liu
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        Comparator<Graph<VLabel, ELabel>.Vertex> flip = orderVert(vweighter);
        ArrayList<Graph<VLabel, ELabel>.Vertex> closedset
            = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openset
            = new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(9, flip);
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Vertex>
            camefrom = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gscore
            = new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        openset.add(V0);
        gscore.put(V0, (double) 0);
        vweighter.setWeight(V0.getLabel(), gscore.get(V0)
            + h.dist(V0.getLabel(), V1.getLabel()));
        while (openset.size() != 0) {
            Graph<VLabel, ELabel>.Vertex current = openset.poll();
            if (current.equals(V1)) {
                G.reconstruct(camefrom, current, eweighter);
                return G.pathBuilder();
            }
            closedset.add(current);
            for (Iteration<Graph<VLabel, ELabel>.Vertex> iterator
                    = G.neighbors(current); iterator.hasNext();) {
                Graph<VLabel, ELabel>.Vertex neighbor = iterator.next();
                Graph<VLabel, ELabel>.Edge chosenPath
                    = G.getEdge(current, neighbor);
                Double tmpgscore = gscore.get(current)
                    + eweighter.weight(chosenPath.getLabel());
                Double tmpfscore = tmpgscore
                    + h.dist(neighbor.getLabel(), V1.getLabel());
                if (closedset.contains(neighbor)
                    && tmpfscore >= vweighter.weight(neighbor.getLabel())) {
                    continue;
                } else if (!openset.contains(neighbor)
                    || tmpfscore < vweighter.weight(neighbor.getLabel())) {
                    camefrom.put(neighbor, current);
                    gscore.put(neighbor, tmpgscore);
                    vweighter.setWeight(neighbor.getLabel(), tmpfscore);
                    if (!openset.contains(neighbor)) {
                        openset.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        Comparator<Graph<VLabel, ELabel>.Vertex> flip = orderVert();
        ArrayList<Graph<VLabel, ELabel>.Vertex> closedset
            = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> openset
            = new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(9, flip);
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Vertex>
            camefrom = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gscore
            = new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        openset.add(V0);
        gscore.put(V0, (double) 0);
        V0.getLabel().setWeight(gscore.get(V0)
            + h.dist(V0.getLabel(), V1.getLabel()));
        while (openset.size() != 0) {
            Graph<VLabel, ELabel>.Vertex current = openset.poll();
            if (current.equals(V1)) {
                return pathBuilderx(G, G.reconstruct(camefrom, current));
            }
            closedset.add(current);
            for (Iteration<Graph<VLabel, ELabel>.Vertex> iterator
                    = G.neighbors(current); iterator.hasNext();) {
                Graph<VLabel, ELabel>.Vertex neighbor = iterator.next();
                Graph<VLabel, ELabel>.Edge chosenPath
                    = getEdgex(current, neighbor, G);
                Double tmpgscore = gscore.get(current)
                    + (chosenPath.getLabel()).weight();
                Double tmpfscore = tmpgscore
                    + h.dist(neighbor.getLabel(), V1.getLabel());
                if (closedset.contains(neighbor)
                    && tmpfscore >= neighbor.getLabel().weight()) {
                    continue;
                } else if (!openset.contains(neighbor)
                    || tmpfscore < neighbor.getLabel().weight()) {
                    camefrom.put(neighbor, current);
                    gscore.put(neighbor, tmpgscore);
                    neighbor.getLabel().setWeight(tmpfscore);
                    if (!openset.contains(neighbor)) {
                        openset.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    /** Comparator takes in the weighter VWEIGHTER to determine and returns
     *  a comparator so priority queue can have something to compare with.
     *  VLABEL and ELABEL are the types. */
    static <VLabel, ELabel>
    Comparator<Graph<VLabel, ELabel>.Vertex>
    orderVert(final Weighter<? super VLabel> vweighter) {
        Comparator<Graph<VLabel, ELabel>.Vertex> change
            = new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                @Override
                public int compare(Graph<VLabel, ELabel>.Vertex x1,
                    Graph<VLabel, ELabel>.Vertex x2) {
                    return Double.compare(vweighter.weight(x1.getLabel()),
                        vweighter.weight(x2.getLabel()));
                }
            };
        return change;
    }

    /** VLABEL ELABEL.
     *  List of Edge constructor and returns and ArrayList<Edge>. Takes
     *  in the graph GRAPHX and stack STACK. */

    static <VLabel extends Weightable, ELabel extends Weighted>
    ArrayList<Graph<VLabel, ELabel>.Edge>
    pathBuilderx(Graph<VLabel, ELabel> graphx,
                Stack<Graph<VLabel, ELabel>.Vertex> stack) {
        ArrayList<Graph<VLabel, ELabel>.Edge> bestPath
            = new ArrayList<Graph<VLabel, ELabel>.Edge>();
        Graph<VLabel, ELabel>.Vertex tmp;
        tmp = stack.pop();
        while (!stack.empty()) {
            Graph<VLabel, ELabel>.Vertex tmp1 = stack.pop();
            Graph<VLabel, ELabel>.Edge add = getEdgex(tmp, tmp1, graphx);
            bestPath.add(add);
            tmp = tmp1;
        }
        return bestPath;
    }

    /** Method takes in two vertices and returns the
     *  edge that corresponds with the two vertices.
     *  Takes in V0 and V1. Takes in VLABEL ELABEL GRAPH. */
    static <VLabel extends Weightable, ELabel extends Weighted>
    Graph<VLabel, ELabel>.Edge getEdgex(Graph<VLabel, ELabel>.Vertex v0,
        Graph<VLabel, ELabel>.Vertex v1,
        Graph<VLabel, ELabel> graph) {
        Stack<Graph<VLabel, ELabel>.Edge> hold
            = new Stack<Graph<VLabel, ELabel>.Edge>();
        for (Graph<VLabel, ELabel>.Edge items : graph.edges()) {
            Graph<VLabel, ELabel>.Vertex tmp0 = items.getV0();
            Graph<VLabel, ELabel>.Vertex tmp1 = items.getV1();
            if (tmp0.equals(v0) && tmp1.equals(v1)) {
                hold.push(items);
            }
        }
        if (hold.size() > 0) {
            return smallestEdgex(hold);
        } else {
            System.err.print("no edge found");
            return null;
        }
    }

    /** Determines which edge is the smallest. Takes in INPUT ELABEL VLABEL
     *  and returns an edge. */
    static <VLabel extends Weightable, ELabel extends Weighted>
    Graph<VLabel, ELabel>.Edge
    smallestEdgex(Stack<Graph<VLabel, ELabel>.Edge> input) {
        Graph<VLabel, ELabel>.Edge test = input.pop();
        try {
            if (input.size() == 1) {
                return test;
            } else {
                Graph<VLabel, ELabel>.Edge start;
                start = test;
                while (!input.empty()) {
                    Graph<VLabel, ELabel>.Edge comp = input.pop();
                    double tmp = start.getLabel().weight();
                    double tmp2 = comp.getLabel().weight();
                    if (tmp >= tmp2) {
                        start = comp;
                    }
                }
                return start;
            }
        } catch (NullPointerException e) {
            return test;
        }
    }

    /** Comparator for vertex extension.
     *  VLABEL and ELABEL are the types and returns a comparator
     *  for vertices. */
    static <VLabel extends Weightable, ELabel extends Weighted>
    Comparator<Graph<VLabel, ELabel>.Vertex>
    orderVert() {
        Comparator<Graph<VLabel, ELabel>.Vertex> change
            = new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                @Override
                public int compare(Graph<VLabel, ELabel>.Vertex x1,
                    Graph<VLabel, ELabel>.Vertex x2) {
                    return Double.compare(x1.getLabel().weight(),
                        x2.getLabel().weight());
                }
            };
        return change;
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };

}
