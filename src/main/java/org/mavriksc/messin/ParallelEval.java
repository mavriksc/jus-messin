package org.mavriksc.messin;

public class ParallelEval {

    public static void main(String[] args) {
        Node[] locs = new Node[10];
        locs[0] = new Node(1, 1, 1);

    }
}

class Node {
    int id;
    int x;
    int y;

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double getDistance(Node that) {

        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
    }
}
