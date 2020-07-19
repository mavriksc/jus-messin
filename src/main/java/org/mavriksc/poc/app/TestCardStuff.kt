package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card

fun main() {
    val hand = listOf(Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS),
            Card(Card.Rank.FOUR, Card.Suit.DIAMONDS),
            Card(Card.Rank.THREE, Card.Suit.CLUBS),
            Card(Card.Rank.TWO, Card.Suit.DIAMONDS),
            Card(Card.Rank.ACE, Card.Suit.DIAMONDS),
            Card(Card.Rank.FIVE, Card.Suit.DIAMONDS),
            Card(Card.Rank.JACK, Card.Suit.DIAMONDS))

    val straight = checkFlush(hand)
    straight.map { print("$it ") }


}

