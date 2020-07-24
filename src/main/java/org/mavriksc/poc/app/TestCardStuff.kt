package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.Comparator
import kotlin.collections.LinkedHashSet

// maybe create comparator that increments value so we can see how many times it's called on the insert versus the sort

const val allHandsFileName = "allHandsSorted.dat"

fun main() {
    //testHandSorting()
    //genAllHands()
    runAllHands()
    //testTreeStuff()
    //loadAndPrint()
    //makeASmallListAndSave()


}

fun makeASmallListAndSave() {
    val allhands = load(allHandsFileName)
    val sublist = allhands.filterIndexed { index, _ -> index % 100_000 == 0 }
    save(sublist, "sublist.dat")
    val shuffled = sublist.shuffled()
    save(shuffled,"shuffled.dat")

}

fun loadAndPrint() {
    val hands = load(allHandsFileName)
    outputFirstAndLastHands(hands)
}

fun testTreeStuff() {
    //possibly slower than generating the list and then sorting it.
    // lower proc load but may pick up as list gets big.
    val hands = TreeSet<Long>(handComparatorLong.then(Comparator.naturalOrder()))
    println("Generate all 7 card hands")
    println(Date())
    genSevenCardLongs(hands)
    println("Done Generated ${hands.size} hands")
    println(Date())
    //return hands
    outputFirstAndLastHands(hands)
    save(hands, allHandsFileName)
}

fun outputHand(hand:Long){
    val ranked = getHandAndHandRank(hand.toCardList())
    println("${hand.toCardList().realToString()}, Rank: ${ranked.second} , Cards : ${ranked.first.realToString()}")
}
private fun outputFirstAndLastHands(hands: Collection<Long>) {
    val highHand = getHandAndHandRank(hands.first().toCardList())
    val lowHand = getHandAndHandRank(hands.last().toCardList())
    println("First Hand: ${hands.first().toCardList().realToString()}, Rank: ${highHand.second} , Cards : ${highHand.first.realToString()}")
    println("Last Hand: ${hands.last().toCardList().realToString()}, Rank: ${lowHand.second} , Cards : ${lowHand.first.realToString()}")
}

private fun genSevenCardLongs(hands: MutableCollection<Long>) {
    val deckSize = 52
    (0..deckSize - 7).forEach { first ->
        (first + 1..deckSize - 6).forEach { second ->
            (second + 1..deckSize - 5).forEach { third ->
                (third + 1..deckSize - 4).forEach { fourth ->
                    (fourth + 1..deckSize - 3).forEach { fif ->
                        (fif + 1..deckSize - 2).forEach { sixth ->
                            (sixth + 1 until deckSize).forEach { seventh ->
                                val n = 1L.shl(first) + 1L.shl(second) + 1L.shl(third) + 1L.shl(fourth) + 1L.shl(fif) + 1L.shl(sixth) + 1L.shl(seventh)
                                hands.add(n)
                            }
                        }
                    }
                }
            }
        }
        println("1/${deckSize - 7} way done generating")
    }
}

fun runAllHands() {
    val hands = genHandsAsLongs()
    println("Sorting")
    hands.sortWith(handComparatorLong)
    println("Done sorting")
    println(Date())
    outputFirstAndLastHands(hands)
    save(hands, allHandsFileName)
}

fun save(hands: Collection<Long>, file: String) {
    ObjectOutputStream(FileOutputStream(file)).use { it -> it.writeObject(hands) }
}

fun load(file: String): List<Long> {
    println("reading File")
    println(Date())
    ObjectInputStream(FileInputStream(file)).use { it ->
        val hands = it.readObject()
        println("Done reading")
        println(Date())
        return hands as List<Long>
    }
}

fun genStartingHands(): List<Long> {

    val deckSize = 52
    val hands = mutableListOf<Long>()
    (0..deckSize - 2).forEach { first ->
        (first + 1 until deckSize).forEach { second ->
            val n = 1L.shl(first) + 1L.shl(second)
            hands.add(n)
        }
        println("1/${deckSize - 2} way done generating")
    }
    println("Done Generated ${hands.size} hands")
    println(Date())
    return hands
}

fun genHandsAsLongs(): MutableList<Long> {

    println("Generate all 7 card hands")
    println(Date())
    val hands = mutableListOf<Long>()
    genSevenCardLongs(hands)

    println("Done Generated ${hands.size} hands")
    println(Date())
    return hands
}

fun Long.toCardList(): List<Card> {
    var startValue = this
    val cards = mutableListOf<Card>()
    (0..52).forEach {
        if (startValue == 0L)
            return cards
        else
            if (startValue.and(1L) == 1L)
                cards.add(it.toCard())
        startValue = startValue.shr(1)
    }
    return cards
}

fun Int.toCard(): Card {
    return Card(Card.Rank.values()[this % 13], Card.Suit.values()[this / 13])
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

val handComparatorLong: Comparator<Long> = Comparator { h1, h2 -> compareHands(h1.toCardList(), h2.toCardList()) }
val handComparator: Comparator<List<Card>> = Comparator { h1, h2 -> compareHands(h1, h2) }
