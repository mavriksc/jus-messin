package org.mavriksc.poc.model;

import org.mavriksc.poc.error.EmptyDeckException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Deck {
    private static int SHUFFLES = 1;
    private List<Card> cards;
    public Deck(){
        cards = new ArrayList<>(52);

        for (Card.Suit suit: Card.Suit.values())
            for (Card.Rank rank: Card.Rank.values())
                cards.add(new Card(rank, suit));
    }

    public void shuffle(){
        List<Card> temp = new ArrayList<>(52);
        for (int i = 0; i < SHUFFLES; i++) {
            do {
                temp.add(cards.remove(ThreadLocalRandom.current().nextInt(cards.size())));
            } while (cards.size()>0);
            cards.addAll(temp);
            temp.clear();
        }
    }

    public int size(){
        return cards.size();
    }

    public Card dealCard() throws EmptyDeckException{
        if (cards.size()>0)
            return cards.remove(0);
        else
            throw new EmptyDeckException();
    }

    @Override
    public String toString() {
        StringBuilder sb  = new StringBuilder();
        cards.forEach(c->sb.append(c.toString()));
        return sb.toString();
    }
}
