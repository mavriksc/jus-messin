package org.mavriksc.messin.random

import processing.core.PApplet
import java.util.ArrayDeque

private const val BOARD_SIZE = 9
private const val ALL_VALUES_MASK = 0b111111111

fun main() {
    PApplet.main("org.mavriksc.messin.random.SudokuWaveFunctionCollapseSketch")
}

fun isValidSudoku(board: Array<CharArray>): Boolean {
    val rows = Array(BOARD_SIZE) { mutableSetOf<Char>() }
    val cols = Array(BOARD_SIZE) { mutableSetOf<Char>() }
    val boxes = Array(BOARD_SIZE) { mutableSetOf<Char>() }
    for (i in board.indices) {
        for (j in board[i].indices) {
            val c = board[i][j]
            if (c != '.') {
                if (rows[i].contains(c) || cols[j].contains(c) || boxes[i / 3 * 3 + j / 3].contains(c)) return false
                rows[i] += c
                cols[j] += c
                boxes[i / 3 * 3 + j / 3] += c
            }
        }
    }
    return true
}

class SudokuWaveFunctionCollapseSketch : PApplet() {
    private val board = arrayOf(
        "12..3....".toCharArray(),
        "4..5.....".toCharArray(),
        ".98.....3".toCharArray(),
        "5...6...4".toCharArray(),
        "...8.3..5".toCharArray(),
        "7...2...6".toCharArray(),
        "......2..".toCharArray(),
        "...419..8".toCharArray(),
        "....8..79".toCharArray(),
    )

    private lateinit var solver: SudokuWaveSolver
    private val margin = 36f
    private val boardPixels = 720f
    private val cellPixels = boardPixels / BOARD_SIZE

    override fun settings() {
        size(980, 820)
    }

    override fun setup() {
        solver = SudokuWaveSolver(board)
        frameRate(14f)
        textFont(createFont("Arial", 24f))
    }

    override fun draw() {
        background(246)
        solver.step()
        drawBoard()
        drawLegend()
    }

    private fun drawBoard() {
        val snapshot = solver.cells
        for (row in 0 until BOARD_SIZE) {
            for (col in 0 until BOARD_SIZE) {
                drawCell(row, col, snapshot[row][col])
            }
        }
        stroke(20)
        for (i in 0..BOARD_SIZE) {
            strokeWeight(if (i % 3 == 0) 4f else 1.4f)
            val p = margin + i * cellPixels
            line(margin, p, margin + boardPixels, p)
            line(p, margin, p, margin + boardPixels)
        }
    }

    private fun drawCell(row: Int, col: Int, cell: WaveCell) {
        val x = margin + col * cellPixels
        val y = margin + row * cellPixels
        val active = solver.activePeer?.row == row && solver.activePeer?.col == col
        val source = solver.activeSource?.row == row && solver.activeSource?.col == col
        val contradiction = solver.contradictionCell?.row == row && solver.contradictionCell?.col == col
        when {
            contradiction && frameCount % 8 < 4 -> fill(230f, 50f, 45f)
            active -> fill(82f, 154f, 232f)
            source -> fill(181f, 214f, 250f)
            else -> fill(255)
        }
        noStroke()
        rect(x, y, cellPixels, cellPixels)

        when {
            cell.value != 0 -> drawSolvedCell(x, y, cell)
            else -> drawPossibilities(x, y, cell)
        }
    }

    private fun drawSolvedCell(x: Float, y: Float, cell: WaveCell) {
        fill(if (cell.guessDependent) color(22, 145, 82) else color(15))
        textAlign(CENTER, CENTER)
        textSize(36f)
        text(cell.value.toString(), x + cellPixels / 2f, y + cellPixels / 2f - 1f)
    }

    private fun drawPossibilities(x: Float, y: Float, cell: WaveCell) {
        textAlign(CENTER, CENTER)
        textSize(15f)
        val mini = cellPixels / 3f
        for (value in 1..9) {
            val index = value - 1
            val cx = x + index % 3 * mini + mini / 2f
            val cy = y + index / 3 * mini + mini / 2f
            when {
                cell.hasCandidate(value) -> {
                    fill(25)
                    text(value.toString(), cx, cy)
                }
                cell.isGuessRemoved(value) -> {
                    fill(22f, 145f, 82f)
                    text(value.toString(), cx, cy)
                    stroke(22f, 145f, 82f)
                    strokeWeight(2f)
                    line(cx - 6f, cy - 6f, cx + 6f, cy + 6f)
                    line(cx + 6f, cy - 6f, cx - 6f, cy + 6f)
                    noStroke()
                }
            }
        }
    }

