package org.mavriksc.messin.modint

import kotlin.properties.Delegates

var modulo = 69
var i: Int by Delegates.observable(0) { _, oldValue, newValue ->
    if (newValue == oldValue)
        println("didnt change")
    else {
        println("new value $newValue")
        i = newValue % modulo
        println("set value $i")
    }
}


fun main() {
    i = 67
    i++
    println(i)
    i++
    println(i)
    i++
    println(i)
    i++
    println(i)
    i++
    println(i)
    i++
    println(i)
    i++
    println(i)
}