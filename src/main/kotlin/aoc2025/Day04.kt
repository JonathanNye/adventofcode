package aoc2025

import util.GridList
import util.solveAndAssert

fun main() {
    solveAndAssert(
        year = "2025",
        day = "04",
        part1 = ::part1 to 13,
        part2 = ::part2 to 43,
    )
}

private fun part1(input: List<String>): Int {
    val chars = input.flatMap { it.toCharArray().toList() }
    val grid = GridList(
        backingList = chars,
        width = input.first().length,
        height = input.size,
    )

    return grid.removableIndices().count()
}

private fun GridList<Char>.removableIndices(): List<Int> = indices.filter { index ->
    // is a roll of paper
    this[index] == '@' &&
            // has fewer than 4 paper neighbors
            this.neighbors(index, includeDiagonals = true).count {
                it == '@'
            } < 4
}

private fun part2(input: List<String>): Int {
    val inputChars = input.flatMap { it.toCharArray().toList() }
    val width = input.first().length
    val height = input.size
    var workingGrid = GridList(
        backingList = inputChars,
        width = width,
        height = height,
    )
    // count how many can be removed until no more can be removed
    var removedIndices = 0
    do {
        val removableIndices = workingGrid.removableIndices()
        removedIndices += removableIndices.size
        val newBackingList = workingGrid.mapIndexed { idx, oldValue ->
            if (idx in removableIndices) {
                '.'
            } else {
                oldValue
            }
        }
        workingGrid = GridList(
            backingList = newBackingList,
            width = width,
            height = height,
        )
    } while (removableIndices.isNotEmpty())

    return removedIndices
}
