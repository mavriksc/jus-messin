package org.mavriksc.messin

fun String.readFile() = this.toReader()
    .readLines()

fun <T> String.mapLinesToList(transform: (String) -> T): List<T> = this.toReader()
    .useLines { it.map(transform).toList() }

fun String.forEachLine(block: (String) -> Unit) = this.toReader().forEachLine { block(it)  }

fun String.toReader() = ClassLoader
    .getSystemResourceAsStream(this)
    ?.bufferedReader()!!

