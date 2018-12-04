
private data class Claim(val number: Int, val x: Int, val y: Int, val w: Int, val h: Int)

fun main() {
    val input = readInput("day3.txt")
//    val sample = """
//    #1 @ 1,3: 4x4
//    #2 @ 3,1: 4x4
//    #3 @ 5,5: 2x2
//    """
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val claims = parseClaims(input)
    val fabric = createFabric(claims)
//    printGrid(fabric)
    return fabric.sumBy {
        it.count { it > 1 }
    }
}

private fun part2(input: List<String>): Claim? {
    val claims = parseClaims(input)
    val fabric = createFabric(claims)
    return claims.find { claim ->
        with(claim) {
            (x until x + w).all {
                val column = fabric[it]
                (y until y + h).all {
                    column[it] == 1
                }
            }
        }
    }
}

private fun parseClaims(input: List<String>): List<Claim> {
    val pattern = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
    return input.mapNotNull { line ->
        pattern.find(line)?.let {
            val (number, x, y, w, h) = it.destructured
            Claim(number.toInt(), x.toInt(), y.toInt(), w.toInt(), h.toInt())
        }
    }
}

private fun createFabric(claims: List<Claim>): Array<Array<Int>> {
    val maxWidth: Int = claims.map { it.x + it.w }.max() ?: 1000
    val maxHeight: Int = claims.map { it.y + it.h }.max() ?: 1000

    val fabric: Array<Array<Int>> = Array(maxWidth + 1) { Array(maxHeight + 1) { 0 } }

    claims.forEach { claim ->
        with(claim) {
            (x until x + w).forEach {
                val column = fabric[it]
                (y until y + h).forEach {
                    column[it] += 1
                }
            }
        }
    }

    return fabric
}

private fun printGrid(grid: Array<Array<Int>>) {
    grid.forEach {
        it.forEach {
            print(
                when (it) {
                    0 -> "."
                    else -> it
                }
            )
        }
        println()
    }
}
