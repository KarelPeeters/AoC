package y2017.w2.d11

import readText
import java.lang.Math.abs

val input = readText("y2017/w2/d11/input.txt")
val steps = input.split(",")

data class CubeCoord(val x: Int, val y: Int) {
    val z = -x - y
    val distance = (abs(x) + abs(y) + abs(z)) / 2

    operator fun plus(other: CubeCoord) = CubeCoord(x + other.x, y + other.y)
}

fun main(args: Array<String>) {

    var curr = CubeCoord(0, 0)
    steps.map {
        curr += when (it) {
            "n" -> CubeCoord(0, -1)
            "ne" -> CubeCoord(1, -1)
            "se" -> CubeCoord(1, 0)
            "s" -> CubeCoord(0, 1)
            "sw" -> CubeCoord(-1, 1)
            "nw" -> CubeCoord(-1, 0)
            else -> throw IllegalArgumentException()
        }
        curr.distance
    }.max().also { println("max distance: $it") }

    println("end distance: ${curr.distance}")
}

