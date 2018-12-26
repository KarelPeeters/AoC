package y2018.w1.d06

import readText
import kotlin.math.abs

val input = readText("y2018/w1/d06/input2.txt")

data class Point(val x: Int, val y: Int) {
    fun distanceTo(otherX: Int, otherY: Int) = abs(x - otherX) + abs(y - otherY)
}

fun main(args: Array<String>) {
    val coords = input.lines().map {
        val (x, y) = it.split(", ").map(String::toInt)
        Point(x, y)
    }

    val minX = coords.minBy { it.x }!!.x
    val maxX = coords.maxBy { it.x }!!.x
    val minY = coords.minBy { it.y }!!.y
    val maxY = coords.maxBy { it.y }!!.y

    val areas = coords.associateTo(mutableMapOf()) { it to 0 }
    var safeSize = 0

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (coords.sumBy { it.distanceTo(x, y) } < 10_000)
                safeSize++

            val closest = coords.minBy { it.distanceTo(x, y) }!!
            val smallestDist = closest.distanceTo(x, y)
            if (coords.count { it.distanceTo(x, y) == smallestDist } > 1)
                continue

            if (x == minX || x == maxX || y == minY || y == maxY)
                areas.remove(closest)
            areas[closest] = 1 + (areas[closest] ?: continue)
        }
    }

    println(areas.values.max())
    println(safeSize)
}