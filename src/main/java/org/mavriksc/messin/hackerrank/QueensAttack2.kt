package org.mavriksc.messin.hackerrank

import kotlin.math.min

//https://www.hackerrank.com/challenges/queens-attack-2/problem

10/22 failing 

fun main() {
    print(queensAttack(8, 0, 5, 4, arrayOf(arrayOf(6, 5))))

}


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

    obstacles.forEach {
        val r = it[0]
        val c = it[1]
        if (c == c_q) {
            if (r < r_q) {
                top = min(top, (r_q - r)-1)
            } else {
                bottom = min(bottom, (r - r_q)-1)
            }
        } else if (r == r_q) {
            if (c < c_q) {
                left = min(left, (c_q - c)-1)
            } else {
                right = min(right, (c - c_q)-1)
            }
        } else if (r - c == queenMainDiagDistance) {
            if (r < r_q) {//tl
                tl = min(tl, (r_q - r)-1)
            } else {//br
                br = min(br, (r - r_q-1))
            }
        } else if (r + c == queenAltDiagDistance) {
            if (r < r_q) {
                bl = min(bl, (r - r_q)-1)
            } else {
                tr = min(tr, (r_q - r)-1)
            }
        }
    }
    return top + bottom + left + right + tl + tr + bl + br

}
