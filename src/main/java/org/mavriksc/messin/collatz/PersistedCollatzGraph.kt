package org.mavriksc.messin.collatz

import java.io.File
import java.math.BigInteger
import java.util.Properties

private const val DEFAULT_GRAPH_MAX_SEED = 1_000L
private const val DEFAULT_GRAPH_DIR = "colatz-data"
private const val DEFAULT_APPEND_MODE = true
private const val DEFAULT_EXPORT_SORTED_SNAPSHOT = false
private const val GRAPH_ROOT = 1L
private const val GRAPH_SCHEMA_VERSION = 1
private const val NODES_FILE_NAME = "nodes.csv"
private const val META_FILE_NAME = "meta.properties"
private const val SORTED_SNAPSHOT_FILE_NAME = "nodes-sorted.csv"

data class GraphMetadata(
    val maxSeedProcessed: Long,
    val nodeCount: Int,
    val rootValue: Long,
    val schemaVersion: Int
)

fun main() {
    val maxSeed = System.getProperty("collatz.graph.maxSeed")?.toLong() ?: DEFAULT_GRAPH_MAX_SEED
    val graphDir = File(System.getProperty("collatz.graph.dir") ?: DEFAULT_GRAPH_DIR)
    val appendMode = System.getProperty("collatz.graph.append")?.toBooleanStrictOrNullCompat() ?: DEFAULT_APPEND_MODE
    val exportSortedSnapshot =
        System.getProperty("collatz.graph.exportSortedSnapshot")?.toBooleanStrictOrNullCompat()
            ?: DEFAULT_EXPORT_SORTED_SNAPSHOT

    val state = if (appendMode) {
        loadGraphState(graphDir)
    } else {
        GraphState(mutableMapOf(), GraphMetadata(0L, 0, GRAPH_ROOT, GRAPH_SCHEMA_VERSION))
    }

    val nodes = state.nodesByNumber
    val startSeed = if (appendMode) state.metadata.maxSeedProcessed + 1L else 1L
    val normalizedStartSeed = maxOf(1L, startSeed)
    var processedSeedCount = 0
    val appendedNodes = mutableListOf<PersistedCollatzNode>()

    ensureRootPresent(nodes)
    if (state.metadata.nodeCount == 0 && nodes.containsKey(GRAPH_ROOT)) {
        appendedNodes += nodes.getValue(GRAPH_ROOT)
    }

    if (normalizedStartSeed <= maxSeed) {
        for (seed in normalizedStartSeed..maxSeed) {
            processedSeedCount++
            if (nodes.containsKey(seed)) {
                continue
            }

            val unknownPath = collectUnknownPath(seed, nodes)
            if (unknownPath.isEmpty()) {
                continue
            }

            val tailDistance = knownTailDistance(unknownPath.last(), nodes)
            var distance = tailDistance
            for (index in unknownPath.lastIndex downTo 0) {
                distance += 1
                val value = unknownPath[index]
                if (!nodes.containsKey(value)) {
                    val node = buildPersistedNode(value, distance)
                    nodes[value] = node
                    appendedNodes += node
                }
            }
        }
    }

    val metadata = GraphMetadata(
        maxSeedProcessed = maxOf(state.metadata.maxSeedProcessed, maxSeed),
        nodeCount = nodes.size,
        rootValue = GRAPH_ROOT,
        schemaVersion = GRAPH_SCHEMA_VERSION
    )

    persistGraph(graphDir, nodes, appendedNodes, metadata, appendMode, exportSortedSnapshot)
    printGraphSummary(
        graphDir,
        maxSeed,
        normalizedStartSeed,
        processedSeedCount,
        appendedNodes.size,
        metadata,
        exportSortedSnapshot
    )
}

private data class GraphState(
    val nodesByNumber: MutableMap<Long, PersistedCollatzNode>,
    val metadata: GraphMetadata
)

fun loadPersistedGraphNodes(graphDir: File): List<PersistedCollatzNode> =
    loadGraphState(graphDir).nodesByNumber.values.sortedBy { it.number }

fun loadPersistedGraphMetadata(graphDir: File): GraphMetadata =
    loadGraphState(graphDir).metadata

private fun ensureRootPresent(nodes: MutableMap<Long, PersistedCollatzNode>) {
    if (!nodes.containsKey(GRAPH_ROOT)) {
        nodes[GRAPH_ROOT] = buildPersistedNode(GRAPH_ROOT, 0)
    }
}

private fun collectUnknownPath(seed: Long, knownNodes: Map<Long, PersistedCollatzNode>): List<Long> {
    val path = mutableListOf<Long>()
    var current = seed
    while (!knownNodes.containsKey(current)) {
        path += current
        current = collatzNext(current)
    }
    return path
}

private fun knownTailDistance(lastUnknown: Long, knownNodes: Map<Long, PersistedCollatzNode>): Int {
    val tail = collatzNext(lastUnknown)
    return knownNodes[tail]?.distanceFromRoot
        ?: error("Tail node $tail should already exist in the graph.")
}

private fun loadGraphState(graphDir: File): GraphState {
    val nodesFile = File(graphDir, NODES_FILE_NAME)
    val metaFile = File(graphDir, META_FILE_NAME)
    if (!nodesFile.exists() || !metaFile.exists()) {
        return GraphState(mutableMapOf(), GraphMetadata(0L, 0, GRAPH_ROOT, GRAPH_SCHEMA_VERSION))
    }

    val metadata = loadMetadata(metaFile)
    val nodesByNumber = loadNodes(nodesFile)
    return GraphState(nodesByNumber, metadata)
}

