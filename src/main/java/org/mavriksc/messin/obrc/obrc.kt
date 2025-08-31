package org.mavriksc.messin.obrc

import java.io.File
import java.util.TreeMap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun main() {
    val start = System.currentTimeMillis()
    naive()
    val end = System.currentTimeMillis()
    println("Elapsed time: ${end - start}ms")
}

data class Measurement(val station: String, val temp: Double) {
    constructor(stationAndTemp: String) : this(
        stationAndTemp.substringBefore(";"), stationAndTemp.substringAfter(";").toDouble()
    )
}

data class MeasurementAggregator(
    var min: Double = Double.POSITIVE_INFINITY,
    var max: Double = Double.NEGATIVE_INFINITY,
    var sum: Double = 0.0,
    var count: Int = 0
)

fun Double.roundTenths() = (this * 10.0).roundToInt() / 10.0
data class ResultRow(val min: Double, val avg: Double, val max: Double) {
    override fun toString() = "${min.roundTenths()}/${avg.roundTenths()}/${max.roundTenths()}"

}

fun naive() {
    val holding: TreeMap<String, MeasurementAggregator> = TreeMap()

    val results = File("D:\\code\\jus-messin\\src\\main\\resources\\obrc\\measurements3.txt")
        .useLines { lines ->
        lines.map { line -> Measurement(line) }
            .fold(holding) { acc, measurement ->
                acc[measurement.station] = acc.getOrDefault(measurement.station, MeasurementAggregator())
                    .apply {
                        count++
                        sum += measurement.temp
                        min = min(min, measurement.temp)
                        max = max(max, measurement.temp)
                    }
                acc
            }.map { Pair(it.key, ResultRow(it.value.min, it.value.sum.roundTenths() / it.value.count, it.value.max)) }
    }

    println(results)

}