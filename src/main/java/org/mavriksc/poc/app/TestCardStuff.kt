package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card

fun main() {
    val hand = listOf(Card(Card.Rank.SIX, Card.Suit.DIAMONDS),
            Card(Card.Rank.SIX, Card.Suit.CLUBS),
            Card(Card.Rank.SIX, Card.Suit.HEARTS),
            Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS),
            Card(Card.Rank.ACE, Card.Suit.DIAMONDS),
            Card(Card.Rank.EIGHT, Card.Suit.HEARTS),
            Card(Card.Rank.EIGHT, Card.Suit.CLUBS))

    val fours = check3OfAKind(hand)
    fours.map { print("$it ") }


}

