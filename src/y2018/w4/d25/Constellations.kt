package y2018.w4.d25

import readText
import java.lang.Math.abs
import java.util.*

val input = readText("y2018/w4/d25/input.txt")

typealias Point = List<Int>

fun Point.dist(other: Point) = this.zip(other).sumBy { (a, b) -> abs(a - b) }

fun main(args: Array<String>) {

    val points = input.lines().map { it.split(',').map { it.trim().toInt() } }

    val freePoints = ArrayDeque(points.toMutableSet())
    val constellations = mutableListOf<Set<Point>>()

    while (freePoints.isNotEmpty()) {
        val start = freePoints.remove()
        val queue = ArrayDeque<Point>()
        queue.add(start)

        val constellation = mutableSetOf<Point>()

        while (queue.isNotEmpty()) {
            val curr = queue.poll()
            val new = freePoints.filter { it !in constellation && it.dist(curr) <= 3 }
            freePoints.removeAll(new)
            constellation.addAll(new)
            queue.addAll(new)
        }

        constellations.add(constellation)
    }

    println(constellations.size)

}
