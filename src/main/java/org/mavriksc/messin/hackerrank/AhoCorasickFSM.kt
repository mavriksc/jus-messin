package org.mavriksc.messin.hackerrank

import java.util.*

class AhoCorasickFSM(private val dict: Array<String>, maxS:Int) {
    val k = dict.size
    private val maxC = 26
    val out = Array(maxS) { 0 }
    private val f = Array(maxS) { -1 }
    private val g = Array(maxS) { Array(maxC) { -1 } }

    init {
        buildFSM(dict, k)
    }

    private fun buildFSM(arr: Array<String>, k: Int): Int {
        var states = 1
        for (i in 0 until k) {
            val word = arr[i]
            var currentState = 0
            for (element in word) {
                val ch = element - 'a'
                if (g[currentState][ch] == -1)
                    g[currentState][ch] = states++
                currentState = g[currentState][ch]
            }
            out[currentState] = out[currentState] or (1.shl(i))
        }
        for (ch in 0 until maxC)
            if (g[0][ch] == -1)
                g[0][ch] = 0
        val q: Queue<Int> = LinkedList<Int>()
        for (ch in 0 until maxC) {
            if (g[0][ch] != 0) {
                f[g[0][ch]] = 0
                q.offer(g[0][ch])
            }
        }
        while (q.size != 0) {
            val state = q.remove()
            for (ch in 0 until maxC) {
                if (g[state][ch] != -1) {
                    var failure = f[state]
                    while (g[failure][ch] == -1)
                        failure = f[failure]
                    failure = g[failure][ch]
                    f[g[state][ch]] = failure
                    out[g[state][ch]] = out[g[state][ch]] or out[failure]
                    q.offer(g[state][ch])
                }
            }
        }
        return states
    }

    private fun findNextState(currentState: Int, nextInput: Char): Int {
        var answer = currentState
        val ch = nextInput - 'a'
        while (g[answer][ch] == -1)
            answer = f[answer]
        return g[answer][ch]
    }

    fun findDictWordsInText(text: String) {
        var currentState = 0
        for (i in text.indices) {
            currentState = findNextState(currentState, text[i])
            if (out[currentState] == 0)
                continue

            for (j in 0 until k) {
                if (out[currentState] and 1.shl(j) != 0) {
                    println("Word ${dict[j]} appears from ${i - dict[j].length + 1} to $i")
                }
            }
        }
    }

    fun scoreStrand(strand: String, strandInfo: StrandInfo, scores: Array<Int>): Long {
        var score = 0L
        var currentState = 0
        for (i in strand.indices) {
            currentState = findNextState(currentState, strand[i])
            if (out[currentState] == 0)
                continue

            for (j in 0 until k) {
                if (out[currentState] and 1.shl(j) > 0 && (j >= strandInfo.first && j <= strandInfo.last))
                    score += scores[j].toLong()
            }
        }
        return score
    }

}
