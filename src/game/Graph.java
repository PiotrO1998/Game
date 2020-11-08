package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Class models a Graph, to allow a BFS traversal to find the shortest possible path for the SmartEnemy.
 *
 * @author Pedro Caetano
 * @author Piotr Obara
 * @version 1.3
 */
class Graph implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Log log = new Log(getClass().getSimpleName());
    private final int numberVertices;   // No. of vertices
    private final LinkedList<Integer>[] adj; //Adjacency Lists

    /**
     * Initialises a Graph with given number of vertices.
     *
     * @param numberVertices Number of vertices in the Graph
     */
    Graph(int numberVertices) {
        log.setImportance(1);
        this.numberVertices = numberVertices;

        //create a list of adjacency's for each vertice
        adj = new LinkedList[numberVertices];
        for (int i = 0; i < numberVertices; ++i) {
            adj[i] = new LinkedList();
        }
    }

    /**
     * Adds an edge between two vertices in the Graph.
     *
     * @param verticeA Vertice to pair with verticeB
     * @param verticeB Vertice to pair with verticeA
     */
    void addEdge(int verticeA, int verticeB) {
        adj[verticeA].add(verticeB);
    }

    /**
     * Finds a path between verticeStart and verticeEnd.
     *
     * @param verticeStart Vertice to start path finding from
     * @param verticeEnd   Vertice to find
     * @return String representing possible path
     */
    ArrayList<Integer> findPath(int verticeStart, int verticeEnd) {
        //init lists and arrays required to keep track of progress through the search
        Queue<Integer> q = new LinkedList<>();
        boolean[] visited = new boolean[numberVertices];
        String[] pathTo = new String[numberVertices];

        //add current vertice
        q.add(verticeStart);
        pathTo[verticeStart] = verticeStart + " ";

        //run BFS on each vertice, adding neighbours, until it is found
        while (q.peek() != null) {
            if (runBFS(q.poll(), verticeEnd, visited, q, pathTo)) {
                q.clear(); //stops searching, Player has been found
            }
        }

        //create path to end vertice
        ArrayList<Integer> path = new ArrayList<>();
        if (pathTo[verticeEnd] != null) {
            Scanner line = new Scanner(pathTo[verticeEnd]);
            while (line.hasNextInt()) {
                path.add(line.nextInt());
            }
        } else {
            log.add("No valid move");
        }
        return path;
    }

    /**
     * Executes a logical BFS given the current state of the graph.
     *
     * @param verticeStart Vertice to begin BFS on
     * @param verticeEnd   Vertice to find
     * @param visited      Array of visited Vertices
     * @param q            Queue of Vertices to process
     * @param pathTo       pathTo generated so far
     * @return True if found
     */
    private boolean runBFS(int verticeStart, int verticeEnd, boolean[] visited, Queue<Integer> q, String[] pathTo) {
        if (visited[verticeStart]) {
            //do nothing as has already been indexed
        } else if (verticeStart == verticeEnd) {
            //end vertice reached
            return true;
        } else {
            //add add to queue and path
            visited[verticeStart] = true;
            for (int nextVertex : adj[verticeStart]) {
                pathTo[nextVertex] = pathTo[verticeStart] + nextVertex + " ";
                q.add(nextVertex);
            }
        }
        return false;
    }
}