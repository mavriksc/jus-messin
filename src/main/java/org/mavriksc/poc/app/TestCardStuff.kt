package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card
import java.util.*
import kotlin.Comparator
import kotlin.collections.LinkedHashSet

fun main() {

    //testHandSorting()
    genAllHands()

}

fun genAllHands() {
    // runs out of memory lols
    val allCards = Card.getAllCards()
    println("Generate all 7 card hands")
    println(Date())
    val allHands: MutableList<List<Card>> = mutableListOf()
    (0..allCards.size - 7).forEach { first ->
        (first + 1..allCards.size - 6).forEach { second ->
            (second + 1..allCards.size - 5).forEach { third ->
                (third + 1..allCards.size - 4).forEach { fourth ->
                    (fourth + 1..allCards.size - 3).forEach { fif ->
                        (fif + 1..allCards.size - 2).forEach { sixth ->
                            (sixth until allCards.size).forEach { seventh ->
                                allHands.add(listOf(allCards[first], allCards[second], allCards[third], allCards[fourth], allCards[fif], allCards[sixth], allCards[seventh]))
                                if ((first + 1) % 13 == 0)
                                    println("1/4 way done generating")
                            }
                        }
                    }
                }
            }
        }
    }

    println("Done Generated ${allHands.size} hands")
    println(Date())
    println("Sorting")
    allHands.sortWith(handComparator)
    println("Done sorting")
    println(Date())
    val highHand = getHandAndHandRank(allHands.first())
    val lowHand = getHandAndHandRank(allHands.last())
    println("Found Hand: ${allHands.first().realToString()}, Rank: ${highHand.second} , Cards : Rank: ${highHand.first.realToString()}")
    println("Found Hand: ${allHands.last().realToString()}, Rank: ${lowHand.second} , Cards : Rank: ${lowHand.first.realToString()}")

}


private fun testHandSorting() {
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

fun List<Card>.realToString() = this.joinToString(" ")

private operator fun <E> LinkedHashSet<E>.get(i: Int): E = this.filterIndexed { index, _ -> index == i }.first()


val handComparator: Comparator<List<Card>> = Comparator { h1, h2 -> compareHands(h1, h2) }
