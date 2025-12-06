package aoc2025

import util.solveAndAssert
import kotlin.math.max
import kotlin.math.min

fun main() {
    solveAndAssert(
        year = "2025",
        day = "05",
        part1 = ::part1 to 3,
        part2 = ::part2 to 14L,
    )
}

private val rangeMatcher = """(\d+)-(\d+)""".toRegex()
private val ingredientMatcher = """\d+""".toRegex()
private fun parse(input: List<String>): Pair<List<LongRange>, List<Long>> {
    val ranges = input.mapNotNull { line ->
        rangeMatcher.matchEntire(line)?.destructured?.let { (start, end) ->
            start.toLong()..end.toLong()
        }
    }
    val ingredients = input.mapNotNull { line ->
        ingredientMatcher.matchEntire(line)?.let {
            line.toLong()
        }
    }
    return ranges to ingredients
}

private fun part1(input: List<String>): Int {
    val (ranges, ingredients) = parse(input)
    val freshCount = ingredients.count { ingredient ->
        ranges.any { range -> range.contains(ingredient) }
    }
    return freshCount
}

private fun part2(input: List<String>): Long {
    val (ranges, _) = parse(input)
    val workingList = ranges.toMutableList()
    do {
        val overlappingIndices = workingList.findOverlappersIndices()
        if (overlappingIndices != null) {
            workingList.mergeIndices(overlappingIndices.first, overlappingIndices.second)
        }
    } while (overlappingIndices != null)

    return workingList.sumOf { range ->
        range.last - range.first + 1L
    }
}

private fun List<LongRange>.findOverlappersIndices(): Pair<Int, Int>? {
    this.forEachIndexed { idx, range ->
        this.forEachIndexed { otherIdx, otherRange ->
            if (idx != otherIdx &&
                (otherRange.contains(range.first) || otherRange.contains(range.last))
            ) {
                return idx to otherIdx
            }
        }
    }
    return null
}

private fun MutableList<LongRange>.mergeIndices(idxOne: Int, idxTwo:Int) {
    val first = this[idxOne]
    val second = this[idxTwo]
    this[idxOne] = min(first.first, second.first) .. max(first.last, second.last)
    this.removeAt(idxTwo)
}
