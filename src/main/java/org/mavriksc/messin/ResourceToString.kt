package org.mavriksc.messin

import java.io.InputStream
import java.util.*

object ResourceToString {
    @JvmStatic
    fun main(args: Array<String>) {
        val pi = convertStreamToString(ResourceToString::class.java.classLoader.getResourceAsStream("pi.txt")!!)
        println(pi)
    }

    fun convertStreamToString(`is`: InputStream): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}
