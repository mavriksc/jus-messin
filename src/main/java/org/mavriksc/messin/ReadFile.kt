package org.mavriksc.messin

fun String.readFile() = this.toReader()
    .readLines()

fun <T> String.mapLines(transform: (String) -> T): List<T> = this.toReader()
    .useLines { it.map(transform).toList() }

fun String.toReader() = ClassLoader
    .getSystemResourceAsStream(this)
    ?.bufferedReader()!!

