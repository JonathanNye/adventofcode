package util

fun readResource(path: String): String {
    return object {}.javaClass.getResourceAsStream(path)?.bufferedReader()?.readText().orEmpty()
}

fun readResourceLines(path: String): List<String> {
    return object {}.javaClass.getResourceAsStream(path)?.bufferedReader()?.readLines().orEmpty()
}

fun <T> assertEqual(actual: T, expected: T) {
    if (actual != expected) throw IllegalStateException("Expected $expected, got $actual")
}

fun <T, U> solveAndAssert(
    year: String,
    day: String,
    reader: (String) -> U,
    part1: Pair<(U) -> T, T>,
    part2: Pair<(U) -> T, T>? = null,
) {
    val sampleInput = reader("/$year/day${day}sample.txt")
    val sampleSolution = part1.first(sampleInput)
    assertEqual(sampleSolution, part1.second)

    val problemInput = reader("/$year/day${day}input.txt")
    println(part1.first(problemInput))

    if (part2 != null) {
        val sampleSolution2 = part2.first(sampleInput)
        assertEqual(sampleSolution2, part2.second)
        println(part2.first(problemInput))
    }
}

fun <T> solveAndAssert(
    year: String,
    day: String,
    part1: Pair<(List<String>) -> T, T>,
    part2: Pair<(List<String>) -> T, T>? = null,
) = solveAndAssert(year, day, ::readResourceLines, part1, part2)