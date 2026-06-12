package org.mavriksc.messin.random

import kotlin.math.max

object KPermuterKnownBest {
    private const val N = 6

    // Robin Houston length-872 n=6 candidate, using 1..6 as published.
    private const val HOUSTON_872 =
        "12345612345162345126345123645132645136245136425136452136451234651234156234152634152364152346152341652341256341253641253461253416253412653412356412354612354162354126354123654132654312645316243516243156243165243162543162453164253146253142653142563142536142531645231465231456231452631452361452316453216453126435126431526431256432156423154623154263154236154231654231564213564215362415362145362154362153462135462134562134652134625134621536421563421653421635421634521634251634215643251643256143256413256431265432165432615342613542613452613425613426513426153246513246531246351246315246312546321546325146325416325461325463124563214563241563245163245613245631246532146532416532461532641532614532615432651436251436521435621435261435216435214635214365124361524361254361245361243561243651423561423516423514623514263514236514326541362541365241356241352641352461352416352413654213654123"

    private val colors = listOf(
        "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m",
        "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m", "\u001B[95m", "\u001B[96m"
    )
    private const val RESET = "\u001B[0m"

    @JvmStatic
    fun main(args: Array<String>) {
        val useAnsi = args.none { it == "--plain" }
        val detailLimit = args.firstOrNull { it.startsWith("--limit=") }
            ?.substringAfter("=")
            ?.toIntOrNull()
            ?: 40
        val analysis = analyzeKnownBest()

        println("knownBestLength=${analysis.source.length}")
        println("uniquePermutations=${analysis.uniquePermutations}")
        println("rotationClasses=${analysis.classCount}")
        println("contiguousRotationRuns=${analysis.runs.size}")
        println("fullRotationRuns=${analysis.runs.count { it.windowCount == N }}")
        println("splitRotationClasses=${analysis.splitClassCount}")
        println("runLengthCounts=${analysis.runs.groupingBy { it.windowCount }.eachCount().toSortedMap()}")
        println("stepCounts=${analysis.transitions.groupingBy { it.step }.eachCount().toSortedMap()}")
        println("classSwitchStepCounts=${analysis.transitions.filter { !it.sameClass }.groupingBy { it.step }.eachCount().toSortedMap()}")
        println("smallestArrangementUnits=${analysis.runs.size}")
        println("sumUnitLengthsBeforeMerging=${analysis.runs.sumOf { it.textLength }}")
        println("forcedGapsOverPerfectRotationFlow=${analysis.transitions.count { !it.sameClass && it.step > 1 }}")
        println()

        println("Colorized known best by rotation class:")
        println(colorizeByRotationClass(analysis, useAnsi))
        println()

        println("First $detailLimit rotation-run units:")
        for (run in analysis.runs.take(detailLimit)) {
            println(run.describe())
        }
        println()

        println("Largest split rotation classes:")
        for (entry in analysis.classRuns.entries.sortedWith(compareByDescending<Map.Entry<String, List<RotationRun>>> { it.value.size }
            .thenBy { it.key }).filter { it.value.size > 1 }.take(detailLimit)) {
            val fragments = entry.value.joinToString(" | ") { "${it.start}-${it.end}:${it.windowCount}" }
            println("${entry.key} -> $fragments")
        }
        println()

        println("Suboptimal-looking class switches:")
        for (transition in analysis.transitions.filter { !it.sameClass }.take(detailLimit)) {
            println(transition.describe())
        }
        println()

        println("Wave-function-collapse view:")
        println("cells=${analysis.runs.size} rotation-run chunks")
        println("domains=remaining rotation classes plus partial-run lengths")
        println("constraints=permutation coverage, no duplicate permutation, suffix/prefix overlap, and each class must complete six rotations total")
        println("lowest-entropy cells are split classes with few legal continuation positions; these are the places to branch first")
    }

    fun analyzeKnownBest(): KnownBestAnalysis = analyzeKnownBest(HOUSTON_872)

