private val example = """dabAcCaCBAcCcaDA"""

val capitalDiff = 'a' - 'A'

fun main() {
    val input = readInput("day5.txt").first()

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: String): Int = react(input).length

private fun part2(input: String): Int {
    return ('A'..'Z').map { letter ->
        val polymer = input
            .replace(letter.toString(), "")
            .replace(letter.toString().toLowerCase(), "")
        react(polymer)
    }.map { it.length }.minBy { it } ?: -1
}

private fun react(input: String): String {
    var polymer = input
    //println(polymer)
    while (true) {
        val preReaction = polymer
        polymer.zipWithNext()
            .forEach { (a, b) ->
                val pair = "$a$b"
                //println("pair: $pair")
                if (Math.abs(a-b) == capitalDiff) {
                    //println("remove $pair")
                    polymer = polymer.replace(pair, "")
                }
            }
        if (preReaction.length != polymer.length) {
            //println(polymer)
        } else { break }
    }
    return polymer
}
