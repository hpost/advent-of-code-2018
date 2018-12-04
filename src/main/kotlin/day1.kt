fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    return readInput("day1.txt")
        .map { it.toInt() }
        .fold(0) { acc, value -> acc + value }
}

private fun part2(): Int {
    val seenFrequencies = mutableSetOf<Int>()
    val input = readInput("day1.txt").map { it.toInt() }
    var acc = 0
    while (true) {
        acc = input.fold(acc) { acc, value ->
            val freq = acc + value
            if (freq in seenFrequencies) {
                return freq
            }
            seenFrequencies += freq
            freq
        }
    }
}
