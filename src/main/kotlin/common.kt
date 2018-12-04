import java.io.File

fun readInput(fileName: String): List<String> =
    File(Int::class.java.getResource("input/$fileName").toURI())
        .readLines()
        .filter { it.isNotBlank() }
