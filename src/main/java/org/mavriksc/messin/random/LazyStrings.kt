package org.mavriksc.messin.random

import kotlin.random.Random

fun main(){
    //setup strings
    val people = (0..1000).map { Person(randomStringLen(10), randomStringLen(10)) }
    val startTime1 = System.currentTimeMillis()
    lazyStringPrint(people)
    val startTime2 = System.currentTimeMillis()
    newString(people)
    println("Time taken Lazy: ${System.currentTimeMillis() - startTime1}")
    println("Time taken New: ${System.currentTimeMillis() - startTime2}")
}



fun lazyStringPrint(items: List<Person>){
    fun grito(p: Person): () -> String = {"Hello, ${p.firstName} ${p.lastName} how are you doing"}
    items.forEach { p -> println("${grito(p)}!") }

}

fun newString(items: List<Person>){
    items.forEach {
        val grito = "Hello, ${it.firstName} ${it.lastName} how are you doing"
        println("$grito!")
    }

}
fun randomStringLen(length:Int):String{
    return (1..length).map { _ -> Random.nextInt('a'.code, 'z'.code) }.map { it.toChar() }.joinToString("")
}


data class Person(val firstName: String, val lastName: String)