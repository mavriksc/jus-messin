package org.mavriksc.messin.random

fun main() {
    var last = 0
    (5..10_000).forEach {
        if (it % 6 != 0) {
            if(it-last == 2) {
                println("$last, $it")
            }
            last = it
        }
    }
}