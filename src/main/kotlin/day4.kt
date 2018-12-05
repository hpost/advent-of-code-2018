private val example = """
[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up
""".lines()

private data class Record(
    val date: String,
    val hour: Int,
    val minute: Int,
    val event: String
)

private data class Day(
    val id: Int,
    val date: String,
    val minutes: List<Boolean> // true if asleep during that minute
)

fun main() {
    val input = readInput("day4.txt")

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val records = parseRecords(input)
    val days = parseDays(records)
    printDays(days)
    val mostMinutesAsleep = days.groupBy { it.id }
        .entries
        .map { (id, days) ->
            id to days.sumBy { it.minutes.count { it } }
        }
        .maxBy { (_, count) -> count }
    println("most minutes: ${mostMinutesAsleep?.second} by guard #${mostMinutesAsleep?.first}")
    val guardId = mostMinutesAsleep?.first ?: -1
    val guardDays = days.filter { it.id == guardId }
    val (bestMinute, asleepCount) = (0..59)
        .map { minute -> minute to guardDays.sumBy { if (it.minutes[minute]) 1 else 0 } }
        .maxBy { (_, count) -> count }!!
    println("$asleepCount times asleep during minute $bestMinute")
    return guardId * bestMinute
}

private fun part2(input: List<String>): Int {
    val records = parseRecords(input)
    val days = parseDays(records)
    val (guard, minute, count) = days
        .groupBy { it.id }
        .map { (guard, days) ->
            (0..59).map { minute ->
                Triple(guard, minute, days.sumBy { if (it.minutes[minute]) 1 else 0 })
            }.maxBy { (_, _, count) -> count }!! // most frequent minute per guard
        }
        .maxBy { (_, _, count) -> count }!!

    println("Guard: $guard, Minute: $minute, Count: $count")
    return guard * minute
}

private fun parseRecords(input: List<String>): List<Record> {
    val pattern = """\[(\d{4}-\d{2}-\d{2}) (\d{2}):(\d{2})] (.*$+)""".toRegex()
    return input
        .sortedBy { it.substring(0..it.indexOf(']')) }
        .mapNotNull { line ->
            pattern.find(line)?.let {
                val (date, hour, minute, event) = it.destructured
                Record(date, hour.toInt(), minute.toInt(), event)
            }
        }
}

private fun parseDays(records: List<Record>) =
    records.filter { it.event.contains('#') }
        .mapNotNull { guardEvent ->
            val shift = records
                .dropWhile { it != guardEvent } // find guard event
                .drop(1) // drop guard event
                .takeWhile { it.event.contains('#').not() } // take until next guard event

            val idRegex = """Guard #(\d+) begins shift""".toRegex()
            val guard = idRegex.find(guardEvent.event)?.destructured?.component1()?.toInt() ?: -1
            val asleep = MutableList(60) { false }.apply {
                val fallingAsleep = shift.filterIndexed { index, _ -> index % 2 == 0 }
                val wakingUp = shift.filterIndexed { index, _ -> index % 2 == 1 }
                fallingAsleep.zip(wakingUp)
                    .forEach { (fallingAsleep, wakingUp) ->
                        (fallingAsleep.minute until wakingUp.minute).forEach {
                            this[it] = true
                        }
                    }
            }
            shift.firstOrNull()?.let {
                Day(guard, it.date, asleep)
            }
        }

private fun printDays(days: List<Day>) {
    println("Date          ID  Minute")
    days.forEach {
        println(with(it) {
            "$date  ${"%4d".format(id)}  ${minutes.joinToString("") { if (it) "#" else "." }}"
        })
    }
    println()
}
