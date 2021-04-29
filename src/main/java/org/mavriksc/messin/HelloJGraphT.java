package org.mavriksc.messin;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public final class HelloJGraphT {
    private HelloJGraphT() {
    }

    public static void main(String[] args) {
        Map<Integer, List<Cell>> schedule = new HashMap<>();

        Cell[][] map = makeMap(32);
        map[7][15].setHalite(0);
        Graph<Cell, DefaultEdge> cellGraph = makeCellGraph(map);

        getSchedule(map[7][15], cellGraph, schedule);
    }

    private static Map<Integer, Cell> getSchedule(Cell origin, Graph<Cell, DefaultEdge> graph, Map<Integer, List<Cell>> schedule) {
        List<Cell> neighbors = Graphs.neighborListOf(graph, origin);
        neighbors.sort((c1, c2) -> c2.getHalite() - c1.getHalite());
        //neighbors.forEach(c -> System.out.println(c.toString() + "Halite:" + c.getHalite()));
        for (Cell c: neighbors){

        }
        return null;
    }

    private static void breadthFirstOutput(Graph<Cell, DefaultEdge> g, Cell source) {
        BreadthFirstIterator<Cell, DefaultEdge> breadthFirstIterator = new BreadthFirstIterator<>(g, source);
        int counter = 1;
        while (breadthFirstIterator.hasNext()) {
            Cell c = breadthFirstIterator.next();
            System.out.println(counter++ + " " + c.toString());
        }
    }

    private static void depthFirstOutput(Graph<Cell, DefaultEdge> g, Cell source) {
        DepthFirstIterator<Cell, DefaultEdge> depthFirstIterator = new DepthFirstIterator<>(g, source);
        int counter = 1;
        while (depthFirstIterator.hasNext()) {
            Cell c = depthFirstIterator.next();
            System.out.println(counter++ + " " + c.toString());
        }
    }

    private static Cell[][] makeMap(int dim) {
        Cell[][] map = new Cell[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                map[i][j] = new Cell(ThreadLocalRandom.current().nextInt(1000), i, j);
            }
        }
        return map;
    }

    private static Graph<Cell, DefaultEdge> makeCellGraph(Cell[][] map) {
        Graph<Cell, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                g.addVertex(map[i][j]);
                if (i > 0) {
                    g.addEdge(map[i][j], map[i - 1][j]);
                }
                if (i == map.length - 1) {
                    g.addEdge(map[i][j], map[(i + 1) % map.length][j]);
                }
                if (j > 0) {
                    g.addEdge(map[i][j], map[i][j - 1]);
                }
                if (j == map[i].length - 1) {
                    g.addEdge(map[i][j], map[i][(j + 1) % map[i].length]);
                }

            }
        }
        return g;
    }

    private static int turnsToMine(int halite) {
        if (halite <= 0) {
            return 0;
        } else {
            int getsMined = (int) Math.ceil((float) halite / 4);
            System.out.println("Gets mined: " + getsMined);
            return 1 + turnsToMine(halite - getsMined);
        }
    }


}

class Cell {
    private int halite;
    private int x;
    private int y;

    public Cell(int halite, int x, int y) {
        this.halite = halite;
        this.x = x;
        this.y = y;
    }

    public int getHalite() {
        return halite;
    }

    public void setHalite(int halite) {
        this.halite = halite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
