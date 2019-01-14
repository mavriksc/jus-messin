package org.mavriksc.messin;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ParallelEval {

    public static void main(String[] args) {
        Tour[] tours = new Tour[5];
        Node[] path = new Node[5];
        path[0] = new Node(1, 1, 1);
        path[1] = new Node(2, 5, 6);
        path[2] = new Node(3, 1, 10);
        path[3] = new Node(4, 10, 1);
        path[4] = new Node(5, 4, 8);

        for (int i = 0; i < tours.length; i++) {
            tours[i] = new Tour(randPath(path));
        }
        getFittest(tours);

    }

    private static Tour getFittest(Tour[] tours) {
        List<Map.Entry<Tour, Double>> lengths = new ArrayList<>();
        Arrays.stream(tours).parallel()
                .forEach(t -> lengths.add(new AbstractMap.SimpleEntry<Tour, Double>(t, t.getLength())));
        System.out.println("TOUR:" + lengths.get(0).getKey() + " length:" + lengths.get(0).getValue());
        return Collections.min(lengths, Comparator.comparingDouble(Map.Entry::getValue)).getKey();
    }

    private static Node[] randPath(Node[] path) {
        List<Node> middleMan = new ArrayList<>();
        Collections.addAll(middleMan, path);
        Collections.shuffle(middleMan);
        Node[] result = new Node[middleMan.size()];
        for (int i = 0; i < middleMan.size(); i++) {
            result[i] = middleMan.get(i);
        }
        return result;
    }
}

class Tour {
    Node[] path;

    Tour(Node[] path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Tour{" + "path=" + Arrays.toString(path) + '}';
    }

    double getLength() {
        if (path.length < 2) {
            return 0;
        } else {
            double len = 0;
            for (int i = 0; i < path.length - 1; i++) {
                len = +path[i].getDistance(path[1 + 1]);
            }
            return len;
        }
    }

    ;

}

class Node {
    int id;
    int x;
    int y;

    Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    public double getDistance(Node that) {

        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
    }
}
