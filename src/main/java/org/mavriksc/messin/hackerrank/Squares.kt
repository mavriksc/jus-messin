package org.mavriksc.messin.hackerrank

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

//We got the wrong answer for a:303390079  b:497423761  we got:4885  but answer was 4884

fun main() {
//    val input = readFile("D:\\code\\jus-messin\\src\\main\\resources\\squares-input03.txt")
//    val answers = readFile("D:\\code\\jus-messin\\src\\main\\resources\\squares-output03.txt")
//    input.filterIndexed { i, _ -> i > 0 }
//        .forEachIndexed { i, line ->
//            val inputs = line.split(" ")
//            if (answers[i].toInt() != squares(inputs[0].toInt(), inputs[1].toInt())) {
//                println(
//                    "We got the wrong answer for a:${inputs[0]}  b:${inputs[1]}  we got:${
//                        squares(
//                            inputs[0].toInt(),
//                            inputs[1].toInt()
//                        )
//                    }  but answer was ${answers[i]}"
//                )
//            }
//        }
    println(squares(303390079,497423761))
    squaresTest(303390079,497423761)



}

//https://www.hackerrank.com/challenges/sherlock-and-squares/problem
fun squares(a: Int, b: Int): Int = (floor(sqrt(b.toDouble())) - ceil(sqrt(a.toDouble())) + 1).toInt()

fun squaresTest(a: Int, b: Int){
    println("sqrt(b):${sqrt(b.toDouble())}")
    println("floor(sqrt(b)):${floor(sqrt(b.toDouble()))}")

    println("sqrt(a):${sqrt(a.toDouble())}")
    println("ceil(sqrt(a):${ceil(sqrt(a.toDouble()))}")

}
