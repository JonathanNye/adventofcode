package aoc2024

import util.GridList
import util.solveAndAssert

fun main() {
    solveAndAssert(
        year = "2024",
        day = "06",
        ::part1 to 41,
        ::part2 to 6,
    )
}

private enum class Orientation {
    UP, DOWN, LEFT, RIGHT;

    fun next(): Orientation = when(this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }
}
private fun part1(input: List<String>): Int {
    val chars = input
        .flatMap { it.toCharArray().toList() }
    val grid = GridList(chars, width = input.first().length, height = input.size)
    var currOffset = grid.indexOf('^').also { if (it == -1) {
        throw IllegalStateException("Couldn't find start") }
    }
    var currentOrientation: Orientation = Orientation.UP
    val visited = mutableSetOf(currOffset)
    while (currOffset in 0 until grid.size) {
        if (isObstructed(currOffset, currentOrientation, grid)) {
            currentOrientation = currentOrientation.next()
        } else {
            currOffset = newOffsetMoving(currOffset, currentOrientation, grid)
            visited.add(currOffset)
        }
    }
    return visited.size - 1 // don't count the move off the grid
}

private fun facingOffset(currOffset: Int, curOrientation: Orientation, grid: GridList<Char>): Int {
    val (x, y) = grid.positionOf(currOffset)
    return when (curOrientation) {
        Orientation.UP -> grid.offsetOf(x, y - 1)
        Orientation.DOWN -> grid.offsetOf(x, y + 1)
        Orientation.LEFT -> grid.offsetOf(x - 1, y)
        Orientation.RIGHT -> grid.offsetOf(x + 1, y)
    }
}

private fun isObstructed(curOffset: Int, curOrientation: Orientation, grid: GridList<Char>): Boolean {
    val (x, y) = grid.positionOf(curOffset)
    return when (curOrientation) {
        Orientation.UP -> grid.getOrNull(x, y - 1)
        Orientation.DOWN -> grid.getOrNull(x, y + 1)
        Orientation.LEFT -> grid.getOrNull(x - 1, y)
        Orientation.RIGHT -> grid.getOrNull(x + 1, y)
    } == '#'
}

private fun newOffsetMoving(curOffset: Int, curOrientation: Orientation, grid: GridList<Char>): Int {
    val (x, y) = grid.positionOf(curOffset)
    return when (curOrientation) {
        Orientation.UP -> grid.offsetOf(x, y - 1)
        Orientation.DOWN -> grid.offsetOf(x, y + 1)
        Orientation.LEFT -> grid.offsetOf(x - 1, y)
        Orientation.RIGHT -> grid.offsetOf(x + 1, y)
    }
}

private fun part2(input: List<String>): Int {
    val chars = input
        .flatMap { it.toCharArray().toList() }
    val grid = GridList(chars, width = input.first().length, height = input.size)
    var currOffset = grid.indexOf('^').also { if (it == -1) {
        throw IllegalStateException("Couldn't find start") }
    }
    val startOffset = currOffset
    var currentOrientation: Orientation = Orientation.UP
    val visited = mutableSetOf(currOffset)
    val hitOffsets = mutableListOf<Int>()
    val loopAdditionOffsets = mutableSetOf<Int>()
    while (currOffset in 0 until grid.size) {
        if (isObstructed(currOffset, currentOrientation, grid)) {
            hitOffsets.add(facingOffset(currOffset, currentOrientation, grid))
            currentOrientation = currentOrientation.next()
            if (hitOffsets.size >= 3) { // Can make a square...
                val (pivotX, pivotY) = grid.positionOf(hitOffsets[hitOffsets.size - 3])
                val (currX, currY) = grid.positionOf(currOffset)
                val (potentialX, potentialY) = when (currentOrientation) {
                    Orientation.UP -> currX to (pivotY - 1)
                    Orientation.DOWN -> currX to (pivotY + 1)
                    Orientation.LEFT -> (pivotX - 1) to currY
                    Orientation.RIGHT -> (pivotX + 1) to currY
                }
                val potentialOffset = grid.offsetOf(potentialX, potentialY)
                if (
                    potentialOffset >= 0 && potentialOffset < grid.size && // must be in the grid
                    potentialOffset != startOffset && // must not be starting point
                    potentialOffset !in visited && // must not be previously-visited
                    grid[potentialOffset] != '#' && // must not already be an obstacle?
                    !grid.obstructionsBetween(currX, currY, potentialX, potentialY) // must be able to reach unobstructed
                ) {
                    loopAdditionOffsets.add(potentialOffset)
                }
            }
        } else {
            currOffset = newOffsetMoving(currOffset, currentOrientation, grid)
            visited.add(currOffset)
        }
    }
    for (y in 0 until grid.height) {
        var rowString = ""
        for (x in 0 until grid.width) {
            val offset = grid.offsetOf(x, y)
            rowString += if (offset in loopAdditionOffsets) {
                '0'
            } else {
                grid[offset]
            }
        }
        println(rowString)
    }
    return loopAdditionOffsets.size
}

private fun GridList<Char>.obstructionsBetween(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
    // must be colinear...
    if (x1 != x2 && y1 != y2) {
        throw IllegalArgumentException("Not colinear: $x1,$y1 and $x2,$y2")
    }
    if (x1 == x2 && y1 == y2) {
        throw IllegalArgumentException("Same points $x1,$y1")
    }
    return if (x1 == x2) { // vertical
        val lowerY = y1.coerceAtMost(y2)
        val upperY = y1.coerceAtLeast(y2)
        (lowerY .. upperY).any { testY ->
            get(x1, testY) == '#'
        }
    } else {
        // horizontal
        val lowerX = x1.coerceAtMost(x2)
        val upperX = x1.coerceAtLeast(x2)
        (lowerX .. upperX).any { testX ->
            get(testX, y1) == '#'
        }
    }
}
