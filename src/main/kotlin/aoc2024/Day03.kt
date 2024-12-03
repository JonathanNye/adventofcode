package aoc2024

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2024/day03sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution,161)

    val problemInput = readResourceLines("/2024/day03input.txt")
    println(part1(problemInput))

    val sampleInput2 = readResourceLines("/2024/day03sample2.txt")
    val sampleSolution2 = part2(sampleInput2)
    assertEqual(sampleSolution2, 48)
    println(part2(problemInput))
}

private val mulMatcher = """mul\((\d+),(\d+)\)""".toRegex()

private fun part1(input: List<String>): Int = sumOfMulsIn(input)

private fun sumOfMulsIn(lines: List<String>): Int =
    lines
        .flatMap { line ->
            mulMatcher.findAll(line)
        }.sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

private val dontDoMatcher = """don't\(\).*?(do\(\)|$)""".toRegex()

private fun part2(input: List<String>): Int {
    val oneLine = input.reduce { acc, next -> acc + next }
    val removals = dontDoMatcher.findAll(oneLine).map { it.value }.toList()
    val minusRemovals = removals.fold(initial = oneLine) { acc, matchResult ->
        acc.replaceFirst(matchResult, "")
    }
    return sumOfMulsIn(listOf(minusRemovals))
}


