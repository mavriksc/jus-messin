package org.mavriksc.messin

fun main() {
 findTwoMissing()
}

fun findOneMissing(){
    val o = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val l = o.shuffled().dropLast(1)
    println(l)
    println(o.reduce { a, b -> a xor b } xor l.reduce { a, b -> a xor b })
}

fun findTwoMissing(){
    val o = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val l = o.shuffled().dropLast(2)
    val uXv = o.reduce { a, b -> a xor b } xor l.reduce { a, b -> a xor b }
    val lsb = uXv and -uXv
    val pot = o.filter { it and lsb == 1 }.reduce { a, b -> a xor b }
    val pom = l.filter { it and lsb == 1 }.reduce { a, b -> a xor b }
    val v = pot xor pom
    val u = uXv xor v
    println(l)
    println("$u, $v")

}