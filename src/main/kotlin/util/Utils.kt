package util

fun readResourceLines(path: String): List<String> {
    return object {}.javaClass.getResourceAsStream(path)?.bufferedReader()?.readLines().orEmpty()
}

fun <T> assertEqual(actual: T, expected: T) {
    if (actual != expected) throw IllegalStateException("Expected $expected, got $actual")
}
