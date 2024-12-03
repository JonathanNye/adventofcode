import util.assertEqual
import util.readResourceLines
import kotlin.math.abs

fun main() {
    val sampleInput = readResourceLines("/2024/day02sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 2)

    val problemInput = readResourceLines("/2024/day02input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 4)
    println(part2(problemInput))
}

private fun part1(input: List<String>): Int {
    return input
        .map { line ->
            line.split(' ').map { it.toInt() }
        }
        .count { intLine ->
            intLine.isSafe()
        }
}

private fun part2(input: List<String>): Int {
    return input
        .map { line ->
            line.split(' ').map { it.toInt() }
        }
        .count { intLine ->
            val safeInitially = intLine.isSafe()
            if (safeInitially) {
                true
            } else {
                intLine
                    .oneMissingPermutations()
                    .any { permutation -> permutation.isSafe() }
            }
        }
}

private fun List<Int>.isSafe(): Boolean {
    val pairs = zipWithNext()
    return pairs.all { it.safeIncreasing() } || pairs.all { it.safeDecreasing() }
}

private fun Pair<Int, Int>.safeIncreasing(): Boolean {
    val delta = first - second
    return delta <= -1 && delta >= -3
}

private fun Pair<Int, Int>.safeDecreasing(): Boolean {
    val delta = first - second
    return delta >= 1 && delta <= 3
}

private fun List<Int>.oneMissingPermutations(): List<List<Int>> {
    return indices.map { idx ->
        val toModify = this.toMutableList()
        toModify.removeAt(idx)
        toModify
    }
}