private fun loadMetadata(metaFile: File): GraphMetadata {
    val properties = Properties()
    metaFile.inputStream().use { properties.load(it) }
    return GraphMetadata(
        maxSeedProcessed = properties.getProperty("maxSeedProcessed", "0").toLong(),
        nodeCount = properties.getProperty("nodeCount", "0").toInt(),
        rootValue = properties.getProperty("rootValue", GRAPH_ROOT.toString()).toLong(),
        schemaVersion = properties.getProperty("schemaVersion", GRAPH_SCHEMA_VERSION.toString()).toInt()
    )
}

private fun loadNodes(nodesFile: File): MutableMap<Long, PersistedCollatzNode> {
    val lines = nodesFile.readLines()
    if (lines.isEmpty()) {
        return mutableMapOf()
    }

    return lines
        .drop(1)
        .filter { it.isNotBlank() }
        .map { parsePersistedNode(it) }
        .associateByTo(mutableMapOf()) { it.number }
}

private fun parsePersistedNode(line: String): PersistedCollatzNode {
    val cells = parseCsvLine(line)
    require(cells.size == 8) { "Expected 8 columns but found ${cells.size}: $line" }
    return PersistedCollatzNode(
        number = cells[0].toLong(),
        definiteConnectedNumber = cells[1].toLong(),
        optionalConnectedNumber = cells[2].takeIf { it.isNotBlank() }?.toLong(),
        distanceFromRoot = cells[3].toInt(),
        towerHeight = cells[4].toInt(),
        factorCount = cells[5].toInt(),
        primeFactorization = cells[6],
        primeNumberSystem = cells[7].toBigInteger()
    )
}

private fun persistGraph(
    graphDir: File,
    nodes: Map<Long, PersistedCollatzNode>,
    appendedNodes: List<PersistedCollatzNode>,
    metadata: GraphMetadata,
    appendMode: Boolean,
    exportSortedSnapshot: Boolean
) {
    graphDir.mkdirs()
    val nodesFile = File(graphDir, NODES_FILE_NAME)
    if (!appendMode || !nodesFile.exists()) {
        nodesFile.writeText(renderNodesCsv(nodes.values.sortedBy { it.number }))
    } else if (appendedNodes.isNotEmpty()) {
        nodesFile.appendText(renderNodeRows(appendedNodes.sortedBy { it.number }))
    }
    persistMetadata(File(graphDir, META_FILE_NAME), metadata)

    if (exportSortedSnapshot) {
        File(graphDir, SORTED_SNAPSHOT_FILE_NAME)
            .writeText(
                renderNodesCsv(
                    nodes.values.sortedWith(compareBy<PersistedCollatzNode>({ it.distanceFromRoot }, { it.number }))
                )
            )
    }
}

private fun persistMetadata(metaFile: File, metadata: GraphMetadata) {
    val properties = Properties()
    properties.setProperty("maxSeedProcessed", metadata.maxSeedProcessed.toString())
    properties.setProperty("nodeCount", metadata.nodeCount.toString())
    properties.setProperty("rootValue", metadata.rootValue.toString())
    properties.setProperty("schemaVersion", metadata.schemaVersion.toString())
    metaFile.outputStream().use { properties.store(it, "Persisted Collatz graph metadata") }
}

private fun renderNodesCsv(nodes: List<PersistedCollatzNode>): String =
    buildString {
        appendLineCompat(
            "number,definiteConnectedNumber,optionalConnectedNumber,distanceFromRoot," +
                "towerHeight,factorCount,primeFactorization,primeNumberSystem"
        )
        append(renderNodeRows(nodes))
    }

private fun renderNodeRows(nodes: List<PersistedCollatzNode>): String =
    buildString {
        nodes.forEach { node ->
            appendLineCompat(
                listOf(
                    node.number.toString(),
                    node.definiteConnectedNumber.toString(),
                    node.optionalConnectedNumber?.toString() ?: "",
                    node.distanceFromRoot.toString(),
                    node.towerHeight.toString(),
                    node.factorCount.toString(),
                    csv(node.primeFactorization),
                    node.primeNumberSystem.toString()
                ).joinToString(",")
            )
        }
    }

private fun printGraphSummary(
    graphDir: File,
    maxSeed: Long,
    startSeed: Long,
    processedSeedCount: Int,
    appendedNodeCount: Int,
    metadata: GraphMetadata,
    exportSortedSnapshot: Boolean
) {
    println("Persisted Collatz graph")
    println("Graph directory: ${graphDir.path}")
    println("Seed range processed this run: $startSeed..$maxSeed")
    println("Seeds examined this run: $processedSeedCount")
    println("New nodes appended this run: $appendedNodeCount")
    println("Graph node count: ${metadata.nodeCount}")
    println("Max seed processed: ${metadata.maxSeedProcessed}")
    println("Schema version: ${metadata.schemaVersion}")
    if (exportSortedSnapshot) {
        println("Sorted snapshot: ${File(graphDir, SORTED_SNAPSHOT_FILE_NAME).path}")
    }
}

private fun parseCsvLine(line: String): List<String> {
    val cells = mutableListOf<String>()
    val current = StringBuilder()
    var insideQuotes = false
    var index = 0
    while (index < line.length) {
        val ch = line[index]
        when {
            ch == '"' && insideQuotes && index + 1 < line.length && line[index + 1] == '"' -> {
                current.append('"')
                index++
            }
            ch == '"' -> insideQuotes = !insideQuotes
            ch == ',' && !insideQuotes -> {
                cells += current.toString()
                current.setLength(0)
            }
            else -> current.append(ch)
        }
        index++
    }
    cells += current.toString()
    return cells
}

private fun csv(value: String): String = "\"${value.replace("\"", "\"\"")}\""

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}

private fun String.toBooleanStrictOrNullCompat(): Boolean? = when (trim().toLowerCase()) {
    "true" -> true
    "false" -> false
    else -> null
}
