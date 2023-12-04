package aoc2023

import util.GridList
import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2023/day03sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 4361)

    val problemInput = readResourceLines("/2023/day03input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 467835)
    println(part2(problemInput))
}

private val numberMatcher = """\d+""".toRegex()

private data class NumberCluster(
    val xRange: IntRange,
    val y: Int,
    val value: Int,
)

private fun part1(lines: List<String>): Int {
    val width = lines.first().length
    val height = lines.size
    val allChars = lines
        .flatMap { it.toCharArray().toList() }
    val grid = GridList(
        backingList = allChars,
        width = width,
        height = height
    )
    val numberClusters = findNumberClusters(lines)
    return numberClusters
        .filter { cluster ->
            cluster.xRange.any { x ->
                grid
                    .neighbors(x = x, y = cluster.y, includeDiagonals = true)
                    .any { it.isEligibleSymbol() }
            }
        }
        .sumOf { it.value }
}

fun Char.isEligibleSymbol(): Boolean = !this.isDigit() && this != '.'

private fun findNumberClusters(lines: List<String>): List<NumberCluster> {
    return lines.flatMapIndexed { y, line ->
        val numberMatches = numberMatcher.findAll(line)
        numberMatches.map { match ->
            NumberCluster(
                xRange = match.range,
                y = y,
                match.value.toInt(),
            )
        }
    }
}

private fun part2(lines: List<String>): Int {
    val width = lines.first().length
    val height = lines.size
    val allChars = lines
        .flatMap { it.toCharArray().toList() }
    val grid = GridList(
        backingList = allChars,
        width = width,
        height = height
    )
    val numberClusters = findNumberClusters(lines)
    val asteriskOffsets = grid.flatMapIndexed { idx, char ->
        if (char == '*') {
            listOf(idx)
        } else {
            emptyList()
        }
    }
    val offsetsToClusters: Map<Int, NumberCluster> = numberClusters
        .flatMap { cluster ->
            cluster.xRange.map { x ->
                (grid.width * cluster.y + x) to cluster
            }
        }.toMap()

    return asteriskOffsets
        .sumOf { asteriskOffset ->
            val adjacentClusters = grid.neighborOffsets(asteriskOffset, includeDiagonals = true)
                .mapNotNull { asteriskNeighborOffset -> offsetsToClusters[asteriskNeighborOffset] }
                .distinct()
            if (adjacentClusters.size == 2) {
                adjacentClusters[0].value * adjacentClusters[1].value
            } else {
                0
            }
        }
}