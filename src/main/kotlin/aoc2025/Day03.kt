package aoc2025

import util.assertEqual
import util.readResourceLines
import kotlin.math.max

fun main() {
    val sampleInput = readResourceLines("/2025/day03sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 357)

    val problemInput = readResourceLines("/2025/day03input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 3121910778619L)
    println(part2(problemInput))
}

private fun part1(input: List<String>): Int {
    return input
        .map { line ->
            line.map { char -> char.digitToInt() }
        }.sumOf { digits ->
            var currMax = 0
            var maxTens = 0 // optimization to skip?
            for (tensIdx in 0..digits.size - 2) { // minus 2 because we need a later ones digit
                val tens = digits[tensIdx]
                // Skip, we can't make a bigger number
                if (tens < maxTens) {
                    continue
                }
                maxTens = tens
                for (onesIdx in tensIdx + 1 until digits.size) {
                    val candidate = maxTens * 10 + digits[onesIdx]
                    currMax = max(candidate, currMax)
                }
            }
            currMax
        }
}

private fun part2(input: List<String>): Long {
    return input
        .map { line ->
            line.map { char -> char.digitToInt() }
        }.sumOf { digits ->
            var workingBank = digits
            (0 until 12).map { digitIdx ->
                val eligibleNumbers = workingBank.subList(0, workingBank.size - 12 + digitIdx + 1)
                val winningIdx = eligibleNumbers.indexOfFirst { candidate ->
                    eligibleNumbers.all { it <= candidate }
                }
                val winner = eligibleNumbers[winningIdx]
                workingBank = workingBank.subList(winningIdx + 1, workingBank.size)
                winner
            }.joinToString(separator = "").toLong()
        }
}