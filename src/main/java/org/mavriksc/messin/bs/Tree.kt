package org.mavriksc.messin.bs

class Tree(val root: Node2) {
    fun insert(value: Int, current: Node2 = this.root) {
        if (value < current.value)
            if (current.left == null)
                current.left = Node2(value)
            else insert(value, current.left!!)
        else if (current.right == null) current.right = Node2(value)
        else insert(value, current.right!!)
    }

}

class Node2(val value: Int, var left: Node2? = null, var right: Node2? = null) {
    override fun toString(): String = "${this.value}\n  (${this.left ?: ""}, ${this.right ?: ""})\n"

    fun print(level: Int = 0, rows: MutableList<String> = mutableListOf(""), it: Node2 = this) {
        rows[level] += "${it.value}"
        if (rows.size <= level + 1) rows.add("")
        rows[level + 1] += "("
        left?.print(level + 1, rows, it.left!!)
        rows[level + 1] += ","
        right?.print(level + 1, rows, it.right!!)
        rows[level + 1] += ")"
        if (level == 0) println(rows.joinToString("\n"))
    }
}

fun main() {
    val list = List(30) { it }.shuffled()
    val tree = Tree(Node2(list[0]))
    list.drop(1).forEach { tree.insert(it) }
    tree.root.print()
}