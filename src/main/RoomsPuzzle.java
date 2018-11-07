/*
* @(#)RoomsPuzzle.java Mar 2, 2018
*© 2017 HYLA, Inc. All Rights Reserved.
*This software is the confidential and proprietary information of HYLA, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the *terms of the license agreement you entered into with HYLA, Inc.
*/
package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author SCarlisle
 *
 */
public class RoomsPuzzle {

    public static final int SIZE = 10000;
    private static List<String> rooms = new ArrayList<>();
    public static final String TREASURE = "T";
    public static final String EMPTY = "e";
    private static int currPos = 0;
    private static int moves = 0;

    /**
     * @param args
     */
    public static void main(String[] args) {
        do {
            init();
            int foundAt = findTreasure();
            System.out.println("After " + moves + ", moves found treasure at: " + foundAt);
        } while (true);
    }

    private static int findTreasure() {
        for (int i = 1; i < rooms.size() - 1; i++) {

            if (rooms.get(i).equals(TREASURE)) {
                return i;
            } else {
                doMove();
            }
        }
        for (int i = rooms.size() - 2; i > 0; i--) {
            if (rooms.get(i).equals(TREASURE)) {
                return i;
            } else {
                doMove();
            }
        }
        throw new RuntimeException("didn't find the treasure");

    }

    private static void init() {
        rooms.clear();
        moves = 0;
        currPos = ThreadLocalRandom.current().nextInt(SIZE);
        for (int i = 0; i < SIZE; i++) {
            rooms.add(i == currPos ? TREASURE : EMPTY);
        }
        //System.out.println("Initial treasure position: " + currPos);
    }

    private static void doMove() {
        moves++;
        rooms.set(currPos, EMPTY);
        currPos += getMove();
        rooms.set(currPos, TREASURE);
    }

    private static int getMove() {
        if (currPos == 0) {
            return 1;
        } else if (currPos == SIZE - 1) {
            return -1;
        } else {
            return ThreadLocalRandom.current().nextBoolean() ? -1 : 1;
        }
    }

}
