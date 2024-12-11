package aoc2024

import util.solveAndAssert

fun main() {
    solveAndAssert(
        year = "2024",
        day = "07",
        ::part1 to 3749L,
        ::part2 to 11387L,
    )
}

private sealed interface PartOneOperator
private sealed interface PartTwoOperator

object Add : PartOneOperator, PartTwoOperator
object Multiply : PartOneOperator, PartTwoOperator
object Concat : PartTwoOperator

private fun partOnePermutations(length: Int): Sequence<List<PartOneOperator>> {
    var curr = 0
    val max = intPow(2, length) - 1
    return generateSequence {
        if (curr > max) {
            null
        } else {
            val bitString = curr.toString(2).padStart(length, '0')
            val toReturn = (0 until length).map {
                when (val char = bitString.getOrElse(it) { '0' }) {
                    '0' -> Add
                    '1' -> Multiply
                    else -> throw IllegalStateException("Unexpected $char")
                }
            }
            curr += 1
            toReturn
        }
    }
}

private fun partTwoPermutations(length: Int): Sequence<List<PartTwoOperator>> {
    var curr = 0
    val max = intPow(3, length) - 1
    return generateSequence {
        if (curr > max) {
            null
        } else {
            val bitString = curr.toString(3).padStart(length, '0')
            val toReturn = (0 until length).map {
                when (val char = bitString.getOrElse(it) { '0' }) {
                    '0' -> Add
                    '1' -> Multiply
                    '2' -> Concat
                    else -> throw IllegalStateException("Unexpected $char")
                }
            }
            curr += 1
            toReturn
        }
    }
}

private fun intPow(base: Int, exponent: Int): Int {
    if (exponent == 0) return 1
    if (exponent < 0) return 0 // Or throw an exception

    var result = 1
    for (i in 1..exponent) {
        result *= base
    }
    return result
}


private fun part1(input: List<String>): Long {
    val equations = input
        .map { line ->
            val parts = line.split(": ")
            val operands = parts[1].split(" ").map { it.toLong() }
            parts[0].toLong() to operands
        }

    return equations
        .filter { (target, operands) ->
            val numOperators = operands.size - 1
            partOnePermutations(numOperators).any { operators ->
                val result = operands.reduceIndexed { index, acc, next ->
                    when (operators[index - 1]) {
                        Add -> acc + next
                        Multiply -> acc * next
                    }
                }
                result == target
            }
        }
        .sumOf { it.first }
}

private fun part2(input: List<String>): Long {
    val equations = input
        .map { line ->
            val parts = line.split(": ")
            val operands = parts[1].split(" ").map { it.toLong() }
            parts[0].toLong() to operands
        }

    return equations
        .filter { (target, operands) ->
            val numOperators = operands.size - 1
            partTwoPermutations(numOperators).any { operators ->
                val result = operands.reduceIndexed { index, acc, next ->
                    when (operators[index - 1]) {
                        Add -> acc + next
                        Multiply -> acc * next
                        Concat -> (acc.toString() + next.toString()).toLong()
                    }
                }
                result == target
            }
        }
        .sumOf { it.first }
}
