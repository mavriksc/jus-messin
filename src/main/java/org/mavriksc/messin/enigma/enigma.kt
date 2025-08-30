package org.mavriksc.messin.enigma

const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890"
val rotors = mutableListOf<String>()
val derangementMap = mutableMapOf<Int, MutableList<Char>>()
val generatedRotors = ("G2VOFENU3CSAQW7XIY90 JKZL1H8T4M6PDR5B\n" +
        "PUHIQM2J4WAYLG5F0CTX6BRV8SDKZE O7N931\n" +
        "6Q3HK7XWOULBEZRTA204GD9F1P8 JSYINVMC5\n" +
        "Y NAB0H7S2O6D9C1J4UMERGQIVK3P5ZWLXF8T\n" +
        "QWU2J6VZD 7MXBHIEORN054GKCTS3A1Y8PLF9").split("\n").toList()

fun genDerangement() {
    ALPHABET.forEachIndexed { index, c ->
        derangementMap.getOrPut(index, { mutableListOf<Char>() })
            .add(c)
    }
    (1..5).forEach { _ ->
        val charset = ALPHABET.toMutableList()
        val rotor = ALPHABET.mapIndexed { i, _ ->
            val selected = charset.filter{ !derangementMap[i]!!.contains(it)}.random()
            charset.remove(selected)
            selected
        }.joinToString("")
        println(rotor)
        rotors.add(rotor)
        rotor.forEachIndexed { index, c ->
            derangementMap[index]!!.add(c)
        }
    }

}



fun main() {
    genDerangement()
}