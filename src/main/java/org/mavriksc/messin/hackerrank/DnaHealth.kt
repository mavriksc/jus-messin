package org.mavriksc.messin.hackerrank

import java.io.File
import java.util.*

//https://www.hackerrank.com/challenges/determining-dna-health

// working but failing all but 2 tests. need to not recalc lps
// not recalc lps but still failing same tests timeout
// https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/
//https://www.sciencedirect.com/science/article/pii/S1570866703000650
//https://www.cs.cmu.edu/~ckingsf/bioinfo-lectures/suffixarrays.pdf
// IDEAS:
//https://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/


//cat all the different trials together with a special character not in genes
// keeping len+1 and the index pairs in a list
// . build suffix tree/ array ( create search that returns indices in text)
// search all genes keeping running totals in a list  then max and min value of that list


// genes

//make map of genes to index list: 103974
// time without maping: 59656
// with memo but on laptop:71245 home: 45587

//caches for old method probably not needed anymore
var lpss: MutableMap<String, Array<Int>> = mutableMapOf()
var counts: MutableMap<String, Long> = mutableMapOf()

fun main() {
    // TODO get recources working right
    val scan = Scanner(File("C:\\git\\mystuff\\jus-messin\\src\\main\\resources\\DNA-2.txt"))
    val start = Date()

    val n = scan.nextLine().trim().toInt()

    val genes = scan.nextLine().split(" ").toTypedArray()

    //val geneMap = scan.nextLine().split(" ").mapIndexed { index, s -> index to s}.groupBy({it.second},{it.first})

    val health = scan.nextLine().split(" ").map { it.trim().toInt() }.toTypedArray()

    val s = scan.nextLine().trim().toInt()
    val inputs = Array<StrandInfo?>(s) { null }
    val geneCat = StringBuilder()
    for (inputRow in 0 until s) {
        val firstLastD = scan.nextLine().split(" ")
        val first = firstLastD[0].trim().toInt()
        val last = firstLastD[1].trim().toInt()
        val d = firstLastD[2]
        geneCat.append(d).append("^")
        inputs[inputRow] = StrandInfo(first, last, geneCat.length)
    }
    val tree = UKKSuffixTree(geneCat.toString())
    scoreAllStrands(tree, inputs, genes, health)

    val end = Date()
    println(end.time - start.time)
}

fun scoreAllStrands(tree: UKKSuffixTree, inputs: Array<StrandInfo?>, genes: Array<String>, health: Array<Int>) {
    val cache = mutableMapOf<String, Array<Int>>()
    val scores = Array(inputs.size) { 0L }

    //think about storing the strand hit count
    genes.mapIndexed() { i, g -> i to cache.getOrPut(g) { indicesToStrandCount(tree.patternIndices(g), inputs) } }
            .forEach { geneIndexAndStrandCounts ->
                geneIndexAndStrandCounts.second
                        .forEachIndexed { index, count ->
                            if (geneIndexAndStrandCounts.first >= inputs[index]!!.first &&geneIndexAndStrandCounts.first <= inputs[index]!!.last) {
                                scores[index] += count.toLong()*health[geneIndexAndStrandCounts.first]
                            }
                        }
            }

    println("${scores.min()} ${scores.max()}")
}

fun indicesToStrandCount(indices: List<Int>, inputs: Array<StrandInfo?>): Array<Int> {
    //may need to reverse or sort indices
    var strandIndex = 0
    var count = 0
    val counts = Array(inputs.size) { 0 }
    indices.reversed().forEach() {
        if (it < inputs[strandIndex]!!.size)
            count++
        else {
            counts[strandIndex] = count
            count = 1
            do {
                strandIndex++
            } while (it > inputs[strandIndex]!!.size)
        }
    }
    return counts
}

class StrandInfo(val first: Int, val last: Int, val size: Int) {
    // since things are a different length instead of keeping the length of each keep the length of all previous strands.
    var score = 0L
}


fun scoreStrandKMP(d: String, first: Int, last: Int, genes: Array<String>, health: Array<Int>): Long =
        (first..last).map { d.kmpMatchScore(genes[it]) * health[it] }.sum()

