package org.mavriksc.messin.collatz

import java.io.File

private const val DEFAULT_MAX_ODD_SEED = 1_000_000L
private const val DEFAULT_OUTPUT_DIR = "build/collatz-investigation"
private const val DEFAULT_EMIT_CSVS = true
private const val DEFAULT_TARGET_SEED = 837_799L
private const val DEFAULT_TARGET_SEED_SELECTOR = ""

fun main() {
    val maxOddSeed = System.getProperty("collatz.maxOddSeed")?.toLong() ?: DEFAULT_MAX_ODD_SEED
    val outputDirPath = System.getProperty("collatz.outputDir") ?: DEFAULT_OUTPUT_DIR
    val emitCsvs = System.getProperty("collatz.emitCsvs")?.toBooleanStrictOrNullCompat() ?: DEFAULT_EMIT_CSVS
    val targetSeedSelector = System.getProperty("collatz.targetSeedSelector") ?: DEFAULT_TARGET_SEED_SELECTOR
    val targetSeed = resolveTargetSeed(
        explicitTarget = System.getProperty("collatz.targetSeed"),
        selector = targetSeedSelector,
        maxOddSeed = maxOddSeed
    )
    val outputDir = File(outputDirPath)

    val towerTransitions = (1L..maxOddSeed step 2L)
        .map { buildTowerTransition(it) }
    val maxEvenToInspect = towerTransitions.maxOfOrNullCompat { it.rawThreeXPlusOne } ?: 0L
    val dualParentEvens = (2L..maxEvenToInspect step 2L)
        .mapNotNull { buildDualParentEvenRecord(it) }
    val trajectory = buildTrajectory(targetSeed)

    if (emitCsvs) {
        outputDir.mkdirs()
        writeTowerTransitionsCsv(File(outputDir, "tower-transitions.csv"), towerTransitions)
        writeDualParentCsv(File(outputDir, "dual-parent-evens.csv"), dualParentEvens)
        writeTrajectoryCsv(File(outputDir, "trajectory-$targetSeed.csv"), trajectory)
    }

    printSummary(maxOddSeed, targetSeed, outputDir, emitCsvs, towerTransitions, dualParentEvens, trajectory)
}

private fun writeTowerTransitionsCsv(file: File, transitions: List<TowerTransitionRecord>) {
    file.writeText(
        buildString {
            appendLineCompat("sourceOdd,sourceFactorization,sourcePrimeNumberSystem,rawThreeXPlusOne,destinationOddCore,absorbedTowerHeight")
            transitions.forEach { row ->
                appendLineCompat(
                    listOf(
                        row.sourceOdd.toString(),
                        csv(row.sourceFactorization),
                        row.sourcePrimeNumberSystem.toString(),
                        row.rawThreeXPlusOne.toString(),
                        row.destinationOddCore.toString(),
                        row.absorbedTowerHeight.toString()
                    ).joinToString(",")
                )
            }
        }
    )
}

private fun writeDualParentCsv(file: File, records: List<DualParentEvenRecord>) {
    file.writeText(
        buildString {
            appendLineCompat("evenNumber,oddPredecessor,oddCore,towerHeight")
            records.forEach { row ->
                appendLineCompat(
                    listOf(
                        row.evenNumber.toString(),
                        row.oddPredecessor.toString(),
                        row.oddCore.toString(),
                        row.towerHeight.toString()
                    ).joinToString(",")
                )
            }
        }
    )
}

private fun writeTrajectoryCsv(file: File, records: List<CollatzStepRecord>) {
    file.writeText(
        buildString {
            appendLineCompat("stepIndex,value,parity,factorization,primeNumberSystem,oddCore,towerHeight,nextValue,moveType")
            records.forEach { row ->
                appendLineCompat(
                    listOf(
                        row.stepIndex.toString(),
                        row.value.toString(),
                        row.parity,
                        csv(row.factorization),
                        row.primeNumberSystem.toString(),
                        row.oddCore.toString(),
                        row.towerHeight.toString(),
                        row.nextValue?.toString() ?: "",
                        row.moveType
                    ).joinToString(",")
                )
            }
        }
    )
}

