package aoc2024

import util.GridList
import util.assertEqual
import util.readResourceLines

fun main() {
    val sampleInput = readResourceLines("/2024/day04sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution,18)

    val problemInput = readResourceLines("/2024/day04input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 9)
    println(part2(problemInput))
}

private fun part1(input: List<String>): Int {
    val chars = input.map { it.toCharArray().toList() }
        .flatten()

    val gridList = GridList(chars, width = input.first().length, height = input.size)

    val columns = gridList.columns().let {
        it + it.map { column -> column.reversed() }
    }
    val rows = gridList.rows().let {
        it + it.map { row -> row.reversed() }
    }
    val diagonals = gridList.diagonals()
    val everythingChars = columns + rows + diagonals
    val everythingStrings = everythingChars.map { it.joinToString(separator = "") }
    val toFind = """XMAS""".toRegex()
    return everythingStrings.sumOf {
        toFind.findAll(it).count()
    }
}

private fun part2(input: List<String>): Int {
    val chars = input.map { it.toCharArray().toList() }
        .flatten()

    val gridList = GridList(chars, width = input.first().length, height = input.size)

    fun GridList<Char>.masxAt(x: Int, y: Int): Boolean {
        val upLeft = get(x - 1, y - 1)
        val upRight = get(x + 1, y - 1)
        val downLeft = get(x - 1, y + 1)
        val downRight = get(x + 1, y + 1)
        return get(x, y) == 'A' &&
                // down/right axis
                ((upLeft == 'M' && downRight == 'S') || (upLeft == 'S' && downRight == 'M')) &&
                // down/left axis
                ((upRight == 'M' && downLeft == 'S') || (upRight == 'S' && downLeft == 'M'))
    }
    var masXesFound = 0
    for (x in 1 until gridList.width - 1) {
        for (y in 1 until gridList.height - 1) {
            if (gridList.masxAt(x, y)) {
                masXesFound += 1
            }
        }
    }
    return masXesFound
}

private fun <T> GridList<T>.diagonals(): List<List<T>> {
    val diagonals = mutableListOf<List<T>>()
    fun GridList<T>.downRightDiagonal(startX: Int, startY: Int): List<T> {
        val diagonal = mutableListOf<T>()
        var x = startX
        var y = startY
        while (x < width && y < height) {
            diagonal.add(get(x, y))
            x += 1
            y += 1
        }
        return diagonal
    }
    fun GridList<T>.downLeftDiagonal(startX: Int, startY: Int): List<T> {
        val diagonal = mutableListOf<T>()
        var x = startX
        var y = startY
        while (x >= 0 && y < height) {
            diagonal.add(get(x, y))
            x -= 1
            y += 1
        }
        return diagonal
    }
    // down-right
//    diagonals.add(downRightDiagonal(0, 0))
    for (x in 0 until width) {
        diagonals.add(downRightDiagonal(x, 0))
    }
    for (y in 1 until height) {
        diagonals.add(downRightDiagonal(0, y))
    }

    // down-left
//    diagonals.add(downLeftDiagonal(width - 1, 0))
    for (x in 0 until width) {
        diagonals.add(downLeftDiagonal(x, 0))
    }
    for (y in 1 until height) {
        diagonals.add(downLeftDiagonal(width - 1, y))
    }
    val reverses = diagonals.map { it.reversed() }
    return diagonals + reverses
}