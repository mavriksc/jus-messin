package org.mavriksc.messin.hackerrank

import java.io.File
import kotlin.math.min

//https://www.hackerrank.com/challenges/queens-attack-2/problem

fun main() {
    val qaInput = qaInput()
    print(queensAttack(qaInput.n, qaInput.k, qaInput.r_q, qaInput.c_q, qaInput.obstacles))

}

fun qaInput(): QAInput {
    val lines = File("D:\\code\\jus-messin\\src\\main\\resources\\QA2.txt").readLines()
    val nk = lines[0].split(' ')
    val n = nk[0].toInt()
    val k = nk[1].toInt()
    val qpos = lines[1].split(' ')
    val r_q = qpos[0].toInt()
    val c_q = qpos[1].toInt()
    val obstacles = mutableListOf<Array<Int>>()
    for (i in 2 until lines.size) {
        val rq = lines[i].split(' ')
        obstacles.add(arrayOf(rq[0].toInt(), rq[1].toInt()))
    }
    return QAInput(n, k, r_q, c_q, obstacles.toTypedArray())
}

class QAInput(val n: Int, val k: Int, val r_q: Int, val c_q: Int, val obstacles: Array<Array<Int>>)

fun queensAttack(n: Int, k: Int, r_q: Int, c_q: Int, obstacles: Array<Array<Int>>): Int {
    val queenMainDiagDistance = r_q - c_q
    val queenAltDiagDistance = r_q + c_q
    var top = r_q - 1
    var bottom = n - r_q
    var left = c_q - 1
    var right = n - c_q
    var tl = min(r_q - 1, c_q - 1)
    var tr = min(r_q - 1, n - c_q)
    var br = min(n - r_q, n - c_q)
    var bl = min(n - r_q, c_q - 1)

    obstacles.takeWhile { top + bottom + left + right + tl + tr + bl + br > 0 }.forEach {
        val r = it[0]
        val c = it[1]
        if (c == c_q) {
            if (r < r_q) {
                top = min(top, (r_q - r) - 1)
            } else {
                bottom = min(bottom, (r - r_q) - 1)
            }
        } else if (r == r_q) {
            if (c < c_q) {
                left = min(left, (c_q - c) - 1)
            } else {
                right = min(right, (c - c_q) - 1)
            }
        } else if (r - c == queenMainDiagDistance) {
            if (r < r_q) {//tl
                tl = min(tl, (r_q - r) - 1)
            } else {//br
                br = min(br, (r - r_q) - 1)
            }
        } else if (r + c == queenAltDiagDistance) {
            if (r > r_q) {
                bl = min(bl, (r - r_q) - 1)
            } else {
                tr = min(tr, (r_q - r) - 1)
            }
        }
    }
    return top + bottom + left + right + tl + tr + bl + br

}
