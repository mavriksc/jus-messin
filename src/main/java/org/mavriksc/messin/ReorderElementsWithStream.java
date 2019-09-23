package org.mavriksc.messin;

import org.mavriksc.messin.objects.ThingObject;
import org.mavriksc.messin.objects.ThingObjectInt;

import java.util.ArrayList;
import java.util.List;

public class ReorderElementsWithStream {

    public static void main(String[] args) {
        List<ThingObjectInt> things = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            things.add(new ThingObjectInt(i));
        }
        System.out.println(things);
        things.add(0,things.remove(things.indexOf(things.stream().filter(t->t.getNum()%2==0).findFirst().orElse(things.get(0)))));
        System.out.println(things);
    }
}
