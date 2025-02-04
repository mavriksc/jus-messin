package org.mavriksc.messin.bs;

public class BinSearch {
    private Node root;

    public BinSearch(Node root) {
        this.root = root;
    }

    public void insert(Node newNode) {
        Node putHere = traverse(root, newNode.getValue());
        if (newNode.getValue() < putHere.getValue()) {
            putHere.setLeft(newNode);
        } else
            putHere.setRight(newNode);

    }

    private Node traverse(Node currentNode, int value) {
        if (currentNode == null) {
            return null;
        }
        if (value >= currentNode.getValue())
            if (currentNode.getRight() == null) {
                return currentNode;
            } else
                return traverse(currentNode.getRight(), value);
        if (value < currentNode.getValue()) {
            if (currentNode.getLeft() == null) {
                return currentNode;
            } else
                return traverse(currentNode.getLeft(), value);
        }
        return null;
    }

    public Node search(Node currentNode, int value) {
        if (currentNode == null) {
            return null;
        }
        if (value == currentNode.getValue()) {
            return currentNode;
        }
        if (value < currentNode.getValue()) {
            return search(currentNode.getLeft(), value);
        } else return search(currentNode.getRight(), value);
    }

    public static void main(String[] args) {
        BinSearch bs = new BinSearch(new Node(56));
        bs.insert(new Node(23));
        bs.insert(new Node(38));
        bs.insert(new Node(14));
        bs.insert(new Node(59));
        bs.insert(new Node(36));
        bs.insert(new Node(7));

        Node found = bs.search(bs.root, 2);
        if (found != null) {
            System.out.println("found value: " + found.getValue() + "in object" + found);
        } else {
            System.out.println(("not found"));
        }

    }
}