fun scoreStrandUKK(d: String, first: Int, last: Int, genes: Array<String>, health: Array<Int>): Long {
    val tree = UKKSuffixTree(d)
    val cache = mutableMapOf<String, Long>()
    return (first..last).map { cache.getOrPut(genes[it]) { tree.patternCount(genes[it]).toLong() } * health[it] }.sum()
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
        val lps = Array(m) { 0 }
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

class UKKSuffixTree(val text: String) {
    private val root = newNode(-1, IntPtr(-1))
    private var activeNode = root
    private var lastNewNode: SuffixNode? = null
    private var activeEdge = -1
    private var activeLength = 0
    private var remainingSuffixCount = 0
    private var leafEnd = IntPtr(-1)
    private var splitEnd: IntPtr? = null
    var size = text.length

    init {
        buildSufFixTree()
    }

    class SuffixNode(var start: Int, val end: IntPtr) {
        val children = sortedMapOf<Char, SuffixNode>()
        var suffixLink: SuffixNode? = null
        var suffixIndex = -1
    }

    data class IntPtr(var value: Int)

    private fun newNode(start: Int, end: IntPtr): SuffixNode {
        return SuffixNode(start, end).apply { suffixLink = root }
    }

    fun nodeString(node: SuffixNode): String {
        return if (node.start < 0) "" else text.substring(node.start, node.end.value + 1)
    }

    private fun edgeLength(node: SuffixNode): Int {
        if (node == root)
            return 0
        return node.end.value - node.start + 1
    }

    private fun walkDown(currNode: SuffixNode): Boolean {
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
            if (activeNode.children[text[activeEdge]] == null) {
                activeNode.children[text[activeEdge]] = newNode(pos, leafEnd)
                lastNewNode?.suffixLink = activeNode
                lastNewNode = null
            } else {
                val next = activeNode.children[text[activeEdge]]!!
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


                activeNode.children[text[activeEdge]] = split
                split.children[text[pos]] = newNode(pos, leafEnd)
                next.start += activeLength
                split.children[text[next.start]] = next

                lastNewNode?.suffixLink = split
                lastNewNode = split
            }
            remainingSuffixCount--
            if (activeNode == root && activeLength > 0) {
                activeLength--
                activeEdge = pos - remainingSuffixCount + 1
            } else if (activeNode != root) {
                activeNode = activeNode.suffixLink!!
            }
        }
    }

    private fun setSuffixIndexByDFS(n: SuffixNode, labelHeight: Int) {
        var leaf = 1
        n.children.forEach {
            leaf = 0
            setSuffixIndexByDFS(it.value, labelHeight + edgeLength(it.value))
        }
        if (leaf == 1) {
            n.suffixIndex = size - labelHeight
        }
    }

    fun buildSufFixTree() {
        for (i in 0 until size) {
            extendSuffixTree(i)
        }
        val labelHeight = 0
        setSuffixIndexByDFS(root, labelHeight)
    }

    // in here and in doTraversal on ' if(n->suffixIndex > -1) ' return empty list instead of 0 or -1
    // call other counting methods but just push n-> suffixIndex into result list.
    private fun doTraversalToCountLeafList(n: SuffixNode): MutableList<Int> {
        if (n.suffixIndex > -1)
            return mutableListOf(n.suffixIndex)
        return n.children.values.map { doTraversalToCountLeafList(it) }.flatten().toMutableList()
    }

    private fun traverseEdge(str: String, idx: Int, start: Int, end: Int): Int {
        var k = start
        var index = idx
        do {
            if (text[k] != str[index])
                return -1
            k++
            index++
        } while (k <= end && index < str.length)
        if (index == str.length)
            return 1
        return 0
    }

    private fun doTraversalList(n: SuffixNode, str: String, idx: Int, falseForFindTrueForCount: Boolean): List<Int> {
        var index = idx
        if (n.start != -1) {
            val result = traverseEdge(str, index, n.start, n.end.value)
            if (result == -1)//no match
                return emptyList()
            if (result == 1) {//match
                return if (n.suffixIndex > -1 && falseForFindTrueForCount)
                    listOf(n.suffixIndex)
                else {
                    return doTraversalToCountLeafList(n)
                }
            }
        }
        index += edgeLength(n)
        return if (n.children.containsKey(str[index]))
            doTraversalList(n.children[str[index]]!!, str, index, falseForFindTrueForCount)
        else emptyList()
    }

    fun patternIndices(str: String): List<Int> {
        return doTraversalList(root, str, 0, true)
    }

    fun patternCount(str: String): Int {
        return doTraversalList(root, str, 0, true).size
    }

    fun checkForSubString(str: String): Boolean {
        return doTraversalList(root, str, 0, false).isNotEmpty()
    }

}


