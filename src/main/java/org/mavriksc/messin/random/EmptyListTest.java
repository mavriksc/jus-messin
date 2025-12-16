package org.mavriksc.messin.random;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmptyListTest {
    public static void main(String[] args) {
        List<List<Integer>> list = makeBigList();
        Date startStream = new Date();
        toStream(list);
        Date stopStream = new Date();
        Date startList = new Date();
        onList(list);
        Date stopList = new Date();
        long streamDuration = stopStream.getTime() - startStream.getTime();
        System.out.println("Stream time: " + streamDuration);

        long listduration = stopList.getTime() - startList.getTime();
        System.out.println("Stream time: " + listduration);


    }

    static List<List<Integer>> makeBigList() {
        List<List<Integer>> r = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            List<Integer> l = new ArrayList<>();
            for (int j = 0; j < i % 10; j++) {
                l.add(i + j);
            }
            r.add(l);
        }
        return r;
    }


    static void toStream(List<List<Integer>> list) {
        list.forEach(l -> {
            try {
                int i = l.stream().findFirst().orElseThrow(() -> new IllegalStateException("No item selected"));
            } catch (IllegalStateException ignored) {

            }
        });

    }

    static void onList(List<List<Integer>> list) {
        list.forEach(l -> {
            int i;
            try {
                if (l.isEmpty()) throw new IllegalStateException("No item selected");
                else i = l.get(0);
            } catch (IllegalStateException ignored) {

            }
        });
    }
}
