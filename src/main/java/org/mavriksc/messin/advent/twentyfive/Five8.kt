package org.mavriksc.messin.advent.twentyfive

import org.mavriksc.messin.random.readFile
import kotlin.math.sqrt

fun main() {
    val sampleOrInput = "input"
    val input = "advent/five/${sampleOrInput}8.txt".readFile()
    var minx = Long.MAX_VALUE
    var maxx = Long.MIN_VALUE
    var miny = Long.MAX_VALUE
    var maxy = Long.MIN_VALUE
    var minz = Long.MAX_VALUE
    var maxz = Long.MIN_VALUE
    val points = mutableListOf<ThreeDPos>()
    input.map { line -> line.split(",").map { it.toLong() } }
        .forEach {
            points.add(ThreeDPos(it[0], it[1], it[2]))
            minx = minOf(minx, it[0])
            maxx = maxOf(maxx, it[0])
            miny = minOf(miny, it[1])
            maxy = maxOf(maxy, it[1])
            minz = minOf(minz, it[2])
            maxz = maxOf(maxz, it[2])
        }
    val root = OctTree(ThreeDBounds(minx, maxx, miny, maxy, minz, maxz))
    root.addAll(points)
    val connections = mutableSetOf<Pair<ThreeDPos, ThreeDPos>>()
    do {
        val shortest = root.leaves().flatMap { leaf ->
            leaf.points.map { current ->
                var closest: ThreeDPos? = null
                var closestDistSquared = Long.MAX_VALUE
                root.queryBounds(leaf.bounds.lookAround())
                    .filter {
                        it != current && !connections.contains(
                            Pair(
                                current,
                                it
                            )
                        ) && !connections.contains(Pair(it, current))
                    }
                    .forEach { other ->
                        val distSquared = current.distanceSquared(other)
                        if (distSquared < closestDistSquared) {
                            closest = other
                            closestDistSquared = distSquared
                        }
                    }
                Pair(Pair(current, closest!!), closestDistSquared)
            }
        }.minBy { it.second }
        connections.add(shortest.first)
    } while (connections.size < 1000)
    val networks = mutableSetOf<Node>()
    connections.forEach { pair ->
        val nodes = networks.filter { it.contains(pair.first) || it.contains(pair.second) }
        when (nodes.size) {
            2 -> {
                nodes[0].join(nodes[1])
                networks.remove(nodes[1])
            }

            1 -> {
                nodes[0].connect(pair.first)
                nodes[0].connect(pair.second)
            }

            0 -> {
                val newNode = Node(pair.first)
                newNode.connect(pair.second)
                networks.add(newNode)
            }
        }
    }
    val connectedPoints = networks.flatMap { it.points() }.toSet()
    val networkSizes = networks.map { it.size() } + List(points.count { it !in connectedPoints }) { 1 }
    println(networkSizes.sortedDescending().take(3).reduce { a, b -> a * b })
}


private data class Node(val point: ThreeDPos) {
    private val connected = mutableSetOf(point)
    fun connect(other: ThreeDPos) {
        connected.add(other)
    }

    fun join(other: Node) {
        connected.addAll(other.connected)
    }

    fun contains(other: ThreeDPos) = connected.contains(other)
    fun size() = connected.size
    fun points() = connected.toSet()
}


data class ThreeDPos(val x: Long, val y: Long, val z: Long) {
    fun difference(other: ThreeDPos) = ThreeDPos(x - other.x, y - other.y, z - other.z)

    fun magnitude() = sqrt((x * x + y * y + z * z).toDouble())

    fun magsq() = x * x + y * y + z * z

    fun distanceTo(other: ThreeDPos) = difference(other).magnitude()

    fun distanceSquared(other: ThreeDPos) = difference(other).magsq()
}

