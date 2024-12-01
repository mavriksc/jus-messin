package org.mavriksc.messin

fun String.readFile() = ClassLoader.getSystemResourceAsStream(this)?.bufferedReader()?.readLines()

fun <T> String.useLines(block: (Sequence<String>) -> T): T = ClassLoader.getSystemResourceAsStream(this)?.bufferedReader()!!.useLines(block)