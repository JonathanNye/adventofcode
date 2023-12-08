package aoc2023

import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2023/day07sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 6440)

    val problemInput = readResourceLines("/2023/day07input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 5905)
    println(part2(problemInput))
}

private class Hand(cards: List<Int>) {

    companion object {
        val part1Comparator = Comparator<Hand> { left, right ->
            if (left.part1HandType != right.part1HandType) {
                left.part1HandType.compareTo(right.part1HandType)
            } else {
                left.highCardValue.compareTo(right.highCardValue)
            }
        }
        val part2Comparator = Comparator<Hand> { left, right ->
            if (left.part2HandType != right.part2HandType) {
                left.part2HandType.compareTo(right.part2HandType)
            } else {
                left.highCardValue.compareTo(right.highCardValue)
            }
        }
    }
    init {
        if (cards.size != 5) throw IllegalArgumentException("Hands must be of size 5")
        cards.forEach { card ->
            if (card < 0 || card > 12) throw IllegalArgumentException("Illegal card value $card")
        }
    }

    val part1HandType: HandType = run {
        // counts of individual card types, descending
        val counts = cards.groupingBy { it }.eachCount().values.sortedDescending()
        when {
            counts.size == 1 -> HandType.FIVE_OF_A_KIND // There's only one kind of card
            counts.first() == 4 -> HandType.FOUR_OF_A_KIND
            counts[0] == 3 && counts[1] == 2 -> HandType.FULL_HOUSE
            counts.first() == 3 -> HandType.THREE_OF_A_KIND
            counts[0] == 2 && counts[1] == 2 -> HandType.TWO_PAIR
            counts.first() == 2 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    val part2HandType: HandType = run {
        val cardsToCounts = cards.groupingBy { it }.eachCount()
        val numWild = cardsToCounts.getOrDefault(0, 0)
        val otherCounts = cardsToCounts.filter { (card, _) -> card != 0 }.values.sortedDescending()
        when (numWild) {
            5 -> HandType.FIVE_OF_A_KIND
            4 -> HandType.FIVE_OF_A_KIND // Just match them all to the odd one out
            3 -> {
                when {
                    otherCounts[0] == 2 -> HandType.FIVE_OF_A_KIND
                    else -> HandType.FOUR_OF_A_KIND
                }
            }
            2 -> {
                when {
                    otherCounts[0] == 3 -> HandType.FIVE_OF_A_KIND
                    otherCounts[0] == 2 -> HandType.FOUR_OF_A_KIND
                    else -> HandType.THREE_OF_A_KIND
                }
            }
            1 -> {
                when {
                    otherCounts[0] == 4 -> HandType.FIVE_OF_A_KIND
                    otherCounts[0] == 3 -> HandType.FOUR_OF_A_KIND
                    otherCounts[0] == 2 && otherCounts[1] == 2 -> HandType.FULL_HOUSE
                    otherCounts[0] == 2 -> HandType.THREE_OF_A_KIND
                    else -> HandType.ONE_PAIR
                }
            }
            else -> part1HandType // No wilds, just evaluate normally
        }
    }

    private val highCardValue: Int = run {
        cards.foldIndexed(0) { idx, acc, card ->
            acc + (card shl (16 - idx * 4))
        }
    }

}

private enum class HandType : Comparable<HandType> {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

// one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
private fun Char.part1CardIntValue(): Int = when(this) {
    '2' -> 0
    '3' -> 1
    '4' -> 2
    '5' -> 3
    '6' -> 4
    '7' -> 5
    '8' -> 6
    '9' -> 7
    'T' -> 8
    'J' -> 9
    'Q' -> 10
    'K' -> 11
    'A' -> 12
    else -> throw IllegalArgumentException("Unknown card car: $this")
}

private fun Char.part2CardIntValue(): Int = when(this) {
    'J' -> 0
    '2' -> 1
    '3' -> 2
    '4' -> 3
    '5' -> 4
    '6' -> 5
    '7' -> 6
    '8' -> 7
    '9' -> 8
    'T' -> 9
    'Q' -> 10
    'K' -> 11
    'A' -> 12
    else -> throw IllegalArgumentException("Unknown card car: $this")
}

private fun part1(lines: List<String>): Int {
    val handsAndBids = lines
        .map { line ->
            val tokens = line.split(" ")
            val cardInts = tokens[0].map { it.part1CardIntValue() }
            Hand(cardInts) to tokens[1].toInt()
        }
//    val sorted = handsAndBids.sortedBy { it.first.part1HandType }
    val sorted = handsAndBids.sortedWith { o1, o2 -> Hand.part1Comparator.compare(o1.first, o2.first) }
    return sorted.mapIndexed { index, (_, bid) ->
        val rank = index + 1
        rank * bid
    }.sum()
}

private fun part2(lines: List<String>): Int {
    val handsAndBids = lines
        .map { line ->
            val tokens = line.split(" ")
            val cardInts = tokens[0].map { it.part2CardIntValue() }
            Hand(cardInts) to tokens[1].toInt()
        }
    val sorted = handsAndBids.sortedWith { o1, o2 -> Hand.part2Comparator.compare(o1.first, o2.first) }
    return sorted.mapIndexed { index, (_, bid) ->
        val rank = index + 1
        rank * bid
    }.sum()
}
