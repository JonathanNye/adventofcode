package aoc2025

import util.solveAndAssert
import java.lang.IllegalStateException

fun main() {
    solveAndAssert(
        year = "2025",
        day = "06",
        part1 = ::part1 to 4277556L,
        part2 = ::part2 to 3263827L,
    )
}

private enum class Operation {
    ADD, MULTIPLY
}
private fun part1(input: List<String>): Long {
    val problemsLines = input.dropLast(1)
    val operations = input.last().mapNotNull { char ->
        when (char) {
            '+' -> Operation.ADD
            '*' -> Operation.MULTIPLY
            else -> null
        }
    }
    val operandsLines = problemsLines
        .map { line ->
            line.split(" ").mapNotNull { token -> token.trim().toLongOrNull() }
        }
    val problems = operandsLines.first().indices.map { problemIdx ->
        operandsLines.map { line -> line[problemIdx] } to operations[problemIdx]
    }
    return problems.sumOf { (operands, operation) ->
        operands.reduce { left, right ->
            when (operation) {
                Operation.ADD -> left + right
                Operation.MULTIPLY -> left * right
            }
        }
    }
}

private fun part2(input: List<String>): Long {
    val operandsLinesRaw = input.dropLast(1)
    val longestOperandLineLength =  operandsLinesRaw.maxOf { it.length }
    val operandsLines = operandsLinesRaw.map {
        it.padEnd(longestOperandLineLength)
    }
    val operationsLine = input.last()
    val operatorIndices = operationsLine.indices.filter { idx ->
        !operationsLine[idx].isWhitespace()
    }
    val operations = input.last().mapNotNull { char ->
        when (char) {
            '+' -> Operation.ADD
            '*' -> Operation.MULTIPLY
            else -> null
        }
    }

    val matcher = """\s*\d+[\s$]*""".toRegex()
    val operandsStrings = operatorIndices.map { operatorIdx ->
        operandsLines.map { operandLine ->
            matcher.matchAt(operandLine, operatorIdx)?.value
                ?: throw IllegalStateException("Couldn't match")
        }
    }

    val operands = operandsStrings.map { operandStringsList ->
        // hacky, but just take the longest one and check all indices
        val charsToCheck = operandStringsList.maxOf { it.length }
        (0 until charsToCheck).mapNotNull { idxToCheck ->
            val candidate = operandStringsList.map { operandString ->
                operandString.getOrNull(idxToCheck) ?: ' '
            }
            candidate.joinToString(separator = "").trim().toLongOrNull()
        }
    }

    val problems = operations.mapIndexed { problemIdx, operation ->
        operands[problemIdx] to operation
    }
    return problems.sumOf { (operands, operation) ->
        operands.reduce { left, right ->
            when (operation) {
                Operation.ADD -> left + right
                Operation.MULTIPLY -> left * right
            }
        }
    }
}