private fun printSummary(
    maxOddSeed: Long,
    targetSeed: Long,
    outputDir: File,
    emitCsvs: Boolean,
    transitions: List<TowerTransitionRecord>,
    dualParentEvens: List<DualParentEvenRecord>,
    trajectory: List<CollatzStepRecord>
) {
    println("Collatz tower investigation")
    println("Odd seeds scanned: 1..$maxOddSeed")
    println("Target trajectory seed: $targetSeed")
    if (emitCsvs) {
        println("CSV output directory: ${outputDir.path}")
    }
    println()

    println("Most common destination towers from odd jumps:")
    transitions
        .groupingBy { it.destinationOddCore }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(10)
        .forEach { (oddCore, count) ->
            println("  tower $oddCore <- $count jumps")
        }
    println()

    println("Most common tower-to-tower bottom attachments:")
    transitions
        .groupingBy { "${it.sourceOdd}->${it.destinationOddCore}" }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(10)
        .forEach { (transition, count) ->
            println("  $transition <- $count jumps")
        }
    println()

    println("Most common absorbed heights after 3n+1:")
    transitions
        .groupingBy { it.absorbedTowerHeight }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(10)
        .forEach { (height, count) ->
            println("  height $height <- $count jumps")
        }
    println()

    println("Odd attachments by destination tower:")
    transitions
        .groupBy { it.destinationOddCore }
        .entries
        .sortedByDescending { it.value.size }
        .take(8)
        .forEach { (tower, rows) ->
            val attachments = rows
                .map { it.sourceOdd }
                .take(8)
                .joinToString(", ")
            println("  tower $tower gets odd attachments from [$attachments]")
        }
    println()

    println("Factor signatures with the strongest height bias:")
    transitions
        .groupBy { factorSignature(it.sourceOdd) }
        .filterValues { it.size >= 10 }
        .entries
        .sortedByDescending { entry ->
            entry.value.groupingBy { it.absorbedTowerHeight }.eachCount().values.max() ?: 0
        }
        .take(8)
        .forEach { (signature, rows) ->
            val dominantHeight = rows
                .groupingBy { it.absorbedTowerHeight }
                .eachCount()
                .entries
                .maxByOrNullCompat { it.value }
            val towerLeaders = rows
                .groupingBy { it.destinationOddCore }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(3)
                .joinToString(", ") { "${it.key}:${it.value}" }
            println(
                "  $signature -> dominant height ${dominantHeight?.key} " +
                    "(${dominantHeight?.value}/${rows.size}), top towers [$towerLeaders]"
            )
        }
    println()

    println("Dual-parent even numbers found: ${dualParentEvens.size}")
    dualParentEvens.take(10).forEach { row ->
        println(
            "  ${row.evenNumber} has odd predecessor ${row.oddPredecessor}, " +
                "tower ${row.oddCore} at height ${row.towerHeight}"
        )
    }
    println()

    val oddSteps = trajectory.filter { it.moveType == "odd-jump" }
    val repeatedOddCores = trajectory
        .groupingBy { it.oddCore }
        .eachCount()
        .entries
        .filter { it.value > 1 }
        .sortedByDescending { it.value }
        .take(10)
        .joinToString(", ") { "${it.key}:${it.value}" }
    val repeatedPrimePatterns = trajectory
        .groupingBy { it.primeNumberSystem }
        .eachCount()
        .entries
        .filter { it.value > 1 }
        .sortedByDescending { it.value }
        .take(10)
        .joinToString(", ") { "${it.key}:${it.value}" }
    println("Trajectory summary for $targetSeed:")
    println("  steps: ${trajectory.size - 1}")
    println("  odd jumps: ${oddSteps.size}")
    println("  max value: ${trajectory.maxOfOrNullCompat { it.value } ?: 0L}")
    println("  terminal value: ${trajectory.lastOrNull()?.value}")
    println("  repeated odd cores: ${if (repeatedOddCores.isBlank()) "none" else repeatedOddCores}")
    println("  repeated prime-number-system values: ${if (repeatedPrimePatterns.isBlank()) "none" else repeatedPrimePatterns}")
}

private fun csv(value: String): String = "\"${value.replace("\"", "\"\"")}\""

private fun resolveTargetSeed(explicitTarget: String?, selector: String, maxOddSeed: Long): Long {
    if (!explicitTarget.isNullOrBlank()) {
        return explicitTarget.toLong()
    }

    return when {
        selector == "longest-under-max" -> findLongestTrajectorySeed(maxOddSeed)
        selector.startsWith("longest-under:") -> findLongestTrajectorySeed(selector.substringAfter(':').toLong())
        else -> DEFAULT_TARGET_SEED
    }
}

private fun findLongestTrajectorySeed(limit: Long): Long {
    require(limit >= 1L) { "Longest-trajectory search requires a positive limit." }
    var bestSeed = 1L
    var bestLength = 0
    for (seed in 1L..limit) {
        val length = trajectoryLength(seed)
        if (length > bestLength) {
            bestLength = length
            bestSeed = seed
        }
    }
    return bestSeed
}

private fun trajectoryLength(seed: Long): Int {
    var steps = 0
    var current = seed
    while (current != 1L) {
        current = collatzNext(current)
        steps++
    }
    return steps
}

private fun String.toBooleanStrictOrNullCompat(): Boolean? = when (trim().toLowerCase()) {
    "true" -> true
    "false" -> false
    else -> null
}

private fun <T> Iterable<T>.maxOfOrNullCompat(selector: (T) -> Long): Long? {
    var found = false
    var max = Long.MIN_VALUE
    for (item in this) {
        val value = selector(item)
        if (!found || value > max) {
            found = true
            max = value
        }
    }
    return if (found) max else null
}

private fun <T> Iterable<T>.maxByOrNullCompat(selector: (T) -> Int): T? {
    var bestItem: T? = null
    var bestValue = Int.MIN_VALUE
    var found = false
    for (item in this) {
        val value = selector(item)
        if (!found || value > bestValue) {
            bestItem = item
            bestValue = value
            found = true
        }
    }
    return bestItem
}

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}
