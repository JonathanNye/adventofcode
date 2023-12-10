package aoc2023

import util.assertEqual
import util.readResourceLines
import kotlin.math.max

fun main() {
    val sampleInput = readResourceLines("/2023/day08sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 2)

    val problemInput = readResourceLines("/2023/day08input.txt")
    println(part1(problemInput))
    val sampleInput2 = readResourceLines("/2023/day08sample2.txt")
    val sampleSolution2 = part2(sampleInput2)
    assertEqual(sampleSolution2, 6)
    println(part2(problemInput))
}

private enum class Move { LEFT, RIGHT }

private data class Node(
    val id: String,
    val leftId: String,
    val rightId: String,
) {
    lateinit var leftRef: Node
    lateinit var rightRef: Node
}

private class CircularIterator<T>(private val items: List<T>): Iterator<T> {
    private var idx = 0
    override fun hasNext(): Boolean = items.isNotEmpty()

    override fun next(): T {
        val next = items[idx]
        idx += 1
        if (idx == items.size) {
            idx = 0
        }
        return next
    }

}

private fun buildInput(input: List<String>): Pair<List<Move>, Map<String, Node>> {
    val moves: List<Move> = input[0]
        .map {
            when (it) {
                'L' -> Move.LEFT
                'R' -> Move.RIGHT
                else -> throw IllegalArgumentException("Unknown move $it")
            }
        }
    val nodeLines = input.drop(2)
    val nodePattern = """(...) = \((...), (...)\)""".toRegex()
    val allNodes = nodeLines
        .map { line ->
            val (id, leftId, rightId) = nodePattern.matchEntire(line)!!.destructured
            Node(id, leftId, rightId)
        }
        .associateBy { it.id }
    allNodes
        .values
        .forEach {
            it.leftRef = allNodes[it.leftId] ?: throw IllegalStateException("Couldn't find ${it.leftId}")
            it.rightRef = allNodes[it.rightId] ?: throw IllegalStateException("Couldn't find ${it.rightId}")
        }
    return moves to allNodes
}

private fun part1(input: List<String>): Int {
    val (moves, allNodes) = buildInput(input)
    var currentNode = allNodes["AAA"] ?: throw IllegalStateException("Couldn't find start node AAA")
    var numMoves = 0
    val moveIterator = CircularIterator(moves)
    while (currentNode.id != "ZZZ") {
        currentNode = when (moveIterator.next()) {
            Move.LEFT -> currentNode.leftRef
            Move.RIGHT -> currentNode.rightRef
        }
        numMoves += 1
    }
    return numMoves
}

private fun part2(input: List<String>): Long {
    val (moves, allNodes) = buildInput(input)
    val startNodes = allNodes.values.filter { it.id.endsWith('A') }


    val movesToEnds = startNodes.map { startNode ->
        val moveIterator = CircularIterator(moves)
        var numMoves = 0L
        var currNode = startNode
        while (true) {
            numMoves += 1
            currNode = when(moveIterator.next()) {
                Move.LEFT -> currNode.leftRef
                Move.RIGHT -> currNode.rightRef
            }
            if (currNode.id.endsWith('Z')) {
                break
            }
        }
        numMoves
    }
    fun lcm(a: Long, b: Long): Long {
        val larger = max(a, b)
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }
    return movesToEnds.reduce(::lcm)
}