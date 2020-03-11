package org.mavriksc.messin.hackerrank

import java.io.BufferedReader
import java.io.FileReader

//2
//3
//1 3 1
//2 1 2
//3 3 3
//3
//0 2 1
//1 1 1
//2 0 0
fun main() {
    val reader = BufferedReader(FileReader("C:\\git\\mystuff\\jus-messin\\src\\main\\resources\\cob\\input01.txt"))
    val q = reader.readLine().trim().toInt()

    for (qItr in 1..q) {
        val n = reader.readLine().trim().toInt()

        val container = Array(n) { Array(n) { 0 } }

        for (i in 0 until n) {
            container[i] = reader.readLine().split(" ").map{ it.trim().toInt() }.toTypedArray()
        }

        val result = organizingContainers(container)

        println(result)
    }
}

fun organizingContainers(container: Array<Array<Int>>): String {
    for (i in container.indices) {
        var ins = 0L
        var outs = 0L
        for (j in container.indices) {
            if (j != i) {
                ins += container[j][i]
                outs += container[i][j]
            }
        }
        if (ins != outs)
            return "Impossible"
    }
    return "Possible"
}

fun <R>permuteList(list: List<R>): List<List<R>> {
    if (list.size == 1)
        return listOf(list)
    return list.flatMap {
        val tail = list.toMutableList()
        tail.remove(it)
        permuteList(tail).map { tp ->
            mutableListOf(it).apply { addAll(tp) }
        }
    }
}
