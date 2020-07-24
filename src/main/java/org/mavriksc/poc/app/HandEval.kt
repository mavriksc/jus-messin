package org.mavriksc.poc.app

import org.mavriksc.poc.model.Card

//5+ hand eval

//  general evaluator take game config... game config
//   map hand type -> Pair<Function<List<Card>->List<Card>, Function<List<Card>->Int>>


fun main() {
    print(compareHands(emptyList(), emptyList()))
}


fun compareHands(hand1: List<Card>, hand2: List<Card>): Int {
    val h1Ranked = getHandAndHandRank(hand1)
    val h2Ranked = getHandAndHandRank(hand2)
    return if (h1Ranked.second == h2Ranked.second)
        compareSameHandRank(h1Ranked.first,h2Ranked.first)
    else
        h2Ranked.second.compareTo(h1Ranked.second)
}

fun compareSameHandRank(hand1: List<Card>, hand2: List<Card>): Int {
    (hand1.indices).forEach {
        if (hand1[it].rank() != hand2[it].rank()){
            return hand1[it].rank().compareTo(hand2[it].rank())
        }
    }
    return 0
}

fun getHandAndHandRank(cards: List<Card>): Pair<List<Card>, HandRank> {

    var hand = checkStraightFlush(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.STRAIGHT_FLUSH)
    hand = check4OfAKind(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.FOUR_OF_A_KIND)
    hand = checkFullHouse(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.FULL_HOUSE)
    hand = checkFlush(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.FLUSH)
    hand = checkStraight(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.STRAIGHT)
    hand = check3OfAKind(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.THREE_OF_A_KIND)
    hand = check2Pair(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.TWO_PAIR)
    hand = check1Pair(cards)
    if (hand.isNotEmpty())
        return Pair(hand, HandRank.ONE_PAIR)
    hand = getHighCardHand(cards)
    return Pair(hand, HandRank.HIGH_CARD)
}


enum class HandRank {
    STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
}

fun getHighCardHand(cards: List<Card>): List<Card> {
    return cards.sortedByDescending { it.rank() }.take(5)
}

fun check1Pair(cards: List<Card>): List<Card> {
    val rankMap = cards.groupBy { it.rank() }
    val keys = rankMap.keys.sortedWith(compareBy({ rankMap[it]!!.size }, { it })).reversed()
    return if (rankMap[keys[0]]!!.size == 2)
        rankMap[keys[0]]!!
                .union(cards.filterNot { it.rank() == keys[0] || it.rank() == keys[1] }
                        .sortedByDescending { it.rank() }
                        .take(3))
                .toList()
    else emptyList()
}

fun check2Pair(cards: List<Card>): List<Card> {
    val rankMap = cards.groupBy { it.rank() }
    val keys = rankMap.keys.sortedWith(compareBy({ rankMap[it]!!.size }, { it })).reversed()
    return if (rankMap[keys[0]]!!.size == 2 && rankMap[keys[1]]?.size!! == 2)
        rankMap[keys[0]]!!
                .union(rankMap[keys[1]]!!.take(2))
                .union(cards.filterNot { it.rank() == keys[0] || it.rank() == keys[1] }
                        .sortedByDescending { it.rank() }
                        .take(1))
                .toList()
    else emptyList()

}

fun check3OfAKind(cards: List<Card>): List<Card> {
    val rankMap = cards.groupBy { it.rank() }
    val trips = rankMap.filter { it.value.size == 3 }
    return if (trips.isNotEmpty())
        rankMap[trips.keys.first()]
                ?.union(cards.filterNot { it.rank() == trips.keys.first() }
                        .sortedByDescending { it.rank() }.take(2))?.toList()!!
    else emptyList()
}

fun checkStraight(cards: List<Card>): List<Card> {
    val handRanks = cards.map { it.rank() }.toSet()
    (Card.Rank.ACE.ordinal downTo Card.Rank.FIVE.ordinal).forEach {
        val itHighStraight = (it downTo it - 4).map { r -> r.toRank() }
        if (handRanks.containsAll(itHighStraight)) {
            return itHighStraight.map { cards.find { c -> c.rank() == it }!! }
        }
    }
    return emptyList()
}

fun checkFlush(cards: List<Card>): List<Card> {
    val isFlush = cards.groupBy { it.suit() }.filter { it.value.size >= 5 }
    return if (isFlush.isNotEmpty()) {
        isFlush[isFlush.keys.first()]?.sortedByDescending { it.rank() }?.filterIndexed { index, _ -> index < 5 }!!
    } else
        emptyList()
}

fun checkFullHouse(cards: List<Card>): List<Card> {
    val rankMap = cards.groupBy { it.rank() }
    val keys = rankMap.keys.sortedWith(compareBy({ rankMap[it]!!.size }, { it })).reversed()
    return if (rankMap[keys[0]]!!.size == 3 && rankMap[keys[1]]?.size!! >= 2)
        rankMap[keys[0]]!!.union(rankMap[keys[1]]!!.take(2)).toList()
    else emptyList()
}

fun check4OfAKind(cards: List<Card>): List<Card> {
    val rankMap = cards.groupBy { it.rank() }
    val quads = rankMap.filter { it.value.size == 4 }
    if (quads.isNotEmpty()) {
        val kicker = rankMap[rankMap.keys.sorted().filterNot { it == quads.keys.first() }.last()]?.first()!!
        return quads[quads.keys.first()]!!.union(listOf(kicker)).toList()
    }
    return emptyList()
}

fun checkStraightFlush(cards: List<Card>): List<Card> {
    val flushCards = cards.groupBy { it.suit() }.filter { it.value.size >= 5 }
    return if (flushCards.isNotEmpty())
        flushCards[flushCards.keys.first()]?.let { checkStraight(it) }!!
    else emptyList()
}

fun Int.toRank(): Card.Rank {
    // convert -1..12 -> Rank
    return when {
        this < 0 -> Card.Rank.ACE
        else -> Card.Rank.values()[this]
    }
}