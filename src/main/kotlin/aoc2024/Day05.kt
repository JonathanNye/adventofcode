package aoc2024

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2024/day05sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution,143)

    val problemInput = readResourceLines("/2024/day05input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 123)
    println(part2(problemInput))
}

private fun part1(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    return updates
        .filter { update -> update.isValidGiven(rules) }
        .sumOf { update -> update[update.size / 2] }
}

private fun parseInput(input: List<String>): Pair<List<Pair<Int,Int>>, List<List<Int>> > {
    val dividerIdx = input.indexOf("")
    val rules = input.take(dividerIdx)
        .map { line ->
            line.split('|').let { it[0].toInt() to it[1].toInt() }
        }
    val updates: List<List<Int>> = input.drop(dividerIdx + 1)
        .map { line ->
            val updateParts = line
                .split(',')
                .map { it.toInt() }
            updateParts
        }
    return rules to updates
}

private fun List<Int>.isValidGiven(rules: List<Pair<Int, Int>>): Boolean {
    return rules.all { (leftPage, rightPage) ->
        val leftIdx = indexOf(leftPage)
        val rightIdx = indexOf(rightPage)
        if (leftIdx == -1 || rightIdx == -1) {
            true
        } else {
            leftIdx < rightIdx
        }
    }
}

private fun part2(input: List<String>): Int {
    val (rules, updates) = parseInput(input)

    val comp = Comparator<Int> { o1, o2 ->
        if (o1 == o2) {
            return@Comparator 0
        }
        if (rules.contains(o1 to o2)) {
            -1
        } else {
            1
        }
    }

    return updates
        .filter {
            update -> !update.isValidGiven(rules)
        }
        .map { failedUpdate ->
            failedUpdate.sortedWith(comp)
        }
        .sumOf { update -> update[update.size / 2] }
}
