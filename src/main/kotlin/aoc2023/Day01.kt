package aoc2023

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2023/day01sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 142)

    val problemInput = readResourceLines("/2023/day01input.txt")
    println(part1(problemInput))

    val sampleInput2 = readResourceLines("/2023/day01sample2.txt")
    val sampleSolution2 = part2(sampleInput2)
    assertEqual(sampleSolution2, 281)
    println(part2(problemInput))
}

private fun part1(lines: List<String>): Int {
    return lines.sumOf { line ->
        val digits = line.filter { it.isDigit() }
        (digits.first().toString() + digits.last()).toInt()
    }
}

private fun part2(lines: List<String>): Int {
    return lines
        .sumOf { line ->
            val foundDigits = line.findDigits()
            (foundDigits.first().toString() + foundDigits.last()).toInt()
        }
}

fun String.findDigits(): List<Int> {
    val foundAsDigits = this.mapIndexed { idx, char -> idx to char }
        .filter { (_, char) -> char.isDigit() }
    val foundAsWords = digitMappings.keys.flatMap { digitString ->
        this
            .indicesOf(digitString)
            .map { idx -> idx to (digitMappings[digitString] ?: throw IllegalStateException("Couldn't map $digitString")) }
    }
    return (foundAsDigits + foundAsWords)
        .sortedBy { it.first }
        .map { it.second.digitToInt() }
}

private val digitMappings = mapOf(
    "zero" to '0',
    "one" to '1',
    "two" to '2',
    "three" to '3',
    "four" to '4',
    "five" to '5',
    "six" to '6',
    "seven" to '7',
    "eight" to '8',
    "nine" to '9',
)
private fun String.indicesOf(substring: String): List<Int> {
    var startIdx = 0
    val results = mutableListOf<Int>()
    while (true) {
        val foundIdx = this.indexOf(substring, startIdx)
        if (foundIdx == -1) {
            break
        } else {
            results.add(foundIdx)
        }
        startIdx = foundIdx + 1
    }
    return results
}
