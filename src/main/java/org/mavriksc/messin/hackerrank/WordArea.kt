package org.mavriksc.messin.hackerrank

fun designerPdfViewer(h:Array<Int>,word:String)= word.length * word.toLowerCase().chars().map { h[it-'a'.toInt()] }.max().asInt
