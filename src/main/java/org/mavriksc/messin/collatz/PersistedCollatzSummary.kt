package org.mavriksc.messin.collatz

import java.io.File

private const val DEFAULT_SUMMARY_GRAPH_DIR = "colatz-data"
private const val DEFAULT_SUMMARY_OUTPUT = "summary.txt"
private const val DEFAULT_EXPORT_SUMMARY_CSVS = true

data class AttachmentSummary(
    val sourceOddCore: Long,
    val destinationOddCore: Long,
    val count: Int
)

fun main() {
    val graphDir = File(System.getProperty("collatz.graph.dir") ?: DEFAULT_SUMMARY_GRAPH_DIR)
    val outputFile = File(graphDir, System.getProperty("collatz.summary.file") ?: DEFAULT_SUMMARY_OUTPUT)
    val exportSummaryCsvs =
        System.getProperty("collatz.summary.exportCsvs")?.toBooleanStrictOrNullCompat() ?: DEFAULT_EXPORT_SUMMARY_CSVS

    val nodes = loadPersistedGraphNodes(graphDir)
    val metadata = loadPersistedGraphMetadata(graphDir)
    val optionalNodes = nodes.filter { it.optionalConnectedNumber != null }

    val summaryText = buildString {
        appendLineCompat("Persisted Collatz graph summary")
        appendLineCompat("graphDir=${graphDir.path}")
        appendLineCompat("nodeCount=${nodes.size}")
        appendLineCompat("maxSeedProcessed=${metadata.maxSeedProcessed}")
        appendLineCompat("optionalAttachmentCount=${optionalNodes.size}")
        appendLineCompat("")

        appendLineCompat("Tower height distribution:")
        nodes.groupBy { it.towerHeight }
            .entries
            .sortedBy { it.key }
            .forEach { appendLineCompat("  h=${it.key} count=${it.value.size}") }
        appendLineCompat("")

        appendLineCompat("Factor count distribution:")
        nodes.groupBy { it.factorCount }
            .entries
            .sortedBy { it.key }
            .forEach { appendLineCompat("  factors=${it.key} count=${it.value.size}") }
        appendLineCompat("")

        appendLineCompat("Top distances from root:")
        nodes.sortedByDescending { it.distanceFromRoot }
            .take(15)
            .forEach {
                appendLineCompat(
                    "  n=${it.number} d=${it.distanceFromRoot} h=${it.towerHeight} factors=${it.factorCount} pf=${it.primeFactorization}"
                )
            }
        appendLineCompat("")

        appendLineCompat("Top tower-to-tower odd attachments:")
        computeAttachmentSummaries(optionalNodes)
            .take(15)
            .forEach {
                appendLineCompat("  ${it.sourceOddCore} -> ${it.destinationOddCore} count=${it.count}")
            }
        appendLineCompat("")

        appendLineCompat("Destination towers with most odd attachments:")
        optionalNodes
            .groupBy { towerInfo(it.number).oddCore }
            .entries
            .sortedByDescending { it.value.size }
            .take(15)
            .forEach {
                val attachments = it.value.mapNotNull { row -> row.optionalConnectedNumber }.take(8).joinToString(", ")
                appendLineCompat("  tower=${it.key} count=${it.value.size} examples=[$attachments]")
            }
    }

    outputFile.parentFile?.mkdirs()
    outputFile.writeText(summaryText)

    if (exportSummaryCsvs) {
        File(graphDir, "tower-height-summary.csv").writeText(renderTowerHeightSummaryCsv(nodes))
        File(graphDir, "attachment-summary.csv").writeText(renderAttachmentSummaryCsv(computeAttachmentSummaries(optionalNodes)))
    }

    println(summaryText)
    println()
    println("Summary output: ${outputFile.path}")
}

fun computeAttachmentSummaries(nodes: List<PersistedCollatzNode>): List<AttachmentSummary> =
    nodes.groupBy {
        val source = it.optionalConnectedNumber ?: 0L
        towerInfo(source).oddCore to towerInfo(it.number).oddCore
    }
        .entries
        .map { AttachmentSummary(it.key.first, it.key.second, it.value.size) }
        .sortedByDescending { it.count }

private fun renderTowerHeightSummaryCsv(nodes: List<PersistedCollatzNode>): String =
    buildString {
        appendLineCompat("towerHeight,count")
        nodes.groupBy { it.towerHeight }
            .entries
            .sortedBy { it.key }
            .forEach { appendLineCompat("${it.key},${it.value.size}") }
    }

private fun renderAttachmentSummaryCsv(rows: List<AttachmentSummary>): String =
    buildString {
        appendLineCompat("sourceOddCore,destinationOddCore,count")
        rows.forEach { appendLineCompat("${it.sourceOddCore},${it.destinationOddCore},${it.count}") }
    }

private fun StringBuilder.appendLineCompat(value: String) {
    append(value)
    append('\n')
}

private fun String.toBooleanStrictOrNullCompat(): Boolean? = when (trim().toLowerCase()) {
    "true" -> true
    "false" -> false
    else -> null
}
