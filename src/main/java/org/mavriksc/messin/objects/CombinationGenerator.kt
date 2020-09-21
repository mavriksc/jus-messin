package org.mavriksc.the_sim_p_s_son.engine

class CombinationGenerator<T>(private val items: List<T>, choose: Int = 1) : Iterator<List<T>>, Iterable<List<T>> {
    private val indices = Array(choose) { it }
    private var first = true
    private var count = (items.size downTo items.size - choose + 1).map { it.toLong() }.fold(1L) { acc, it -> acc * it } /
            (choose downTo 1L).fold(1L) { acc, it -> acc * it }

    init {
        if (items.isEmpty() || choose > items.size || choose < 1)
            error("list must have at least 'choose' items and 'choose' min is 1")
        println(count)
    }

    override fun hasNext() = count > 0

    override fun next(): List<T> {
        if (!hasNext()) error("No combinations left please call has next before next :)")
        if (!first) {
            incrementAndCarry()
        } else
            first = false
        count--
        return List(indices.size) { items[indices[it]] }
    }

    override fun iterator(): Iterator<List<T>> = this

    private fun incrementAndCarry() {
        var carry: Boolean
        var place = indices.lastIndex
        do {
            carry = if ((place == indices.lastIndex && indices[place] < items.lastIndex)
                    || (place != indices.lastIndex && indices[place] < indices[place + 1] - 1)) {
                indices[place]++
                (place + 1..indices.lastIndex).forEachIndexed { index, i ->
                    indices[i] = indices[place] + index + 1
                }
                false
            } else
                true
            place--
        } while (carry && place > -1)
    }


}

fun main() {
    val combGen = CombinationGenerator(listOf(1, 2, 3, 4, 5, 6, 7), 7)
    combGen.map { println(it.joinToString()) }

}
