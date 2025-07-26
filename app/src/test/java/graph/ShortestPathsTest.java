package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

import java.util.LinkedList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /* Returns the Graph loaded from the file with filename fn located in
     * src/test/resources at test time. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        String filePath = getGraphResource(fn);
        try {
          result = ShortestPaths.parseGraph("basic", filePath);
        } catch (FileNotFoundException e) {
          fail("Could not find graph " + fn);
        }
        return result;
    }

    /** Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below. */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2+2, 4);
    }

    /** Minimal test case to check the path from A to B in Simple0.txt */
    @Test
    public void test01Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  b);
        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
    }

    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */
    @Test
    public void test02Simple1() {
        Graph g = loadBasicGraph("Simple1.txt");
        ShortestPaths sp = new ShortestPaths();
        Node s = g.getNode("S");
        sp.compute(s);
        Node d = g.getNode("D");
        LinkedList<Node> sdPath = sp.shortestPath(d);
        assertEquals(sdPath.size(), 3);
        assertEquals(sdPath.getFirst(), s);
        assertEquals(sdPath.getLast(), d);
        assertEquals(sp.shortestPathLength(d), 7.0, 1e-6);
    }


    @Test
    public void test03Simple2() {
        Graph g = loadBasicGraph("Simple2.txt");
        ShortestPaths sp = new ShortestPaths();
        Node d = g.getNode("D");
        sp.compute(d);
        Node b = g.getNode("B");
        LinkedList<Node> dbPath = sp.shortestPath(b);
        assertEquals(dbPath.size(), 5);
        assertEquals(dbPath.getFirst(), d);
        assertEquals(dbPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 9.0, 1e-6);
    }

    /**
     * Makes a simple graph that goes from 0-20, with distance 1 length between all nodes
     */
    @Test
    public void test03BasicGraph() {
        Graph g = new Graph();
        for(int i = 0; i <= 19; i++) {
            String iString = String.valueOf(i);
            String iString1 = String.valueOf(i+1);
            Node j = g.getNode(iString);
            Node j1 = g.getNode(iString1);
            g.addEdge(j, j1, 1);
        }
        ShortestPaths sp = new ShortestPaths();
        Node zero = g.getNode("0");
        sp.compute(zero);
        Node twenty = g.getNode("20");
        LinkedList<Node> zerotwentyPath = sp.shortestPath(twenty);
        assertEquals(zerotwentyPath.size(), 21);
        assertEquals(zerotwentyPath.getFirst(), zero);
        assertEquals(zerotwentyPath.getLast(), twenty);
        assertEquals(sp.shortestPathLength(twenty), 20.0, 1e-6);
    }

    /**
     * Makes sure that if we have a-b and b-a, that the shortest path from a to itself is still 0.0
     */
    @Test
    public void test04UndirectedMayhem() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);
        g.addEdge(b, a, 1);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        LinkedList<Node> abPath = sp.shortestPath(a);
        assertEquals(abPath.size(), 1);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), a);
        assertEquals(sp.shortestPathLength(a), 0.0, 1e-6);
    }

    @Test
    public void test05UnreachableNode() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(c, a, 1);
        ShortestPaths sp = new ShortestPaths();
        sp.compute(a);
        LinkedList<Node> acPath = sp.shortestPath(c);
        assertEquals(acPath, null);
        assertEquals(sp.shortestPathLength(c), Double.POSITIVE_INFINITY, 1e-6);
    }
}
