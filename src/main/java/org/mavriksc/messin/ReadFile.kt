package org.mavriksc.messin

fun String.readFile() = this.toReader()
    .readLines()

fun <T> String.mapLines(transform: (String) -> T): Sequence<T> = this.toReader()
    .useLines { lines -> lines.map(transform) }

fun String.toReader() = ClassLoader
    .getSystemResourceAsStream(this)
    ?.bufferedReader()!!

