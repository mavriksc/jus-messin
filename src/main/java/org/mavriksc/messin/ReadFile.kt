package org.mavriksc.messin

fun String.readFile() = ClassLoader.getSystemResourceAsStream(this)?.bufferedReader()?.readLines()
