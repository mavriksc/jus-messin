package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card

//5-7 hand eval
// TODO change 'checks' to return the selected cards
//   cant call flush and then straight because will return highest flush cards and not eval straight unless just that
//   is avail with board
// TODO general evaluator take game config... game config
//   map hand type -> Pair<Function<List<Card>->List<Card>, Function<List<Card>->Int>>


fun main() {
    print(eval(emptyList()))
}

fun eval(cards: List<Card>): Int {
    if (cards.size < 5 || cards.size > 7) return 0
    var hand = checkStraightFlush(cards)
    if (hand.isNotEmpty())
        return rankStraightFlush(hand)
    hand = check4OfAKind(cards)
    if (hand.isNotEmpty())
        return rank4OfAKind(hand)
    hand = checkFullHouse(cards)
    if (hand.isNotEmpty())
        return rankFullHouse(hand)
    hand = checkFlush(cards)
    if (hand.isNotEmpty())
        return rankFlush(hand)
    hand = checkStraight(cards)
    if (hand.isNotEmpty())
        return rankStraight(hand)
    hand = check3OfAKind(cards)
    if (hand.isNotEmpty())
        return rank3OfAKind(hand)
    hand = check2Pair(cards)
    if (hand.isNotEmpty())
        return rank2Pair(hand)
    hand = check1Pair(cards)
    if (hand.isNotEmpty())
        return rank1Pair(hand)
    return highcard(cards)
}

fun highcard(hand: List<Card>): Int {
    return 0
}

fun rank1Pair(hand: List<Card>): Int {
    return 0
}

fun check1Pair(cards: List<Card>): List<Card> {
    return emptyList()
}

fun rank2Pair(hand: List<Card>): Int {
    return 0
}

fun check2Pair(cards: List<Card>): List<Card> {
    return emptyList()
}

fun rank3OfAKind(hand: List<Card>): Int {
    return 0
}

fun check3OfAKind(cards: List<Card>): List<Card> {
    return emptyList()
}

fun rankStraight(hand: List<Card>): Int {
    return 0
}

fun checkStraight(cards: List<Card>): List<Card> {
    val handRanks = cards.map { it.rank() }.toSet()
    (Card.Rank.ACE.ordinal downTo Card.Rank.FIVE.ordinal).forEach {
        val itHighStraight = (it downTo it - 4).map { r -> r.toRank() }
        if (handRanks.containsAll(itHighStraight)) {
            val rank = Card.Rank.values()[it]
            println("$rank high straight")
            return itHighStraight.map { cards.find { c -> c.rank() == it }!! }
        }
    }
    return emptyList()
}

fun rankFlush(hand: List<Card>): Int {
    return 0
}

fun checkFlush(cards: List<Card>): List<Card> {
    val isFlush  = cards.groupBy { it.suit() }.filter { it.value.size>=5 }
    return if (isFlush.isNotEmpty()) {
         isFlush[isFlush.keys.first()]?.sortedByDescending { it.rank() }?.filterIndexed { index, _ -> index < 5 }!!
    } else
     emptyList()
}

fun rankFullHouse(hand: List<Card>): Int {
    return 0
}

fun checkFullHouse(cards: List<Card>): List<Card> {
    return emptyList()
}

fun rank4OfAKind(hand: List<Card>): Int {
    return 0
}

fun rankStraightFlush(hand: List<Card>): Int {
    return 0
}

fun check4OfAKind(cards: List<Card>): List<Card> {
    return emptyList()
}

fun checkStraightFlush(cards: List<Card>): List<Card> {

    return emptyList()
}

fun Int.toRank(): Card.Rank {
    // convert -1..12 -> Rank
    return when {
        this < 0 -> Card.Rank.ACE
        else -> Card.Rank.values()[this]
    }
}