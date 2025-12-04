package aoc2025

import util.assertEqual
import util.readResourceLines


fun main() {
    val sampleInput = readResourceLines("/2025/day02sample.txt")
        .first()
        .toRanges()

    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 1227775554L)

    val problemInput = readResourceLines("/2025/day02input.txt")
        .first()
        .toRanges()
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 4174379265L)
    println(part2(problemInput))
}

private fun String.toRanges(): List<LongRange> = split(',')
    .map { rangeString ->
        val parts = rangeString.split("-")
        val lower = parts[0].toLong()
        val upper = parts[1].toLong()
        lower..upper
    }

private fun part1(input: List<LongRange>): Long {
    return input.sumOf { range ->
        range.sumOf { candidate ->
            if (candidate.isHalfRepeater()) {
                candidate
            } else {
                0L
            }
        }
    }
}

private fun part2(input: List<LongRange>): Long {
    return input.sumOf { range ->
        range.sumOf { candidate ->
            if (candidate.isRepeater()) {
                candidate
            } else {
                0L
            }
        }
    }
}

private fun Long.isHalfRepeater(): Boolean {
    val string = this.toString()
    val length = string.length
    if (length % 2 != 0) {
        return false
    }
    return string.take(length / 2) ==
            string.substring(length / 2, length)
}

private fun Long.isRepeater(): Boolean {
    val string = this.toString()
    val length = string.length
    for (substringLength in (1 .. length / 2)) {
        if (length % substringLength != 0) {
            continue
        }
        val substrings = (0 until length / substringLength)
            .map {
                val start = it * substringLength
                string.substring(start, start + substringLength)
            }

        val substringSet = substrings.toSet()
        if (substringSet.size == 1) {
            return true
        }
    }
    return false
}
