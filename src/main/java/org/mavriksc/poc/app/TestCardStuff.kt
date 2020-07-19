package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card

fun main() {

    val sfHigh = listOf(Card(Card.Rank.ACE, Card.Suit.DIAMONDS),
            Card(Card.Rank.KING, Card.Suit.DIAMONDS),
            Card(Card.Rank.QUEEN, Card.Suit.DIAMONDS),
            Card(Card.Rank.JACK, Card.Suit.DIAMONDS),
            Card(Card.Rank.TEN, Card.Suit.DIAMONDS),
            Card(Card.Rank.EIGHT, Card.Suit.HEARTS),
            Card(Card.Rank.EIGHT, Card.Suit.CLUBS))
    val sfLow = listOf(Card(Card.Rank.SIX, Card.Suit.HEARTS),
            Card(Card.Rank.SEVEN, Card.Suit.HEARTS),
            Card(Card.Rank.FIVE, Card.Suit.HEARTS),
            Card(Card.Rank.EIGHT, Card.Suit.HEARTS),
            Card(Card.Rank.NINE, Card.Suit.HEARTS),
            Card(Card.Rank.TWO, Card.Suit.HEARTS),
            Card(Card.Rank.FIVE, Card.Suit.CLUBS))
    val qHigh = listOf(Card(Card.Rank.TEN, Card.Suit.DIAMONDS),
            Card(Card.Rank.TEN, Card.Suit.CLUBS),
            Card(Card.Rank.TEN, Card.Suit.HEARTS),
            Card(Card.Rank.TEN, Card.Suit.SPADES),
            Card(Card.Rank.ACE, Card.Suit.DIAMONDS),
            Card(Card.Rank.EIGHT, Card.Suit.HEARTS),
            Card(Card.Rank.EIGHT, Card.Suit.CLUBS))
    val qLow = listOf(Card(Card.Rank.SIX, Card.Suit.DIAMONDS),
            Card(Card.Rank.SIX, Card.Suit.CLUBS),
            Card(Card.Rank.SIX, Card.Suit.HEARTS),
            Card(Card.Rank.SIX, Card.Suit.SPADES),
            Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS),
            Card(Card.Rank.TWO, Card.Suit.HEARTS),
            Card(Card.Rank.FIVE, Card.Suit.CLUBS))
    val handList = listOf(qHigh, sfHigh, sfLow, qLow)
    val sortedlist = handList.sortedWith(handComparator)
    sortedlist.forEach { hand ->
            val hr = getHandAndHandRank(hand)
            hr.first.forEach { print(it) }
            println()
            println(getHandAndHandRank(hand).second)
    }

}

val handComparator: Comparator<List<Card>> = Comparator { h1, h2 -> compareHands(h1, h2) }
