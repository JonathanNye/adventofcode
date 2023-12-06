package aoc2023

import util.assertEqual
import util.readResource
import kotlin.math.max
import kotlin.math.min

fun main() {
    val sampleInput = readResource("/2023/day05sample.txt")
    val sampleSolution = part1(sampleInput)
    assertEqual(sampleSolution, 35L)

    val problemInput = readResource("/2023/day05input.txt")
    println(part1(problemInput))

    val sampleSolution2 = part2(sampleInput)
    assertEqual(sampleSolution2, 46)
    println(part2(problemInput))
}

private data class Mapper(
    val dstStart: Long,
    val srcStart: Long,
    val length: Long
) {

    val srcRange = LongRange(srcStart, srcStart + length - 1)

    val delta = dstStart - srcStart
    fun map(input: Long): Long? {
        return if (input in srcRange) {
            input + delta
        } else {
            null
        }
    }
}
private val numberRegex = """\d+""".toRegex()
private fun parseInput(input: String): Pair<String, List<List<Mapper>>> {
    val inputSections = input.split("\n\n")

    val phases = inputSections
        .drop(1) // omit seeds section
        .map { sectionString ->
            sectionString
                .split("\n")
                .drop(1) // omit "x-to-y map:", we just care about the ordering
                .map { sectionMapperLine ->
                    val numbers = numberRegex.findAll(sectionMapperLine)
                        .map { it.value.toLong() }
                        .toList()
                    Mapper(
                        dstStart = numbers[0],
                        srcStart = numbers[1],
                        length = numbers[2],
                    )
                }
        }
    return inputSections[0] to phases
}

private fun part1(input: String): Long {

    val (seedsLine, phases) = parseInput(input)

    val seeds = numberRegex
        .findAll(seedsLine)
        .map { it.value.toLong() }
        .toList()

    fun List<Mapper>.map(input: Long): Long {
        return this.firstNotNullOfOrNull { mapper ->
            mapper.map(input)
        } ?: input // just use the same value if the input wasn't in any ranges
    }

    return seeds
        .map { seed ->
            phases.fold(seed) { acc, mappers ->
                mappers.map(acc)
            }
        }
        .min()
}

private fun part2(input: String): Long {
    val (seedsLine, phases) = parseInput(input)

    val initialRanges = numberRegex
        .findAll(seedsLine)
        .map { it.value.toLong() }
        .toList()
        .windowed(size = 2, step = 2, partialWindows = false) { seedRangeTokens ->
            val start = seedRangeTokens[0]
            LongRange(start, start + seedRangeTokens[1] - 1)
        }

    fun LongRange.overlaps(other: LongRange) = this.last >= other.first && this.first <= other.last
    fun LongRange.offsetBy(delta: Long) = LongRange(this.first + delta, this.last + delta)

    val processedRanges = phases.fold(initialRanges) { ranges, phase ->
        ranges.flatMap { range ->
            val applicableMappers = phase
                .filter { mapper ->
                    range.overlaps(mapper.srcRange)
                }
            if (applicableMappers.isEmpty()) {
                listOf(range)
            } else {
                buildList {
                    val firstApplicable = applicableMappers.first()
                    if (range.first < firstApplicable.srcRange.first) {
                        add(LongRange(range.first, firstApplicable.srcRange.first - 1))
                    }

                    applicableMappers.forEach { mapper ->
                        add(
                            LongRange(
                                start = max(range.first, mapper.srcRange.first),
                                endInclusive = min(range.last, mapper.srcRange.last)
                            ).offsetBy(mapper.delta)
                        )
                    }

                    val lastApplicable = applicableMappers.last()
                    if (range.last > lastApplicable.srcRange.last) {
                        add(LongRange(lastApplicable.srcRange.last + 1, range.last))
                    }
                }
            }
        }
    }

    return processedRanges.minBy { it.first }.first
}
