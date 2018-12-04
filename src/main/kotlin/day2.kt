fun main() {
    val input = readInput("day2.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val twos = input.sumBy { id -> if (multiplesOf(2, id) > 0) 1 else 0 }
    val threes = input.sumBy { id -> if (multiplesOf(3, id) > 0) 1 else 0 }
    return twos * threes
}

private fun multiplesOf(count: Int, id: String) = id
    .groupBy { letter -> letter }
    .values
    .filter { it.size == count }
    .count()

private fun part2(input: List<String>): String {
    val ids = input
        .filter { word -> input.map { diff(word, it) }.any { it == 1 } }
    return ids[0].zip(ids[1]).joinToString("") { (a, b) -> if (a == b) "$a" else "" }
}

private fun diff(first: String, second: String): Int =
    first.zip(second).count { (a, b) -> a != b }