package org.mavriksc.messin


    fun main() {
        val list: MutableList<String> = ArrayList()
        list.add("abc")
        list.add("abc")
        list.add("def")
        val things = list.mapIndexed { index, s -> index to s}.groupBy({it.second},{it.first})
        things.forEach { (s, integers) ->
            println(s)
            integers.forEach { x -> println(x) }
        }
    }