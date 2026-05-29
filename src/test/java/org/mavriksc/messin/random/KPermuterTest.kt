package org.mavriksc.messin.random

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KPermuterTest {
    @Test
    fun generatesExpectedPermutationCounts() {
        val expected = mapOf(
            1 to 1,
            2 to 2,
            3 to 6,
            4 to 24,
            5 to 120,
            6 to 720
        )

        for ((n, count) in expected) {
            assertEquals(count, KPermuter.permutations(n).size)
        }
    }

    @Test
    fun rotationColumnsCoverEveryPermutationOnce() {
        val n = 5
        val columns = KPermuter.rotationColumns(n)
        val ids = HashSet<String>()

        for (column in columns) {
            for (rotation in column.orientations) {
                ids.add(rotation.joinToString(","))
            }
        }

        assertEquals(KPermuter.permutations(n).size, ids.size)
        assertEquals(KPermuter.permutations(n).size, columns.sumOf { it.orientations.size })
    }

    @Test
    fun singleSymbolRotationColumnIsSupported() {
        val columns = KPermuter.rotationColumns(1)

        assertEquals(1, columns.size)
        assertEquals("A", KPermuter.render(columns.single().sequence))
    }

    @Test
    fun computesSuffixPrefixOverlap() {
        val left = intArrayOf(0, 1, 2, 3, 4)
        val right = intArrayOf(2, 3, 4, 0, 1)
        val unrelated = intArrayOf(4, 3, 2, 1, 0)

        assertEquals(3, KPermuter.overlap(left, right))
        assertEquals(1, KPermuter.overlap(left, unrelated))
    }

    @Test
    fun precomputesColumnOverlapScores() {
        val columns = KPermuter.rotationColumns(3)
        val overlaps = KPermuter.columnOverlapMatrix(columns)

        assertEquals(columns.size, overlaps.size)
        assertEquals(columns[0].sequence.size, overlaps[0][0])
        assertEquals(
            KPermuter.overlap(columns[0].sequence, columns[1].sequence),
            overlaps[0][1]
        )
    }

    @Test
    fun recursiveBaselineHasExpectedLengths() {
        val expected = mapOf(
            1 to 1,
            2 to 3,
            3 to 9,
            4 to 33,
            5 to 153,
            6 to 873
        )

        for ((n, length) in expected) {
            val candidate = KPermuter.recursiveSuperpermutation(n)
            assertEquals(KPermuter.expectedRecursiveLength(n), candidate.size)
            assertEquals(length, candidate.size)
        }
    }

    @Test
    fun defaultFiveSymbolCandidateContainsAllPermutations() {
        val candidate = KPermuter.recursiveSuperpermutation(5)

        assertEquals(153, candidate.size)
        assertTrue(KPermuter.containsAllPermutations(candidate, 5))
    }

    @Test
    fun sixSymbolCandidateContainsAllPermutations() {
        val candidate = KPermuter.recursiveSuperpermutation(6)

        assertEquals(873, candidate.size)
        assertTrue(KPermuter.containsAllPermutations(candidate, 6))
    }

    @Test
    fun beamSearchHandlesSixSymbolRotationColumns() {
        val columns = KPermuter.rotationColumns(6)
        val arrangement = KPermuter.arrangeColumnsBeam(columns, 10)

        assertEquals(120, columns.size)
        assertEquals(120, arrangement.order.size)
    }

    @Test
    fun orientedColumnBeamProducesAValidSixSymbolCandidate() {
        val columns = KPermuter.rotationColumns(6)
        val result = KPermuter.arrangeOrientedColumnsParallel(columns, 12, 4)

        assertTrue(KPermuter.containsAllPermutations(result.sequence, 6))
    }

    @Test
    fun graphBeamProducesAValidFiveSymbolCandidate() {
        val result = KPermuter.arrangePermutationGraphParallel(5, 12, 4)

        assertTrue(KPermuter.containsAllPermutations(result.sequence, 5))
    }
}