data class ThreeDBounds(
    val xmin: Long,
    val xmax: Long,
    val ymin: Long,
    val ymax: Long,
    val zmin: Long,
    val zmax: Long
) {
    fun contains(point: ThreeDPos): Boolean {
        return point.x in xmin..xmax && point.y in ymin..ymax && point.z in zmin..zmax
    }

    fun intersects(bounds: ThreeDBounds): Boolean {
        return this.xmin <= bounds.xmax && this.xmax >= bounds.xmin &&
                this.ymin <= bounds.ymax && this.ymax >= bounds.ymin &&
                this.zmin <= bounds.zmax && this.zmax >= bounds.zmin
    }

    fun lookAround(): ThreeDBounds {
        val xSize = xmax - xmin
        val ySize = ymax - ymin
        val zSize = zmax - zmin
        return ThreeDBounds(xmin - xSize, xmax + xSize, ymin - ySize, ymax + ySize, zmin - zSize, zmax + zSize)
    }
}

private class OctTree(
    val bounds: ThreeDBounds
) {
    val maxSize = 10
    val children: Lazy<Array<OctTree>> = lazy { doChildren() }
    val points = mutableListOf<ThreeDPos>()

    fun addPoint(point: ThreeDPos) {
        if (!this.bounds.contains(point)) return
        if (!children.isInitialized() && points.size < maxSize)
            points.add(point)
        else {
            if (!children.isInitialized()) {
                children.value.forEach { it.addAll(points) }
                points.clear()
            }
            children.value.first { it.bounds.contains(point) }.addPoint(point)
        }
    }

    fun addAll(points: List<ThreeDPos>) {
        points.forEach { addPoint(it) }
    }

    fun queryBounds(other: ThreeDBounds): List<ThreeDPos> {
        return if (!this.bounds.intersects(other)) emptyList()
        else if (children.isInitialized()) children.value.flatMap { it.queryBounds(other) }
        else points.filter { other.contains(it) }
    }

    fun leaves(): List<OctTree> {
        return if (children.isInitialized()) children.value.flatMap { it.leaves() } else listOf(this)
    }

    private fun doChildren(): Array<OctTree> {
        with(bounds) {
            require(xmin < xmax && ymin < ymax && zmin < zmax) {
                "Cannot subdivide bounds smaller than 8 unit volume: $bounds"
            }
            return arrayOf(
                OctTree(
                    ThreeDBounds(
                        xmin,
                        (xmin + xmax) / 2,
                        ymin,
                        (ymin + ymax) / 2,
                        zmin,
                        (zmin + zmax) / 2
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        (xmin + xmax) / 2 + 1,
                        xmax,
                        ymin,
                        (ymin + ymax) / 2,
                        zmin,
                        (zmin + zmax) / 2
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        xmin,
                        (xmin + xmax) / 2,
                        (ymin + ymax) / 2 + 1,
                        ymax,
                        zmin,
                        (zmin + zmax) / 2
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        (xmin + xmax) / 2 + 1,
                        xmax,
                        (ymin + ymax) / 2 + 1,
                        ymax,
                        zmin,
                        (zmin + zmax) / 2
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        xmin,
                        (xmin + xmax) / 2,
                        ymin,
                        (ymin + ymax) / 2,
                        (zmin + zmax) / 2 + 1,
                        zmax
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        (xmin + xmax) / 2 + 1,
                        xmax,
                        ymin,
                        (ymin + ymax) / 2,
                        (zmin + zmax) / 2 + 1,
                        zmax
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        xmin,
                        (xmin + xmax) / 2,
                        (ymin + ymax) / 2 + 1,
                        ymax,
                        (zmin + zmax) / 2 + 1,
                        zmax
                    )
                ),
                OctTree(
                    ThreeDBounds(
                        (xmin + xmax) / 2 + 1,
                        xmax,
                        (ymin + ymax) / 2 + 1,
                        ymax,
                        (zmin + zmax) / 2 + 1,
                        zmax
                    )

                )
            )
        }
    }
}
