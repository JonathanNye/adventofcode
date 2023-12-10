package aoc2023

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2023/day09sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 114)

    val problemInput = readResourceLines("/2023/day09input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 2)
    println(part2(problemInput))
}

private fun part1(input: List<String>): Int {
    val histories = input
        .map { line ->
            line.split(' ').map { it.toInt() }
        }
    return histories.sumOfNextValues()
}

private fun part2(input: List<String>): Int {
    val histories = input
        .map { line ->
            line.split(' ').map { it.toInt() }.reversed()
        }
    return histories.sumOfNextValues()
}

private fun List<List<Int>>.sumOfNextValues(): Int {
    fun List<Int>.differences() = zipWithNext { a, b -> b - a }
    fun List<Int>.nextValue(): Int {
        if (this.all { it == 0 }) {
            return 0
        }
        val differences = this.differences()
        return this.last() + differences.nextValue()
    }
    return sumOf { it.nextValue() }
}
