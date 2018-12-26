package y2018.w1.d07

import readText
import timeNano
import java.util.*

/*val input = """
Step C must be finished before step A can begin.
Step C must be finished before step F can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.
""".trimIndent()*/

val input = readText("y2018/w1/d07/input.txt")

fun main(args: Array<String>) {
    val requiredBy = input.lines().groupBy({ it[5] }, { it[36] })
    val allSteps = requiredBy.keys + requiredBy.values.flatten()
    val requires = allSteps.associate { s -> s to requiredBy.filterValues { s in it }.keys }

    fun doSteps(workers: Int): Pair<String, Int> {
        val ready = TreeSet<Char>()
        val done = mutableSetOf<Char>()

        val timeLeft = IntArray(workers)
        val assigned = CharArray(workers)

        var str = ""
        var time = 0

        do {
            val minTime = timeLeft.filter { it > 0 }.min() ?: 0

            time += minTime
            for (w in 0 until workers) {
                if (timeLeft[w] != 0) {
                    timeLeft[w] -= minTime

                    if (timeLeft[w] == 0) {
                        done += assigned[w]
                        str += assigned[w]
                    }
                }
            }

            ready.addAll(allSteps.filter { it !in done && it !in assigned && done.containsAll(requires.getValue(it)) })

            for (w in 0 until workers) {
                if (timeLeft[w] == 0) {
                    val curr = ready.pollFirst() ?: break
                    assigned[w] = curr
                    timeLeft[w] = curr - 'A' + 61
                }

            }
        } while (timeLeft.any { it != 0 })

        return str to time
    }

    timeNano {
        println(doSteps(1))
    }
    timeNano {
        println(doSteps(5))
    }
}