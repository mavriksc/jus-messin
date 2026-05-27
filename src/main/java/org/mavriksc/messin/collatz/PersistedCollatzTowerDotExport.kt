package org.mavriksc.messin.collatz

import java.io.File
import java.util.ArrayDeque

private const val DEFAULT_TOWER_GRAPH_DIR = "colatz-data"
private const val DEFAULT_TOWER_DOT_OUTPUT = "tower-graph.dot"
private const val DEFAULT_TOWER_CSV_OUTPUT = "tower-transitions-only.csv"
private const val DEFAULT_MIN_EDGE_COUNT = 1
private const val DEFAULT_TOWER_SNAPSHOT_OUTPUT = "tower-node-heights.csv"
private val BRANCH_COLOR_PALETTE = listOf(
    "#1f77b4",
    "#ff7f0e",
    "#2ca02c",
    "#d62728",
    "#9467bd",
    "#8c564b",
    "#e377c2",
    "#7f7f7f",
    "#bcbd22",
    "#17becf",
    "#4e79a7",
    "#f28e2b",
    "#59a14f"
)

fun main() {
    val graphDir = File(System.getProperty("collatz.graph.dir") ?: DEFAULT_TOWER_GRAPH_DIR)
    val dotFile = File(graphDir, System.getProperty("collatz.tower.dot.file") ?: DEFAULT_TOWER_DOT_OUTPUT)
    val csvFile = File(graphDir, System.getProperty("collatz.tower.csv.file") ?: DEFAULT_TOWER_CSV_OUTPUT)
    val nodeCsvFile = File(graphDir, System.getProperty("collatz.tower.nodeCsv.file") ?: DEFAULT_TOWER_SNAPSHOT_OUTPUT)
    val minEdgeCount = System.getProperty("collatz.tower.minEdgeCount")?.toInt() ?: DEFAULT_MIN_EDGE_COUNT

    val nodes = loadPersistedGraphNodes(graphDir)
    val attachmentNodes = nodes.filter { it.optionalConnectedNumber != null }
    val transitions = computeAttachmentSummaries(attachmentNodes)
        .filter { it.count >= minEdgeCount }
    val towers = transitions.flatMap { listOf(it.sourceOddCore, it.destinationOddCore) }.toSortedSet()
    val towerHeights = computeTowerAttachmentHeights(attachmentNodes)
    val branchDepths = computeBranchDepths(transitions, towers)

    nodeCsvFile.writeText(
        buildString {
            appendLineCompat("towerOddCore,maxAttachmentHeight,branchDepth,colorIndex")
            towers.forEach { tower ->
                val branchDepth = branchDepths[tower] ?: 0
                appendLineCompat("$tower,${towerHeights[tower] ?: 0},$branchDepth,${branchDepth % BRANCH_COLOR_PALETTE.size}")
            }
        }
    )

    csvFile.writeText(
        buildString {
            appendLineCompat("sourceOddCore,destinationOddCore,count")
            transitions.forEach { appendLineCompat("${it.sourceOddCore},${it.destinationOddCore},${it.count}") }
        }
    )

    dotFile.writeText(
        buildString {
            appendLineCompat("digraph collatz_towers {")
            appendLineCompat("  rankdir=LR;")
            appendLineCompat("  node [shape=ellipse, style=filled, fontname=\"Courier New\", fontsize=11];")
            towers.forEach {
                val attachmentHeight = towerHeights[it] ?: 0
                val branchDepth = branchDepths[it] ?: 0
                val fillColor = BRANCH_COLOR_PALETTE[branchDepth % BRANCH_COLOR_PALETTE.size]
                appendLineCompat(
                    "  $it [label=\"tower $it\\nattach h=$attachmentHeight\\nbranch d=$branchDepth\", fillcolor=\"$fillColor\"];"
                )
            }
            transitions.forEach {
                appendLineCompat("  ${it.sourceOddCore} -> ${it.destinationOddCore} [label=\"${it.count}\"];")
            }
            appendLineCompat("}")
        }
    )

    println("Tower-only graph export")
    println("Graph directory: ${graphDir.path}")
    println("Tower count: ${towers.size}")
    println("Transition count: ${transitions.size}")
    println("DOT output: ${dotFile.path}")
    println("CSV output: ${csvFile.path}")
    println("Tower height CSV: ${nodeCsvFile.path}")
}

private fun computeBranchDepths(
    transitions: List<AttachmentSummary>,
    towers: Set<Long>
): Map<Long, Int> {
    if (towers.isEmpty()) {
        return emptyMap()
    }

    val incomingCounts = transitions.groupingBy { it.destinationOddCore }.eachCount()
    val reverseEdges = transitions.groupBy { it.destinationOddCore }.mapValues { entry -> entry.value.map { it.sourceOddCore } }
    val branchDepths = mutableMapOf<Long, Int>()
    val queue = ArrayDeque<Long>()

    branchDepths[1L] = 0
    queue.add(1L)

    while (queue.isNotEmpty()) {
        val destination = queue.removeFirst()
        val destinationDepth = branchDepths[destination] ?: 0
        val branchIncrement = if ((incomingCounts[destination] ?: 0) > 1) 1 else 0
        reverseEdges[destination].orEmpty().sorted().forEach { source ->
            val sourceDepth = destinationDepth + branchIncrement
            val existing = branchDepths[source]
            if (existing == null || sourceDepth < existing) {
                branchDepths[source] = sourceDepth
                queue.add(source)
            }
        }
    }

    towers.forEach { tower ->
        if (!branchDepths.containsKey(tower)) {
            branchDepths[tower] = 0
        }
    }

    return branchDepths
}

private fun computeTowerAttachmentHeights(nodes: List<PersistedCollatzNode>): Map<Long, Int> =
    nodes.groupBy { towerInfo(it.number).oddCore }
        .mapValues { entry -> entry.value.map { it.towerHeight }.max() ?: 0 }

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}
