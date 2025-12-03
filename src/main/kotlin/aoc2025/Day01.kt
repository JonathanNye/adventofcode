package aoc2025

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2025/day01sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 3)

    val problemInput = readResourceLines("/2025/day01input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 6)
    println(part2(problemInput))
}

private val linePattern = """([LR])(\d+)""".toRegex()

private enum class Direction {
    LEFT, RIGHT
}

private const val NUM_POSSIBLE_VALUES = 100

private fun String.toDirection(): Direction = when (this) {
    "L" -> Direction.LEFT
    "R" -> Direction.RIGHT
    else -> throw IllegalArgumentException("Unknown direction \"$this\"")
}

private fun part1(input: List<String>): Int {
    return input
        .map { line ->
            val (dirString, rotationString) = linePattern.matchEntire(line)!!.destructured
            dirString.toDirection() to rotationString.toInt()
        }
        .map { rotation ->
            when (rotation.first) {
                Direction.LEFT -> {
                    // turn into equivalent right rotation to make math easier lol
                    Direction.RIGHT to (NUM_POSSIBLE_VALUES - rotation.second)
                }
                Direction.RIGHT -> rotation // leave it alone
            }
        }
        .runningFold(50) { currRotation, (_, rotationAmount) ->
            // it's always right, just add it
            (currRotation + rotationAmount) % NUM_POSSIBLE_VALUES
        }
        .count { it == 0 }
}

private fun part2(input: List<String>): Int {
    var currentRotation = 50
    var zeroHits = 0
    val rotations = input
        .map { line ->
            val (dirString, rotationString) = linePattern.matchEntire(line)!!.destructured
            dirString.toDirection() to rotationString.toInt()
        }
        .flatMap { (direction, amount) ->
            if (amount <= NUM_POSSIBLE_VALUES) {
                listOf(direction to amount)
            } else {
                // big rotation, break it into multiples of no more than one revolution
                val numFullRotations = amount / NUM_POSSIBLE_VALUES
                val extraRotationAmount = amount % NUM_POSSIBLE_VALUES
                buildList {
                    repeat(numFullRotations) {
                        add(direction to NUM_POSSIBLE_VALUES)
                    }
                    add(direction to extraRotationAmount)
                }
            }
        }

    for ((direction, rotation) in rotations) {
        val startedFromZero = currentRotation == 0

        // If it's a single revolution, just count a zero hit
        if (rotation == NUM_POSSIBLE_VALUES) {
            zeroHits++
            continue
        }
        when (direction) {
            Direction.LEFT -> currentRotation -= rotation
            Direction.RIGHT -> currentRotation += rotation
        }


        if (currentRotation == 0) {
            zeroHits++
        } else if (currentRotation < 0 ) {
            currentRotation += NUM_POSSIBLE_VALUES
            if (!startedFromZero) {
                zeroHits++
            }
        }
        else if (currentRotation >= NUM_POSSIBLE_VALUES) {
            zeroHits++
            currentRotation %= NUM_POSSIBLE_VALUES
        }
    }
    return zeroHits
}