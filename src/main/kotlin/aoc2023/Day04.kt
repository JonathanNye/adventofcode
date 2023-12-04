package aoc2023

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2023/day04sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 13L)

    val problemInput = readResourceLines("/2023/day04input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 30)
    println(part2(problemInput))
}

private data class ScratchCard(
    val id: Int,
    val winners: Set<Int>,
    val numbers: List<Int>, // not sure if there are dupes
) {
    companion object {
        private val linePattern = """Card\s+(\d+):(.+)\|(.+)""".toRegex()
        private val numberPattern = """\d+""".toRegex()
        fun parse(line: String): ScratchCard {
            val (idString, winnersString, numbersString) = linePattern.matchEntire(line)!!.destructured
            val id = idString.toInt()
            val winners = numberPattern.findAll(winnersString)
                .map { it.value.toInt() }
                .toSet()
            val numbers = numberPattern.findAll(numbersString)
                .map { it.value.toInt() }
                .toList()
            return ScratchCard(
                id = id,
                winners = winners,
                numbers = numbers,
            )
        }
    }
    val numMatches = numbers
        .count { it in winners }
    val score: Long = when (numMatches) {
        0 -> 0L
        else -> {
            1L shl (numMatches - 1) // 2^(n-1)
        }
    }
}

private fun part1(input: List<String>): Long {
    val cards = input.map { ScratchCard.parse(it) }
    return cards.sumOf { it.score }
}

private fun part2(input: List<String>): Int {
    val cards = input.map { ScratchCard.parse(it) }
    val cardsToQuantity = mutableMapOf<Int, Int>()
    cards.associateTo(cardsToQuantity) { it.id to 1 }
    cards.forEach { card ->
        val baseId = card.id
        val baseQty = cardsToQuantity[baseId]!!
        if (card.numMatches > 0) {
            (baseId + 1 .. baseId + card.numMatches).forEach { incrId ->
                cardsToQuantity[incrId] = cardsToQuantity[incrId]!! + baseQty
            }
        }
    }
    return cardsToQuantity.values.sum()
}
