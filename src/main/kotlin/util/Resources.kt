package util

fun readResourceLines(path: String): List<String> {
    return object {}.javaClass.getResourceAsStream(path)?.bufferedReader()?.readLines().orEmpty()
}