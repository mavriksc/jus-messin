package org.mavriksc.messin.random

import java.util.*

fun main() {
    val cal = Calendar.getInstance()
    cal.set(2020,Calendar.AUGUST,24)
    cal.add(Calendar.DAY_OF_YEAR,-60)

    print(cal.time)
}