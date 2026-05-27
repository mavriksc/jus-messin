package org.mavriksc.messin.hackerrank

import java.util.ArrayDeque

class AhoCorasickHealthFSM(private val dict: Array<String>, maxS: Int = dict.sumBy { it.length } + 1) {
    private val maxC = 26
    private val nodes = ArrayList<Node>(maxS)
    private var cachedScores: Array<Int>? = null
    private var cachedPrefixHealth = emptyArray<LongArray>()

    private class Node {
        val next = IntArray(26) { -1 }
        var failure = 0
        var outputLink = -1
        val terminalIndices = mutableListOf<Int>()
    }

    init {
        nodes.add(Node())
        buildTrie()
        buildFailureLinks()
    }

    private fun buildTrie() {
        dict.forEachIndexed { index, word ->
            var currentState = 0
            for (element in word) {
                val ch = charIndex(element)
                if (nodes[currentState].next[ch] == -1) {
                    nodes[currentState].next[ch] = nodes.size
                    nodes.add(Node())
                }
                currentState = nodes[currentState].next[ch]
            }
            nodes[currentState].terminalIndices.add(index)
        }
    }

    private fun buildFailureLinks() {
        val queue = ArrayDeque<Int>()

        for (ch in 0 until maxC) {
            val state = nodes[0].next[ch]
            if (state == -1) {
                nodes[0].next[ch] = 0
            } else {
                nodes[state].failure = 0
                queue.add(state)
            }
        }

        while (!queue.isEmpty()) {
            val state = queue.remove()
            val failure = nodes[state].failure
            nodes[state].outputLink = if (nodes[failure].terminalIndices.isNotEmpty()) failure else nodes[failure].outputLink

            for (ch in 0 until maxC) {
                val nextState = nodes[state].next[ch]
                if (nextState == -1) {
                    nodes[state].next[ch] = nodes[failure].next[ch]
                } else {
                    nodes[nextState].failure = nodes[failure].next[ch]
                    queue.add(nextState)
                }
            }
        }
    }

    private fun findNextState(currentState: Int, nextInput: Char): Int {
        return nodes[currentState].next[charIndex(nextInput)]
    }

    fun findDictWordsInText(text: String) {
        var currentState = 0
        for (i in text.indices) {
            currentState = findNextState(currentState, text[i])
            printMatchesEndingAt(currentState, i)
        }
    }

    private fun printMatchesEndingAt(state: Int, endIndex: Int) {
        var matchState = state
        while (matchState != -1) {
            nodes[matchState].terminalIndices.forEach { geneIndex ->
                println("Word ${dict[geneIndex]} appears from ${endIndex - dict[geneIndex].length + 1} to $endIndex")
            }
            matchState = nodes[matchState].outputLink
        }
    }

    fun scoreStrand(strand: String, strandInfo: StrandInfo, scores: Array<Int>): Long {
        prepareHealthPrefixSums(scores)

        var score = 0L
        var currentState = 0
        for (element in strand) {
            currentState = findNextState(currentState, element)
            score += scoreMatchesAt(currentState, strandInfo)
        }
        return score
    }

    private fun scoreMatchesAt(state: Int, strandInfo: StrandInfo): Long {
        var score = 0L
        var matchState = state
        while (matchState != -1) {
            val geneIndices = nodes[matchState].terminalIndices
            if (geneIndices.isNotEmpty()) {
                val prefixHealth = cachedPrefixHealth[matchState]
                val first = geneIndices.lowerBound(strandInfo.first)
                val lastExclusive = geneIndices.upperBound(strandInfo.last)
                score += prefixHealth[lastExclusive] - prefixHealth[first]
            }
            matchState = nodes[matchState].outputLink
        }
        return score
    }

    private fun prepareHealthPrefixSums(scores: Array<Int>) {
        if (cachedScores === scores) return

        cachedPrefixHealth = Array(nodes.size) { nodeIndex ->
            val geneIndices = nodes[nodeIndex].terminalIndices
            LongArray(geneIndices.size + 1).also { prefix ->
                for (i in geneIndices.indices) {
                    prefix[i + 1] = prefix[i] + scores[geneIndices[i]].toLong()
                }
            }
        }
        cachedScores = scores
    }

    private fun charIndex(ch: Char): Int {
        val index = ch - 'a'
        require(index in 0 until maxC) { "AhoCorasickHealthFSM only supports lowercase a-z input: '$ch'" }
        return index
    }

    private fun List<Int>.lowerBound(target: Int): Int {
        var low = 0
        var high = size
        while (low < high) {
            val mid = (low + high) ushr 1
            if (this[mid] < target) low = mid + 1 else high = mid
        }
        return low
    }

    private fun List<Int>.upperBound(target: Int): Int {
        var low = 0
        var high = size
        while (low < high) {
            val mid = (low + high) ushr 1
            if (this[mid] <= target) low = mid + 1 else high = mid
        }
        return low
    }
}
