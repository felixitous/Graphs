Graphs
======

In this project, we needed to develop a graph package on our own for the two clients:
- Make: a compiler simulator
- Trip: a shortest path simulator ( similar to google maps )

The first part was the creation of the graph package which utilized generics in Java, allowing the package to accept a variety of inputs. The package included important algorithms such as:
- A* Search
- Depth-First Search
- Breadth-First Search

and many more.

The trip package needed to parse text information, build a map around that information, and use a DirectedGraph to find the shortest path to the desired point. It would also sandwich information into one if they were paths with the same name.

A few snippets of the code:

A* Search Algorithm
===================

```java
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
```

Depth-First Search/Traversal
============================
- done recursively
- Pre-vist, Visit, Post-Visit functions to perform actions at those particular points

```java
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        v.discover();
        visit(v);
        Iteration<Graph<VLabel, ELabel>.Edge> iterator = G.outEdges(v);
        while (iterator.hasNext()) {
            Graph<VLabel, ELabel>.Edge current = iterator.next();
            if (!current.status("explored")) {
                Graph<VLabel, ELabel>.Vertex passer = current.getV1();
                if (!passer.status("discovered")) {
                    preVisit(current, current.getV0());
                    current.discover();
                    depthFirstTraverse(G, passer);
                }
            }
        }
        v.explore();
        postVisit(v);
    }
```

Breadth-First Traversal
=======================
```java
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        LinkedList<Graph<VLabel, ELabel>.Vertex> queue
            = new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        ArrayList<Graph<VLabel, ELabel>.Vertex> vList
            = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        queue.add(v);
        visit(v);
        v.discover();
        while (queue.size() != 0) {
            vList.add(queue.removeFirst());
            for (Graph<VLabel, ELabel>.Vertex item : vList) {
                Iteration<Graph<VLabel, ELabel>.Edge> iterator
                    = G.outEdges(item);
                while (iterator.hasNext()) {
                    Graph<VLabel, ELabel>.Edge current = iterator.next();
                    Graph<VLabel, ELabel>.Vertex tempV = current.getV0();
                    Graph<VLabel, ELabel>.Vertex passer = current.getV1();
                    if (!passer.status("discovered")) {
                        preVisit(current, tempV);
                        passer.discover();
                        visit(passer);
                        queue.add(passer);
                    }
                }
            }
        }
        for (Graph<VLabel, ELabel>.Vertex post : vList) {
            postVisit(post);
        }
    }
```







