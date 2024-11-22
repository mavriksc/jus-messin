package org.mavriksc.messin.leetcode


fun main() {
}

fun twoSum(nums: IntArray, target: Int): IntArray {
    nums.forEachIndexed { index, i ->
        nums.forEachIndexed { idx, j ->
            if (index != idx && i + j == target)
                return intArrayOf(index, idx)
        }
    }
    return intArrayOf()
}

fun twoSum2(nums: IntArray, target: Int): IntArray {
    val m = mutableMapOf<Int, Int>()
    nums.forEachIndexed { index, i ->
        m[target - i]?.let { return intArrayOf(it, index) }
        m[i] = index
    }
    return intArrayOf()
}
