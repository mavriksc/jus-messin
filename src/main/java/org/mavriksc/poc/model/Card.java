package org.mavriksc.poc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Card {

    public static enum Rank {
        ACE('A'), TWO('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'),
        EIGHT('8'), NINE('9'), TEN('T'), JACK('J'), QUEEN('Q'), KING('K');

        private final char val;

        Rank(char val) {
            this.val = val;
        }

        public String getVal() {
            return "" + val;
        }
    }

    public static enum Suit {
        DIAMONDS('♦'), CLUBS('♣'), HEARTS('♥'), SPADES('♠');

        private final char val;

        Suit(char val) {
            this.val = val;
        }

        public String getVal() {
            return "" + val;
        }
    }

    private final Rank rank;
    private final Suit suit;

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.getVal() + suit.getVal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Card card = (Card) o;

        if (rank != card.rank)
            return false;
        return suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = rank.hashCode();
        result = 31 * result + suit.hashCode();
        return result;
    }
}