package org.mavriksc.messin.hackerrank

import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.math.*

//https://www.hackerrank.com/challenges/determining-dna-health

//https://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_algorithm

// working but failing all but 2 tests. need to not recalc lps
// not recalc lps but still failing same tests timeout
// https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/
//https://www.sciencedirect.com/science/article/pii/S1570866703000650
//https://www.cs.cmu.edu/~ckingsf/bioinfo-lectures/suffixarrays.pdf
// IDEAS:
//https://www.geeksforgeeks.org/suffix-tree-application-2-searching-all-patterns/

//https://web.stanford.edu/class/cs97si/suffix-array.pdf

//*******DONE but still not fast enough
//create an Int array the size of the final string this maps an index in the total text
// use this to check for gene expression and updating  the score


// genes

//make map of genes to index list: 103974
// time without maping: 59656
// with memo but on laptop:71245 home: 45587
// cat strat with look uptable : 38891 and getting right answers again and passing 3 more tests
//passing 0.1,7,8,9 time out on rest 15,23 for example

///FSM strat: not working and somehow takes longer
// problem is trie loses duplicate entries. of dict words
// and it may be slower maybe try graph instead of arrays 

fun main() {
    //TODO get resources working right

// FSM kinda works getting issues on some tests though
//    val dict = arrayOf("he", "she", "hers", "his")
//    val acFSM = AhoCorasickFSM(dict)
//    acFSM.findDictWordsInText("ahishers")

    // how to create a reader in Hackerank
    //val r = System.`in`.bufferedReader()

    val reader = BufferedReader(FileReader("D:\\code\\jus-messin\\src\\main\\resources\\DNA-2.txt"))
    val start = Date()

    reader.readLine()// clear the number of genes
    val genesRow = reader.readLine()

    val genes = genesRow.split(" ").toTypedArray()

    val acFSM = AhoCorasickFSM(genes,genesRow.length)

    val health = reader.readLine().split(" ").map { it.trim().toInt() }.toTypedArray()

    val s = reader.readLine().trim().toInt()
    var min = Long.MAX_VALUE
    var max = 0L
    for (inputRow in 0 until s) {
        val firstLastD = reader.readLine().split(" ")
        val first = firstLastD[0].trim().toInt()
        val last = firstLastD[1].trim().toInt()
        val d = firstLastD[2]
        val score = acFSM.scoreStrand(d, StrandInfo(first, last), health)
        min = min(min,score)
        max = max(max,score)
    }

    println("$min $max")
    val end = Date()
    println(end.time - start.time)
}


fun scoreAllStrands(tree: UKKSuffixTree, inputs: Array<StrandInfo?>, genes: Array<String>, health: Array<Int>, textToStrand: Array<Int>) {
    val cache = mutableMapOf<String, List<Int>>()
    val scores = Array(inputs.size) { 0L }

    genes.mapIndexed { i, g -> i to cache.getOrPut(g) { tree.patternIndices(g) } }
            .forEach { (geneIndex, locations) ->
                locations.forEach {
                    val inputRow = textToStrand[it]
                    if (geneIndex >= inputs[inputRow]?.first!! && geneIndex <= inputs[inputRow]?.last!!)
                        scores[inputRow] += health[geneIndex].toLong()
                }
            }
    minMaxScores(scores)
}

fun minMaxScores(scores: Array<Long>) {
    var lil = Long.MAX_VALUE
    var big = 0L
    scores.forEach {
        lil = min(lil, it)
        big = max(big, it)
    }
    println("$lil $big")
}

fun countingSort(a: Array<Int>, place: Int, base: Int): Array<Int> {
    val b = Array(a.size) { 0 }
    val c = Array(base) { 0 }
    for (i in a.indices) {
        val digitOfAi = ((a[i] / base.toDouble().pow(place)) % base).toInt()
        c[digitOfAi] += 1
    }
    for (j in 1 until base) c[j] += c[j - 1]
    for (m in a.indices.reversed()) {
        val digitOfAi = ((a[m] / base.toDouble().pow(place)) % base).toInt()
        c[digitOfAi] -= 1
        b[c[digitOfAi]] = a[m]
    }
    return b
}

fun radixSort(a: Array<Int>, base: Int, maxVal: Int): Array<Int> {
    var output = a
    val digits = floor(log(maxVal.toDouble(), base.toDouble()) + 1).toInt()
    for (i in 0 until digits) output = countingSort(output, i, base)
    return output
}

class StrandInfo(val first: Int, val last: Int)

class SuffixArray(val text: String) {
    val n = text.length
    val l = Array(n) { EntRY(0, 0, 0) }
    val p = Array(ceil(log2(n.toDouble())).toInt()) { Array(n) { 0 } }
    val lcp = Array(n) { 0 }
    private var cnt = 1

    class EntRY(var nr0: Int, var nr1: Int, var p: Int) : Comparable<EntRY> {
        override fun compareTo(other: EntRY): Int {
            return if (this.nr0 == other.nr0)
                this.nr1 - other.nr1
            else this.nr0 - other.nr0
        }
    }

    init {
        createArray()
    }

    private fun createArray() {
        for (i in 0 until n) p[0][i] = text[i] - 'a'
        for (k in 1 until p.size) {
            for (i in 0 until n) {
                l[i].nr0 = p[k - 1][i]
                if (i + cnt < n)
                    l[i].nr1 = p[k - 1][i + cnt]
                else
                    l[i].nr1 = -1
                l[i].p = i
            }
            l.sort()
            for (j in 0 until n)
                if (j > 0 && l[j].nr0 == l[j - 1].nr0 && l[j].nr1 == l[j - 1].nr1)
                    p[k][l[j].p] = p[k][l[j - 1].p]
                else
                    p[k][l[j].p] = j
            cnt = cnt.shl(1)
        }
        for (i in 1..n) lcp[i] = lcp(i - 1, i)
    }

    private fun lcp(x: Int, y: Int): Int {
        if (x == y) return n - x
        val one = 1
        var xn = x
        var yn = y
        var k = text.length - 1
        var ret = 0
        do {
            if (p[k][xn] == p[k][yn]) {
                val oneShLK = one.shl(k)
                xn += oneShLK
                yn += oneShLK
                ret += oneShLK
            }
            k--
        } while (k >= 0)
        return ret
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

    private fun buildSufFixTree() {
        for (i in 0 until size) {
            extendSuffixTree(i)
        }
        val labelHeight = 0
        setSuffixIndexByDFS(root, labelHeight)
    }

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

    private fun doTraversalList(n: SuffixNode, str: String, idx: Int, falseForExistenceTrueForIncidents: Boolean): List<Int> {
        var index = idx
        if (n.start != -1) {
            val result = traverseEdge(str, index, n.start, n.end.value)
            if (result == -1)//no match
                return emptyList()
            if (result == 1) {//match
                return if (n.suffixIndex > -1 && falseForExistenceTrueForIncidents)
                    listOf(n.suffixIndex)
                else {
                    return doTraversalToCountLeafList(n)
                }
            }
        }
        index += edgeLength(n)
        return if (n.children.containsKey(str[index]))
            doTraversalList(n.children[str[index]]!!, str, index, falseForExistenceTrueForIncidents)
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

