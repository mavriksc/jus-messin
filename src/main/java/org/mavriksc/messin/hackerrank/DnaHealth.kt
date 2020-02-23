package org.mavriksc.messin.hackerrank

import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

//https://www.hackerrank.com/challenges/determining-dna-health

// working but failing all but 2 tests. need to not recalc lps
// not recalc lps but still failing same tests timeout
// https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/

// IDEAS:
//https://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/


// genes

//make map of genes to index list


fun main(args: Array<String>) {

    val tree = UKKSuffixTree("abcabxabcd\$")
    tree.buildSufFixTree()
//    val scan = Scanner(File("D:\\code\\jus-messin\\src\\main\\resources\\DNA-2.txt"))
//
//    val n = scan.nextLine().trim().toInt()
//
//    val genes = scan.nextLine().split(" ").toTypedArray()
//
//    val health = scan.nextLine().split(" ").map { it.trim().toInt() }.toTypedArray()
//
//    val s = scan.nextLine().trim().toInt()
//    var low = Long.MAX_VALUE
//    var high = 0L
//    val start = Date()
//    for (sItr in 1..s) {
//        val firstLastd = scan.nextLine().split(" ")
//
//        val first = firstLastd[0].trim().toInt()
//
//        val last = firstLastd[1].trim().toInt()
//
//        val d = firstLastd[2]
//        val score = scoreStrand(d, first, last, genes, health)
//        low = min(low, score)
//        high = max(high, score)
//        counts.clear()
//    }
//    val end = Date()
//    println(end.time - start.time)
//    print("$low $high")
}

var lpss: MutableMap<String, Array<Int>> = mutableMapOf()
var counts: MutableMap<String, Long> = mutableMapOf()

fun scoreStrand(d: String, first: Int, last: Int, genes: Array<String>, health: Array<Int>): Long {
    var score = 0L
    for (i in first..last) {
        score += d.kmpMatchScore(genes[i]) * health[i]
    }
    return score
}

fun String.kmpMatchScore(pattern: String): Long {
    return counts.getOrPut(pattern) {
        var count = 0L
        val m = pattern.length
        val n = this.length
        val lps = pattern.computeLPS()
        var j = 0
        var i = 0
        while (i < n) {
            if (pattern[j] == this[i]) {
                j++
                i++
            }
            if (j == m) {
                count++
                j = lps[j - 1]
            } else if (i < n && pattern[j] != this[i]) {
                if (j != 0)
                    j = lps[j - 1]
                else
                    i++
            }
        }
        return count
    }
}

fun String.computeLPS(): Array<Int> {
    return lpss.getOrPut(this) {
        val m = this.length
        val lps = Array<Int>(m) { 0 }
        var i = 1
        var len = 0
        while (i < m) {
            if (this[i] == this[len]) {
                len++
                lps[i] = len
                i++
            } else {
                if (len != 0) {
                    len = lps[len - 1]
                } else {
                    lps[i] = len
                    i++
                }
            }
        }
        return lps
    }
}


class SuffixNode(var start: Int, val end: IntPtr) {
    val children = mutableMapOf<Char, SuffixNode>()
    var suffixLink: SuffixNode? = null
    var suffixIndex = -1
}

data class IntPtr(var value: Int) {
}

class UKKSuffixTree(val text: String) {
    var root: SuffixNode? = null
    var lastNewNode: SuffixNode? = null
    var activeNode: SuffixNode? = null
    var activeEdge = -1
    var activeLength = 0
    var remainingSuffixCount = 0
    var leafEnd = IntPtr(-1)
    var rootEnd: IntPtr? = null
    var splitEnd: IntPtr? = null
    var size = text.length

    private fun newNode(start: Int, end: IntPtr): SuffixNode {
        return SuffixNode(start, end).apply { suffixLink = root }
    }

    fun nodeString(node: SuffixNode): String {
        return if (node.start<0)  "" else text.substring(node.start, node.end.value + 1)
    }

    fun edgeLength(node: SuffixNode): Int = node.end.value - node.start + 1

    fun walkDown(currNode: SuffixNode): Boolean {
        val el = edgeLength(currNode)
        if (activeLength >= el) {
            activeEdge += el
            activeLength -= el
            activeNode = currNode
            return true
        }
        return false
    }

    private fun extendSuffixTree(pos: Int) {
        leafEnd.value = pos
        remainingSuffixCount++
        lastNewNode = null

        while (remainingSuffixCount > 0) {
            if (activeLength == 0)
                activeEdge = pos
            if (activeNode!!.children[text[activeEdge]] == null) {
                activeNode!!.children[text[activeEdge]] = newNode(pos, leafEnd)
                lastNewNode?.suffixLink = activeNode
                lastNewNode = null
            } else {
                val next = activeNode!!.children[text[activeEdge]]!!
                if (walkDown(next))
                    continue
                if (text[next.start + activeLength] == text[pos]) {
                    if (lastNewNode != null && activeNode != root) {
                        lastNewNode?.suffixLink = activeNode
                        lastNewNode = null
                    }
                    activeLength++
                    break
                }
                splitEnd = IntPtr(next.start + activeLength - 1)
                val split = newNode(next.start, splitEnd!!)

                // Something here isn't right i need to set the position of something different
                // or else i'm overwriting it
                activeNode!!.children[text[activeEdge]] = split
                split.children[text[activeEdge]] = newNode(pos, leafEnd) // here
                next.start += activeLength
                split.children[text[next.start]] = next // and here
                // probably need to add 1 or active length to one of them
                // look at algo to figure it out continuing transcription

                lastNewNode?.suffixLink = split
                lastNewNode = split
            }
            remainingSuffixCount--
            if (activeNode == root && activeLength > 0) {
                activeLength--
                activeEdge = pos - remainingSuffixCount + 1
            } else if (activeNode != root) {
                activeNode = activeNode!!.suffixLink
            }
        }
    }

    private fun setSuffixIndexByDFS(n: SuffixNode, labelHeight: Int) {
        if (n.start != -1)
            print(text.substring(n.start, n.end.value + 1))
        var leaf = 1
        n.children.forEach {
            if (leaf == 1 && n.start != -1)
                println("[${n.suffixIndex}]")
            leaf = 0
            setSuffixIndexByDFS(it.value, labelHeight + edgeLength(it.value))
        }
        if (leaf == 1) {
            n.suffixIndex = size - labelHeight
            println("[${n.suffixIndex}]")
        }
    }

    fun buildSufFixTree() {
        rootEnd = IntPtr(-1)
        root = newNode(-1, rootEnd!!)
        activeNode = root
        for (i in 0 until size) {
            extendSuffixTree(i)
        }
        val labelHeight = 0
        setSuffixIndexByDFS(root!!, labelHeight)
    }

}


