package org.mavriksc.messin.hackerrank

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Scanner

class AhoCorasickHealthFSMTest {
    @Test
    fun scoresOverlappingAndDuplicateGenes() {
        val genes = arrayOf("a", "b", "c", "aa", "d", "b")
        val health = arrayOf(1, 2, 3, 4, 5, 6)
        val fsm = AhoCorasickHealthFSM(genes)

        assertEquals(19L, fsm.scoreStrand("caaab", StrandInfo(1, 5), health))
    }

    @Test
    fun solvesHackerRankSample() {
        val input = """
            6
            a b c aa d b
            1 2 3 4 5 6
            3
            1 5 caaab
            0 4 xyz
            2 4 bcdybc
        """.trimIndent()

        assertEquals("0 19", solveDnaHealth(Scanner(input)))
    }
}
