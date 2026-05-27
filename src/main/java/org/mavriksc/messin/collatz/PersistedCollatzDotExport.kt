package org.mavriksc.messin.collatz

import java.io.File
import java.util.ArrayDeque

private const val DEFAULT_DOT_GRAPH_DIR = "colatz-data"
private const val DEFAULT_DOT_OUTPUT = "graph.dot"
private const val DEFAULT_INCLUDE_EXTERNAL = false
private const val COLOR_START_NODE = 4L
private val FULL_GRAPH_COLOR_PALETTE = listOf(
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
    val graphDir = File(System.getProperty("collatz.graph.dir") ?: DEFAULT_DOT_GRAPH_DIR)
    val outputFile = File(graphDir, System.getProperty("collatz.dot.file") ?: DEFAULT_DOT_OUTPUT)
    val includeExternal = System.getProperty("collatz.dot.includeExternal")?.toBooleanStrictOrNullCompat() ?: DEFAULT_INCLUDE_EXTERNAL
    val nodes = loadPersistedGraphNodes(graphDir)
    val knownNumbers = nodes.mapTo(mutableSetOf()) { it.number }
    val colorIndexes = computeFullGraphColorIndexes(nodes)

    outputFile.parentFile?.mkdirs()
    outputFile.writeText(
        buildString {
            appendLineCompat("digraph collatz {")
            appendLineCompat("  rankdir=LR;")
            appendLineCompat("  splines=ortho;")
            appendLineCompat("  ranksep=1.35;")
            appendLineCompat("  nodesep=0.5;")
            appendLineCompat("  outputorder=edgesfirst;")
            appendLineCompat("  node [shape=box, style=filled, fontname=\"Courier New\", fontsize=10];")

            nodes.groupBy { it.distanceFromRoot }
                .toSortedMap()
                .forEach { (_, distanceNodes) ->
                    appendLineCompat("  { rank=same;")
                    distanceNodes.sortedBy { it.number }.forEach { node ->
                        appendLineCompat("    ${node.number};")
                    }
                    appendLineCompat("  }")
                }

            nodes.forEach { node ->
                val colorIndex = colorIndexes[node.number]
                val fillColor = if (colorIndex == null) "#e5e7eb" else FULL_GRAPH_COLOR_PALETTE[colorIndex]
                appendLineCompat(
                    "  ${node.number} [label=\"${node.number}\\nd=${node.distanceFromRoot} h=${node.towerHeight} f=${node.factorCount}\" fillcolor=\"$fillColor\"];"
                )
            }

            nodes.forEach { node ->
                emitEdge(this, node.number, node.definiteConnectedNumber, "2n", "solid", knownNumbers, includeExternal)
                node.optionalConnectedNumber?.let {
                    emitEdge(this, node.number, it, "opt", "dashed", knownNumbers, includeExternal)
                }
            }

            appendLineCompat("}")
        }
    )

    println("Graphviz dot export")
    println("Graph directory: ${graphDir.path}")
    println("Node count: ${nodes.size}")
    println("Output: ${outputFile.path}")
    println("Include external stubs: $includeExternal")
    println("Color start node: $COLOR_START_NODE")
}

private fun computeFullGraphColorIndexes(nodes: List<PersistedCollatzNode>): Map<Long, Int> {
    val nodesByNumber = nodes.associateBy { it.number }
    val colorIndexes = mutableMapOf<Long, Int>()
    val queue = ArrayDeque<Long>()

    if (nodesByNumber.containsKey(COLOR_START_NODE)) {
        colorIndexes[COLOR_START_NODE] = 0
        queue.add(COLOR_START_NODE)
    }

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentColor = colorIndexes[current] ?: 0
        val node = nodesByNumber[current] ?: continue
        val children = mutableListOf<Long>()
        run {
            if (nodesByNumber.containsKey(node.definiteConnectedNumber) && node.definiteConnectedNumber >= COLOR_START_NODE) {
                children.add(node.definiteConnectedNumber)
            }
            val optional = node.optionalConnectedNumber
            if (optional != null && nodesByNumber.containsKey(optional) && optional >= COLOR_START_NODE) {
                children.add(optional)
            }
        }

        children.distinct().sorted().forEachIndexed { index, child ->
            if (!colorIndexes.containsKey(child)) {
                colorIndexes[child] = (currentColor + index + 1) % FULL_GRAPH_COLOR_PALETTE.size
                queue.add(child)
            }
        }
    }

    return colorIndexes
}

private fun emitEdge(
    builder: StringBuilder,
    source: Long,
    target: Long,
    label: String,
    style: String,
    knownNumbers: Set<Long>,
    includeExternal: Boolean,
    constrain: Boolean = true
) {
    if (knownNumbers.contains(target)) {
        builder.appendLineCompat("  $source -> $target [label=\"$label\", style=$style, constraint=${if (constrain) "true" else "false"}];")
        return
    }

    if (includeExternal) {
        val externalId = "external_$target"
        builder.appendLineCompat("  $externalId [label=\"$target\", shape=ellipse, style=dotted];")
        builder.appendLineCompat("  $source -> $externalId [label=\"$label\", style=$style, constraint=${if (constrain) "true" else "false"}];")
    }
}

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}

private fun String.toBooleanStrictOrNullCompat(): Boolean? = when (trim().lowercase()) {
    "true" -> true
    "false" -> false
    else -> null
}
