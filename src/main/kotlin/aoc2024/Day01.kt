package aoc2024

import util.assertEqual
import util.readResourceLines
import kotlin.math.abs

fun main() {
    val sampleInput = readResourceLines("/2024/day01sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 11)

    val problemInput = readResourceLines("/2024/day01input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 31)
    println(part2(problemInput))
}

private val linePattern = """(\d+)\s+(\d+)""".toRegex()


private fun part1(input: List<String>): Int {
    val (leftColumn, rightColumn) = parseColumns(input)
    return leftColumn.sorted()
        .zip(rightColumn.sorted())
        .sumOf { (left, right) -> abs(left - right) }
}

private fun part2(input: List<String>): Int {
    val (leftColumn, rightColumn) = parseColumns(input)
    val rightScores = rightColumn.groupingBy { it }.eachCount()
        .mapValues { (value, count) -> value * count }
    return leftColumn
        .sumOf { leftValue -> rightScores.getOrDefault(leftValue, 0) }
}

private fun parseColumns(input: List<String>) : Pair<List<Int>, List<Int>> =
    input
        .map { line ->
            val (left, right) = linePattern.matchEntire(line)!!.destructured
            left.toInt() to right.toInt()
        }
        .let { parsedLines ->
            parsedLines.map { it.first } to parsedLines.map { it.second }
        }