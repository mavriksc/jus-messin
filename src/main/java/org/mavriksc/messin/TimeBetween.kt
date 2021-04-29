package org.mavriksc.messin

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

fun main() {
    val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val d1 = sdf.parse("Sun Aug 02 19:53:57 CDT 2020")
    val d2 = sdf.parse("Mon Aug 03 01:58:36 CDT 2020")
    print(TimeUnit.HOURS.convert(d2.time-d1.time, TimeUnit.MILLISECONDS))
}
