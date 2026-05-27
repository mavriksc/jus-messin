package org.mavriksc.messin.collatz

import java.io.File

private const val DEFAULT_FAMILY_GRAPH_DIR = "colatz-data"
private const val DEFAULT_FAMILY_DOT_OUTPUT = "tower-family-graph.dot"
private const val DEFAULT_FAMILY_CSV_OUTPUT = "tower-family-summary.csv"
private const val DEFAULT_MIN_FAMILY_LENGTH = 2

data class AttachmentFamily(
    val destinationOddCore: Long,
    val startSource: Long,
    val endSource: Long,
    val length: Int
)

fun main() {
    val graphDir = File(System.getProperty("collatz.graph.dir") ?: DEFAULT_FAMILY_GRAPH_DIR)
    val dotFile = File(graphDir, System.getProperty("collatz.family.dot.file") ?: DEFAULT_FAMILY_DOT_OUTPUT)
    val csvFile = File(graphDir, System.getProperty("collatz.family.csv.file") ?: DEFAULT_FAMILY_CSV_OUTPUT)
    val minFamilyLength = System.getProperty("collatz.family.minLength")?.toInt() ?: DEFAULT_MIN_FAMILY_LENGTH

    val nodes = loadPersistedGraphNodes(graphDir)
    val optionalNodes = nodes.filter { it.optionalConnectedNumber != null }
    val families = buildAttachmentFamilies(optionalNodes)
        .filter { it.length >= minFamilyLength }
        .sortedWith(compareBy<AttachmentFamily> { it.destinationOddCore }.thenBy { it.startSource })

    csvFile.writeText(
        buildString {
            appendLineCompat("destinationOddCore,startSource,endSource,length")
            families.forEach {
                appendLineCompat("${it.destinationOddCore},${it.startSource},${it.endSource},${it.length}")
            }
        }
    )

    val towers = families.map { it.destinationOddCore }.toSortedSet()
    dotFile.writeText(
        buildString {
            appendLineCompat("digraph collatz_tower_families {")
            appendLineCompat("  rankdir=LR;")
            appendLineCompat("  node [fontname=\"Courier New\", fontsize=11];")
            towers.forEach { appendLineCompat("  tower_$it [shape=ellipse, label=\"tower $it\"];") }
            families.forEachIndexed { index, family ->
                val familyId = "family_$index"
                val familyLabel = if (family.length == 1) {
                    "${family.startSource}"
                } else {
                    "${family.startSource} -> ... -> ${family.endSource}\\n4x+1 family len=${family.length}"
                }
                appendLineCompat("  $familyId [shape=box, label=\"$familyLabel\"];")
                appendLineCompat("  $familyId -> tower_${family.destinationOddCore} [label=\"attach\"];")
            }
            appendLineCompat("}")
        }
    )

    println("4x+1 family graph export")
    println("Graph directory: ${graphDir.path}")
    println("Family count: ${families.size}")
    println("DOT output: ${dotFile.path}")
    println("CSV output: ${csvFile.path}")
}

fun buildAttachmentFamilies(nodes: List<PersistedCollatzNode>): List<AttachmentFamily> =
    nodes.groupBy { towerInfo(it.number).oddCore }
        .flatMap { (destinationOddCore, rows) ->
            val sources = rows.mapNotNull { it.optionalConnectedNumber }.distinct().sorted()
            val sourceSet = sources.toSet()
            sources.filter { source ->
                val previous = previousFamilyValue(source)
                previous == null || !sourceSet.contains(previous)
            }.map { start ->
                buildFamily(destinationOddCore, start, sourceSet)
            }
        }

private fun buildFamily(destinationOddCore: Long, start: Long, sourceSet: Set<Long>): AttachmentFamily {
    var current = start
    var length = 1
    while (true) {
        val next = current * 4L + 1L
        if (!sourceSet.contains(next)) {
            return AttachmentFamily(
                destinationOddCore = destinationOddCore,
                startSource = start,
                endSource = current,
                length = length
            )
        }
        current = next
        length++
    }
}

private fun previousFamilyValue(value: Long): Long? =
    if (value > 1L && (value - 1L) % 4L == 0L) {
        ((value - 1L) / 4L).takeIf { it % 2L == 1L }
    } else {
        null
    }

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}
