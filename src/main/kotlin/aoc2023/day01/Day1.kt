package aoc2023.day01

import util.readResourceLines
import java.io.File

fun main() {
    val sampleInput = readResourceLines("/2023/day01sample.txt")
    val sampleSolution = part1(sampleInput)
    assert(sampleSolution == 77)

    val problemInput = readResourceLines("/2023/day01input.txt")
//    println(part1(problemInput))

    val sampleInput2 = readResourceLines("/2023/day01sample2.txt")
//    val sampleSolution2 = part2(sampleInput2)
//    assert(sampleSolution2 == 281)
//    println(part2(problemInput))
    part2(problemInput)
}

private fun part1(lines: List<String>): Int {
    return lines.sumOf { line ->
        val digits = line.filter { it.isDigit() }
        (digits.first().toString() + digits.last()).toInt()
    }
}

private fun part2(lines: List<String>): Int {
    val digitMappings = listOf(
        "zero" to "0",
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
    )
    fun String.replaceAllInstancesOf(mappings: List<Pair<String, String>>): String {
        var working = this
        for (mapping in mappings) {
            working = working.replace(mapping.first, mapping.second)
        }
        return working
    }
    return lines
        .map { it.replaceAllInstancesOf(digitMappings) }
        .onEach {
            println(it)
        }
        .sumOf { line ->
            val digits = line.filter { it.isDigit() }
            (digits.first().toString() + digits.last()).toInt()
        }
}