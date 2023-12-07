package aoc2023

import util.assertEqual
import util.readResource
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    val sampleInput = listOf(
        7 to 9,
        15 to 40,
        30 to 200,
    )
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 288)
    val problemInput = listOf(
        46 to 347,
        82 to 1522,
        84 to 1406,
        79 to 1471,

    )
    println(part1(problemInput))

    val sampleSolution2 = part2(71530L, 940200L)
    assertEqual(sampleSolution2, 71503)
    println(part2(46828479L, 347152214061471L))
}
private fun part1(input: List<Pair<Int, Int>>): Int {
    fun distanceForRace(timeHeld: Int, raceTime: Int): Int {
        return timeHeld * (raceTime - timeHeld)
    }
    return input.map { (raceTime, distanceToBeat) ->
        (1 until raceTime).map { timeToHold ->
            distanceForRace(timeToHold, raceTime)
        }.count { it > distanceToBeat }
    }.reduce(Int::times)
}

private fun part2(raceTime: Long, distanceToBeat: Long): Int {
    val firstRoot = (-raceTime + sqrt(raceTime.toDouble() * raceTime - 4 * -1 * -distanceToBeat)) / (-2L)
    val secondRoot = (-raceTime - sqrt(raceTime.toDouble() * raceTime - 4 * -1 * -distanceToBeat)) / (-2L)
    return floor(secondRoot).roundToInt() - floor(firstRoot).roundToInt()
}