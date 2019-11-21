package org.mavriksc.poc.app;

import org.mavriksc.poc.error.EmptyDeckException;
import org.mavriksc.poc.model.Card;
import org.mavriksc.poc.model.Deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PocTest {
    private static int TEST_SIZE = 52 * 2000;

    public static void main(String[] args) {
        //testShuffle();
        Map<Set<Card>, String> startingHands = new HashMap<>();
        List<Card> cards = Card.getAllCards();
        for (int first = 0; first < cards.size() - 1; first++) {
            Card fc = cards.get(first);
            for (int second = first + 1; second < cards.size(); second++) {
                Card sc = cards.get(second);
                Set<Card> hand = new HashSet<>();
                hand.add(fc);
                hand.add(sc);
                startingHands.put(hand,fc.toString()+sc.toString());
            }
        }
        System.out.println("Count all starting hands: "+startingHands.size());
        //startingHands.forEach((k,v)-> System.out.println(v));
        Set<Card> hand = new HashSet<>();
        hand.add(new Card(Card.Rank.ACE,Card.Suit.SPADES));
        hand.add(new Card(Card.Rank.ACE,Card.Suit.CLUBS));

        System.out.println("ACES:"+startingHands.get(hand));
    }

    private static void testShuffle() {
        Map<Card, List<Integer>> counts = new HashMap<>(52);
        for (int i = 0; i < TEST_SIZE; i++) {
            Deck d = new Deck();
            d.shuffle();
            updateCounts(counts, d);
        }

        StringBuilder sb = new StringBuilder();
        counts.forEach((k, v) -> {
            v.forEach(count -> sb.append(count).append("\t"));
            sb.append("\n").append(k).append("\n");
        });

        System.out.println(sb.toString());
    }

    private static void updateCounts(Map<Card, List<Integer>> counts, Deck d) {
        int size = d.size();
        for (int i = 0; i < size; i++) {
            try {
                Card c = d.dealCard();
                List<Integer> positions = counts.computeIfAbsent(c, card -> {
                    List<Integer> g = new ArrayList<>(52);
                    for (int j = 0; j < 52; j++) {
                        g.add(0);
                    }
                    return g;
                });

                Integer count = positions.get(i);
                positions.set(i, ++count);
            } catch (EmptyDeckException e) {
                e.printStackTrace();
            }
        }

    }
}
