package y2018.w1.d04

import readText

val input = readText("y2018/w1/d04/input.txt")
//val input = readText("y2018/w1/d04/test.txt")

class Date(val y: Int, val m: Int, val d: Int, val h: Int, val mi: Int) : Comparable<Date> {
    override fun compareTo(other: Date) =
            compareValuesBy(this, other, { it.y }, { it.m }, { it.d }, { it.h }, { it.mi })
}

fun main(args: Array<String>) {

    val lines = input.lines()

    val dateRegex = """\[(\d+)-(\d+)-(\d+) (\d+):(\d+)]""".toRegex()

    val records = lines.map {
        val (y, m, d, h, mi) = dateRegex.matchEntire(it.substring(0, 18))!!.groupValues.drop(1).map(String::toInt)
        Date(y, m, d, h, mi) to it.substring(19)
    }.sortedBy { it.first }

    val sleepCount = mutableMapOf<Int, IntArray>()

    var guard: Int = -1
    var sleepStart: Int = -1

    for ((date, rec) in records) {
        when (rec) {
            "falls asleep" -> sleepStart = date.mi
            "wakes up" -> {
                val arr = sleepCount.getValue(guard)
                for (i in sleepStart until date.mi)
                    arr[i] += 1
            }
            else -> {
                guard = rec.split(' ')[1].removePrefix("#").toInt()
                sleepCount.putIfAbsent(guard, IntArray(60))
            }
        }
    }

    //part 1
    val g1 = sleepCount.maxBy { it.value.sum() }!!.key
    val m1 = sleepCount.getValue(g1).withIndex().maxBy { it.value }!!.index
    println(g1 * m1)

    //part 2
    val (g2,m2) = sleepCount.map { (g,a) ->
        g to a.withIndex().maxBy { it.value }!!
    }.maxBy { it.second.value }!!
    println(g2 * m2.index)
}