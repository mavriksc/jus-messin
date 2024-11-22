package org.mavriksc.messin.hackerrank


fun main() {
    println(anagram("xyyx"))
}


fun anagram(s: String): Int {
    if (s.length % 2 == 1)
        return -1
    val half = s.length / 2
    val w1Cache = mutableMapOf<Char, Int>()
    val w2Cache = mutableMapOf<Char, Int>()
    for (i in 0 until half) {
        // min(sum a dif b, sum b dif a)
        if (s[i] != s[i + half]) {
            if (w1Cache[s[i + half]] != null && w1Cache[s[i + half]]!! > 0)
                w1Cache[s[i + half]] = w1Cache[s[i + half]]!! - 1
            w1Cache[s[i]] = w1Cache.getOrDefault(s[i], 0) + 1

            if (w2Cache[s[i]] != null && w2Cache[s[i]]!! > 0)
                w2Cache[s[i]] = w2Cache[s[i]]!! - 1
            w2Cache[s[i + half]] = w2Cache.getOrDefault(s[i + half], 0) + 1
        }
    }
    return Math.min(w1Cache.values.sum(), w2Cache.values.sum())
}
