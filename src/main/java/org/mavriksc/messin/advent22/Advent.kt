package org.mavriksc.messin.advent22


fun String.readFile() = ClassLoader.getSystemResourceAsStream(this)?.bufferedReader()?.readLines()
fun main() {
    //dayOnePuzzleOne()
    //dayOnePuzzleTwo()
    //dayTwoPuzzleOne()
    //dayTwoPuzzleTwo()
    //dayThreePuzzleOne()
    //dayThreePuzzleTwo()
    //dayFourPuzzleOne()
    //dayFourPuzzleTwo()
    dayFivePuzzleOne()
}

fun dayFivePuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_5_input.txt".readFile()!!
    var i = 0
    while (!lines[i].contains('1')) {
        i++
    }
    val floorIndex = i
    val numStacks = lines[floorIndex].trim().split(' ').last().toInt()
    val stacks = (0 until numStacks).map { mutableListOf<Char>() }.toList()
    stacks.forEachIndexed { index, chars ->
        val col = index * 4 + 1
        (floorIndex - 1 downTo 0).forEach { row ->
            if (col < lines[row].length && lines[row].elementAt(col) != ' ')
                chars.add(lines[row].elementAt(col))
        }
    }
    (floorIndex + 2 until lines.size).forEach { index ->
        val split = lines[index].split(' ')
        val boxes = split[1].toInt() - 1
        val from = split[3].toInt() - 1
        val to = split[5].toInt() - 1
        // add sublist of length "boxes" of the end of "from"  reversed to end of "to"
        // puzzle 2 for this day just needs to not reverse the list
        stacks[to].addAll(stacks[from].slice(stacks[from].lastIndex - boxes..stacks[from].lastIndex))
        stacks[from].subList(stacks[from].lastIndex - boxes, stacks[from].size).clear()
    }
    stacks.forEach { print(it.last()) }
}

fun dayFourPuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_4_input.txt".readFile()!!
    println(lines.map {
        val ranges = it.split(',')
        val e1 = ranges[0].split('-')
        val e1r = (e1[0].toInt()..e1[1].toInt()).toSet()
        val e2 = ranges[1].split('-')
        val e2r = (e2[0].toInt()..e2[1].toInt()).toSet()
        if (e1r.intersect(e2r).isNotEmpty()) 1 else 0
    }.sum())
}

fun dayFourPuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_4_input.txt".readFile()!!
    println(lines.map {
        val ranges = it.split(',')
        val e1 = ranges[0].split('-')
        val e1r = (e1[0].toInt()..e1[1].toInt()).toSet()
        val e2 = ranges[1].split('-')
        val e2r = (e2[0].toInt()..e2[1].toInt()).toSet()
        if (e1r.containsAll(e2r) || e2r.containsAll(e1r)) 1 else 0
    }.sum())
}

fun dayThreePuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_3_input.txt".readFile()!!
    println(lines.chunked(3).map {
        val e1 = it[0].toSet()
        val e2 = it[1].toSet()
        val e3 = it[2].toSet()
        val item = e1.intersect(e2).intersect(e3).first().toInt()
        if (item > 90) item - 96 else item - 38
    }.sum())
}

fun dayThreePuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_3_input.txt".readFile()!!
    println(lines.map {
        val comp1 = it.substring(0, it.length / 2).toSet()
        val comp2 = it.substring(it.length / 2).toSet()
        val intersect = comp1.intersect(comp2).first().toInt()
        if (intersect > 90) intersect - 96 else intersect - 38
    }.sum())

}

fun dayTwoPuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_2_input.txt".readFile()
    var points = 0
    lines?.forEach {
        points += when (it.first()) {
            'A' -> when (it.last()) { // ROCK
                'X' -> 3 + 0 // LOSS
                'Y' -> 1 + 3 // DRAW
                'Z' -> 2 + 6 // WIN
                else -> 0
            }

            'B' -> when (it.last()) { // PAPER
                'X' -> 1 + 0 // LOSS
                'Y' -> 2 + 3 // DRAW
                'Z' -> 3 + 6 // WIN
                else -> 0
            }

            'C' -> when (it.last()) { //SCISSORS
                'X' -> 2 + 0 // LOSS
                'Y' -> 3 + 3 // DRAW
                'Z' -> 1 + 6 // WIN
                else -> 0
            }

            else -> 0
        }

    }
    println(points)
}

fun dayTwoPuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_2_input.txt".readFile()
    var points = 0
    lines?.forEach {
        points += pointsForLine(it)
    }
    println(points)

}

fun pointsForLine(line: String): Int {
    // decrypt ?
    when (line.first()) {
        'A' -> when (line.last()) { // ROCK
            'X' -> return 1 + 3 // ROCK
            'Y' -> return 2 + 6 // PAPER
            'Z' -> return 3 + 0 //SCISSORS
        }

        'B' -> when (line.last()) { // PAPER
            'X' -> return 1 + 0 // ROCK
            'Y' -> return 2 + 3 // PAPER
            'Z' -> return 3 + 6 //SCISSORS
        }

        'C' -> when (line.last()) { //SCISSORS
            'X' -> return 1 + 6 // ROCK
            'Y' -> return 2 + 0 // PAPER
            'Z' -> return 3 + 3 //SCISSORS
        }
    }
    return 0
}

fun dayOnePuzzleOne() {
    val lines = "advent/adventofcode.com_2022_day_1_input.txt".readFile()
    val cals = mutableListOf<Int>(0)

    lines?.forEach {
        if (it.isEmpty())
            cals.add(0)
        else {
            // add to last element in cals
            cals[cals.lastIndex] = cals.last() + it.toInt()
        }
    }
    println(cals.max())
}

fun dayOnePuzzleTwo() {
    val lines = "advent/adventofcode.com_2022_day_1_input.txt".readFile()
    val top = mutableListOf(0, 0, 0)
    var curSum = 0
    lines?.forEach {
        if (it.isEmpty()) {
            pushToList(curSum, top)
            curSum = 0
        } else {
            curSum += it.toInt()
        }
    }
    println(top.slice((0..2)).sum())
}

fun pushToList(curSum: Int, top: MutableList<Int>) {
    run breaking@{
        (0..2).forEach {
            if (curSum > top[it]) {
                top.add(it, curSum)
                return@breaking
            }
        }
    }
}
