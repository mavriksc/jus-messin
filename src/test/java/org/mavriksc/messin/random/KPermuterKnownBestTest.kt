package org.mavriksc.messin.random

import org.junit.Assert.assertEquals
import org.junit.Test

class KPermuterKnownBestTest {
    @Test
    fun mapsKnownBestIntoRotationRunChunks() {
        val analysis = KPermuterKnownBest.analyzeKnownBest()

        assertEquals(872, analysis.source.length)
        assertEquals(720, analysis.uniquePermutations)
        assertEquals(120, analysis.classCount)
        assertEquals(145, analysis.runs.size)
        assertEquals(95, analysis.runs.count { it.windowCount == 6 })
        assertEquals(25, analysis.splitClassCount)
    }
}
