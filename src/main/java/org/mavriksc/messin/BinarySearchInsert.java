package org.mavriksc.messin;

import org.mavriksc.messin.objects.ThingObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinarySearchInsert {
    public static void main(String... args) {
        Comparator<ThingObject> c = (t1, t2) -> t1.getData().compareTo(t2.getData());
        List<ThingObject> l = new ArrayList<>();
        add(l, new ThingObject("apple"), c);
        add(l, new ThingObject("zapple"), c);
        add(l, new ThingObject("dapple"), c);
        add(l, new ThingObject("apple"), c);
        add(l, new ThingObject("abple"), c);
        add(l, new ThingObject("capple"), c);
        add(l, new ThingObject("fapple"), c);
        add(l, new ThingObject("tapple"), c);
        add(l, new ThingObject("zzapple"), c);
        l.forEach(t -> System.out.println(t.getData()));
    }

    private static void add(List<ThingObject> l, ThingObject t, Comparator<ThingObject> c) {
        if (l != null) {
            if (l.size() == 0) {
                l.add(t);
                System.out.println("Insert: 0\tSize: " + l.size());
            } else {
                int index = bSearch(l, t, c);
                l.add(index, t);
                System.out.println("Insert: " + index + "\tSize: " + l.size());
            }
        }
    }

    private static int bSearch(List<ThingObject> l, ThingObject t, Comparator<ThingObject> c) {
        boolean notFound = true;
        int high = l.size() - 1;
        int low = 0;
        int look = (low + high) / 2;
        int looks=0;
        while (notFound) {
            looks++;
            if (c.compare(l.get(look), t) > 0) {
                // it's to the left of look
                if (look == 0 || c.compare(l.get(look - 1), t) < 0) {
                    //is it adjacent?
                    notFound = false;
                } else {
                    //look again.
                    high = look - 1;
                    look = (low + high) / 2;
                }
            } else if (c.compare(l.get(look), t) < 0) {
                // it's to the right of look
                if (look == l.size() - 1 || c.compare(l.get(look + 1), t) > 0) {
                    //is it adjacent?
                    look = look + 1;
                    notFound = false;
                } else {
                    //look again.
                    low = look+1;
                    look = (low + high) / 2;
                }
            } else {
                notFound = false;
            }
        }
        System.out.println(looks);
        return look;
    }

}