    private fun drawLegend() {
        val x = margin + boardPixels + 34f
        var y = margin + 14f
        fill(20)
        textAlign(LEFT, TOP)
        textSize(22f)
        text("Sudoku WFC", x, y)
        y += 44f

        textSize(15f)
        fill(20)
        text("Queue: ${solver.queueSize}", x, y)
        y += 24f
        text("Guesses: ${solver.guessDepth}", x, y)
        y += 24f
        text("Status: ${solver.status}", x, y)
        y += 40f

        legendSwatch(x, y, color(82, 154, 232), "processing wave")
        y += 30f
        legendSwatch(x, y, color(20), "determined value")
        y += 30f
        legendSwatch(x, y, color(22, 145, 82), "guess / guess effect")
        y += 30f
        legendSwatch(x, y, color(230, 50, 45), "contradiction")
        y += 46f

        fill(55)
        textSize(13f)
        text("Rows, columns, and boxes\nscope each propagation.\nUnknown cells show remaining\nvalues; green crossed values\nwere removed by a guess.", x, y)
    }

    private fun legendSwatch(x: Float, y: Float, swatchColor: Int, label: String) {
        noStroke()
        fill(swatchColor)
        rect(x, y + 2f, 18f, 18f)
        fill(25)
        textAlign(LEFT, TOP)
        textSize(14f)
        text(label, x + 28f, y)
    }
}

private class SudokuWaveSolver(initialBoard: Array<CharArray>) {
    val cells = Array(BOARD_SIZE) { row ->
        Array(BOARD_SIZE) { col ->
            val value = initialBoard[row][col].digitToIntOrNull() ?: 0
            if (value == 0) WaveCell() else WaveCell(value = value, candidates = value.toMask())
        }
    }

    private val updateQueue = ArrayDeque<PropagationSource>()
    private val guessStack = mutableListOf<GuessFrame>()
    private var currentPeers = emptyList<CellPosition>()
    private var peerIndex = 0
    private var waitingFrames = 0
    private var done = false

    var activeSource: PropagationSource? = null
        private set
    var activePeer: CellPosition? = null
        private set
    var contradictionCell: CellPosition? = null
        private set
    var status: String = "seeding givens"
        private set
    val queueSize: Int get() = updateQueue.size
    val guessDepth: Int get() = guessStack.size

    init {
        require(isValidSudoku(initialBoard)) { "Initial board is not a valid Sudoku state." }
        forEachCell { row, col ->
            val value = cells[row][col].value
            if (value != 0) updateQueue += PropagationSource(row, col, value, guessDependent = false)
        }
    }

    fun step() {
        if (done) return
        if (contradictionCell != null) {
            waitingFrames++
            if (waitingFrames >= 8) backtrackFromContradiction()
            return
        }
        if (activeSource == null) selectNextSource()
        val source = activeSource ?: return
        if (peerIndex >= currentPeers.size) {
            activeSource = null
            activePeer = null
            selectNextSource()
            return
        }

        val peer = currentPeers[peerIndex++]
        activePeer = peer
        processPeer(source, peer)
    }

    private fun selectNextSource() {
        activePeer = null
        if (updateQueue.isNotEmpty()) {
            activeSource = updateQueue.removeFirst()
            currentPeers = peers(activeSource!!.row, activeSource!!.col)
            peerIndex = 0
            status = "propagating ${activeSource!!.value} from r${activeSource!!.row + 1}c${activeSource!!.col + 1}"
            return
        }
        activeSource = null
        val unresolved = unresolvedCells()
        if (unresolved.isEmpty()) {
            done = true
            status = "solved"
            return
        }
        makeGuess(unresolved.minBy { cells[it.row][it.col].candidateCount() })
    }

    private fun processPeer(source: PropagationSource, peer: CellPosition) {
        val cell = cells[peer.row][peer.col]
        if (cell.value != 0 || !cell.hasCandidate(source.value)) return
        cell.removeCandidate(source.value, source.guessDependent)
        if (cell.candidates == 0) {
            contradictionCell = peer
            waitingFrames = 0
            status = "contradiction at r${peer.row + 1}c${peer.col + 1}"
            return
        }
        if (cell.candidateCount() == 1) {
            val solvedValue = cell.onlyCandidate()
            cell.value = solvedValue
            cell.guessDependent = source.guessDependent
            updateQueue += PropagationSource(peer.row, peer.col, solvedValue, source.guessDependent)
        }
    }

