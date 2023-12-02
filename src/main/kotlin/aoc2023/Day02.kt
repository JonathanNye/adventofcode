package aoc2023

import util.assertEqual
import util.readResourceLines
import kotlin.math.max

fun main() {
    val sampleInput = readResourceLines("/2023/day02sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 8)

    val problemInput = readResourceLines("/2023/day02input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 2286)
    println(part2(problemInput))
}

private data class Game(
    val id: Int,
    val trials: Collection<CubeSet>,
)

private data class CubeSet(
    val reds: Int,
    val blues: Int,
    val greens: Int,
)

private val gamePattern = Regex("""Game (\d+): (.+)""")
private val trialCubePattern = Regex("""\s*(\d+) (.+)\s*""")

private fun parseGame(line: String): Game {
    val matchedGame = gamePattern.matchEntire(line)!!
    val gameId = matchedGame.groupValues[1].toInt()
    val trialStrings = matchedGame.groupValues[2].split(";")
    val trials = trialStrings.map { trialString ->
        val cubes = trialString.split(",")
            .map { cubeString ->
                val matchedCubes = trialCubePattern.matchEntire(cubeString)!!
                val numCubes = matchedCubes.groupValues[1].toInt()
                numCubes to matchedCubes.groupValues[2]
            }
        val numRed = cubes.find { it.second == "red" }?.first ?: 0
        val numGreen = cubes.find { it.second == "green" }?.first ?: 0
        val numBlue = cubes.find { it.second == "blue" }?.first ?: 0
        CubeSet(reds = numRed, greens = numGreen, blues = numBlue)
    }
    return Game(gameId, trials)
}

private fun part1(lines: List<String>): Int {
    val games = lines.map { parseGame(it) }
    fun Game.isPossible(maxReds: Int, maxGreens: Int, maxBlues: Int): Boolean {
        return trials.all { trial ->
            trial.reds <= maxReds &&
                    trial.greens <= maxGreens &&
                    trial.blues <= maxBlues
        }
    }
    return games
        .filter { it.isPossible(maxReds = 12, maxGreens = 13, maxBlues = 14) }
        .sumOf { it.id }
}

private fun part2(lines: List<String>): Int {
    val games = lines.map { parseGame(it) }
    val minSets = games.map { game ->
        game.trials.fold(CubeSet(0, 0, 0)) { acc, next ->
            CubeSet(
                reds = max(acc.reds, next.reds),
                greens = max(acc.greens, next.greens),
                blues = max(acc.blues, next.blues)
            )
        }
    }
    return minSets.sumOf {
        it.reds * it.greens * it.blues
    }
}
