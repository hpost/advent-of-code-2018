
val example = """Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin."""

fun main() {
    val input = readInput("day7.txt")

    println("Part 1: ${part1(input)}")
}

private fun parse(input: List<String>): MutableMap<String, List<String>> {
    val regex = """Step (\w) must be finished before step (\w) can begin.""".toRegex()
    val dependencies: MutableMap<String, List<String>> = mutableMapOf()
    input.forEach { line ->
        val (dependency, step) = regex.find(line)!!.destructured
        dependencies[step] = dependencies[step].orEmpty() + listOf(dependency)
    }
    dependencies.values.flatten().filter { it !in dependencies.keys }.forEach {
        dependencies[it] = emptyList()
    }
    return dependencies
}

private fun part1(input: String): String {
    val dependencies = parse(input.lines())
    var result = ""
    
    while (dependencies.isNotEmpty()) {
        println(dependencies)
        dependencies.keys.sorted().find { dependencies[it]?.isEmpty() ?: true }?.let { step ->
        	result += step
       		dependencies.remove(key = step)
            // TODO remove step from every dependency
            dependencies.keys.forEach {
                dependencies[it] = dependencies[it].orEmpty().filter { it != step }
            }
        }
    }
    
    return result
}