    fun analyzeKnownBest(source: String): KnownBestAnalysis {
        val windows = permutationWindows(source)
        val uniquePermutations = windows.map { it.text }.toSet().size
        val classRuns = LinkedHashMap<String, MutableList<RotationRun>>()
        val transitions = ArrayList<WindowTransition>()
        val runs = ArrayList<RotationRun>()

        var currentStart = windows.first().start
        var currentEnd = windows.first().start + N
        var currentClass = windows.first().rotationClass
        var currentWindows = arrayListOf(windows.first())

        for (i in 1 until windows.size) {
            val previous = windows[i - 1]
            val window = windows[i]
            transitions.add(
                WindowTransition(
                    previous.start,
                    window.start,
                    window.start - previous.start,
                    previous.text,
                    window.text,
                    previous.rotationClass == window.rotationClass
                )
            )

            if (window.rotationClass == currentClass && window.start == previous.start + 1) {
                currentWindows.add(window)
                currentEnd = window.start + N
            } else {
                val run = RotationRun(currentClass, currentStart, currentEnd, currentWindows.map { it.text })
                runs.add(run)
                classRuns.getOrPut(currentClass) { ArrayList() }.add(run)
                currentStart = window.start
                currentEnd = window.start + N
                currentClass = window.rotationClass
                currentWindows = arrayListOf(window)
            }
        }

        val finalRun = RotationRun(currentClass, currentStart, currentEnd, currentWindows.map { it.text })
        runs.add(finalRun)
        classRuns.getOrPut(currentClass) { ArrayList() }.add(finalRun)

        return KnownBestAnalysis(
            source,
            windows,
            runs,
            transitions,
            classRuns,
            uniquePermutations,
            classRuns.size,
            classRuns.count { it.value.size > 1 }
        )
    }

    fun colorizeByRotationClass(analysis: KnownBestAnalysis, useAnsi: Boolean): String {
        val classIndex = analysis.classRuns.keys.withIndex().associate { it.value to it.index }
        val marks = Array(analysis.source.length) { "" }
        val text = StringBuilder()

        for (run in analysis.runs) {
            val color = if (useAnsi) colors[classIndex.getValue(run.rotationClass) % colors.size] else ""
            for (i in run.start until max(run.start + 1, run.end)) {
                if (i in marks.indices) {
                    marks[i] = color
                }
            }
        }

        var lastColor = ""
        for (i in analysis.source.indices) {
            val color = marks[i]
            if (color != lastColor) {
                if (useAnsi && lastColor.isNotEmpty()) text.append(RESET)
                if (useAnsi && color.isNotEmpty()) text.append(color)
                lastColor = color
            }
            text.append(analysis.source[i])
        }
        if (useAnsi && lastColor.isNotEmpty()) text.append(RESET)
        return text.toString()
    }

    private fun permutationWindows(source: String): List<PermutationWindow> {
        val result = ArrayList<PermutationWindow>()
        for (start in 0..source.length - N) {
            val text = source.substring(start, start + N)
            if (isPermutation(text)) {
                result.add(PermutationWindow(start, text, canonicalRotation(text)))
            }
        }
        return result
    }

    private fun isPermutation(text: String): Boolean {
        if (text.length != N) return false
        val seen = BooleanArray(N)
        for (ch in text) {
            val index = ch - '1'
            if (index !in 0 until N || seen[index]) return false
            seen[index] = true
        }
        return true
    }

    private fun canonicalRotation(text: String): String {
        var best = text
        for (i in 1 until text.length) {
            val rotated = text.substring(i) + text.substring(0, i)
            if (rotated < best) best = rotated
        }
        return best
    }
}

data class KnownBestAnalysis(
    val source: String,
    val windows: List<PermutationWindow>,
    val runs: List<RotationRun>,
    val transitions: List<WindowTransition>,
    val classRuns: Map<String, List<RotationRun>>,
    val uniquePermutations: Int,
    val classCount: Int,
    val splitClassCount: Int
)

data class PermutationWindow(
    val start: Int,
    val text: String,
    val rotationClass: String
)

data class RotationRun(
    val rotationClass: String,
    val start: Int,
    val end: Int,
    val windows: List<String>
) {
    val windowCount: Int = windows.size
    val textLength: Int = end - start

    fun describe(): String {
        return "$start-$end len=$textLength windows=$windowCount class=$rotationClass text=${windows.joinToString(",")}"
    }
}

data class WindowTransition(
    val fromStart: Int,
    val toStart: Int,
    val step: Int,
    val from: String,
    val to: String,
    val sameClass: Boolean
) {
    fun describe(): String {
        val kind = if (sameClass) "same" else "switch"
        return "$fromStart->$toStart step=$step $kind $from -> $to"
    }
}