    private fun makeGuess(position: CellPosition) {
        val cell = cells[position.row][position.col]
        val guessValue = cell.candidateValues().first()
        guessStack += GuessFrame(
            row = position.row,
            col = position.col,
            value = guessValue,
            snapshot = snapshot()
        )
        cell.value = guessValue
        cell.candidates = guessValue.toMask()
        cell.guessDependent = true
        updateQueue += PropagationSource(position.row, position.col, guessValue, guessDependent = true)
        status = "guessing $guessValue at r${position.row + 1}c${position.col + 1}"
    }

    private fun backtrackFromContradiction() {
        val frame = guessStack.removeLastOrNull()
        if (frame == null) {
            done = true
            status = "no solution from this board"
            return
        }
        restore(frame.snapshot)
        val guessedCell = cells[frame.row][frame.col]
        guessedCell.removeCandidate(frame.value, guessDependentRemoval = true)
        contradictionCell = null
        activeSource = null
        activePeer = null
        updateQueue.clear()
        if (guessedCell.candidates == 0) {
            contradictionCell = CellPosition(frame.row, frame.col)
            waitingFrames = 0
            status = "removed bad guess ${frame.value}; still contradicted"
            return
        }
        if (guessedCell.candidateCount() == 1) {
            val solvedValue = guessedCell.onlyCandidate()
            guessedCell.value = solvedValue
            guessedCell.guessDependent = true
            updateQueue += PropagationSource(frame.row, frame.col, solvedValue, guessDependent = true)
        } else {
            status = "backtracked; removed ${frame.value} from r${frame.row + 1}c${frame.col + 1}"
        }
    }

    private fun snapshot(): Array<Array<WaveCell>> = Array(BOARD_SIZE) { row ->
        Array(BOARD_SIZE) { col -> cells[row][col].copy() }
    }

    private fun restore(snapshot: Array<Array<WaveCell>>) {
        forEachCell { row, col ->
            cells[row][col] = snapshot[row][col].copy()
        }
    }

    private fun unresolvedCells(): List<CellPosition> {
        val result = mutableListOf<CellPosition>()
        forEachCell { row, col ->
            if (cells[row][col].value == 0) result += CellPosition(row, col)
        }
        return result
    }

    private fun peers(row: Int, col: Int): List<CellPosition> {
        val result = linkedSetOf<CellPosition>()
        for (i in 0 until BOARD_SIZE) {
            if (i != col) result += CellPosition(row, i)
            if (i != row) result += CellPosition(i, col)
        }
        val boxRow = row / 3 * 3
        val boxCol = col / 3 * 3
        for (r in boxRow until boxRow + 3) {
            for (c in boxCol until boxCol + 3) {
                if (r != row || c != col) result += CellPosition(r, c)
            }
        }
        return result.sortedWith(
            compareBy<CellPosition> { maxOf(kotlin.math.abs(it.row - row), kotlin.math.abs(it.col - col)) }
                .thenBy { kotlin.math.abs(it.row - row) + kotlin.math.abs(it.col - col) }
                .thenBy { it.row }
                .thenBy { it.col }
        )
    }
}

private data class CellPosition(val row: Int, val col: Int)

private data class PropagationSource(
    val row: Int,
    val col: Int,
    val value: Int,
    val guessDependent: Boolean
)

private data class GuessFrame(
    val row: Int,
    val col: Int,
    val value: Int,
    val snapshot: Array<Array<WaveCell>>
)

private data class WaveCell(
    var value: Int = 0,
    var candidates: Int = ALL_VALUES_MASK,
    var guessRemoved: Int = 0,
    var guessDependent: Boolean = false
) {
    fun hasCandidate(value: Int): Boolean = candidates and value.toMask() != 0

    fun isGuessRemoved(value: Int): Boolean = guessRemoved and value.toMask() != 0

    fun removeCandidate(value: Int, guessDependentRemoval: Boolean) {
        val mask = value.toMask()
        candidates = candidates and mask.inv()
        if (guessDependentRemoval) guessRemoved = guessRemoved or mask
    }

    fun candidateCount(): Int = Integer.bitCount(candidates)

    fun onlyCandidate(): Int = candidateValues().single()

    fun candidateValues(): List<Int> = (1..9).filter { hasCandidate(it) }
}

private fun forEachCell(block: (row: Int, col: Int) -> Unit) {
    for (row in 0 until BOARD_SIZE) {
        for (col in 0 until BOARD_SIZE) {
            block(row, col)
        }
    }
}

private fun Int.toMask(): Int = 1 shl (this - 1)
