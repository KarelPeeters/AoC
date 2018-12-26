package y2018.w2.d10

import readText
import kotlin.math.max
import kotlin.math.min

val input = readText("y2018/w2/d10/input.txt")

class Star(var x: Int, var y: Int, val dx: Int, val dy: Int)

fun main(args: Array<String>) {
    val regex = """-?\d+""".toRegex()

    val stars = input.lines().map { line ->
        val (x, y, dx, dy) = regex.findAll(line).map { it.value.toInt() }.toList()
        Star(x, y, dx, dy)
    }

    for (i in generateSequence(1) { it + 1 }) {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE

        for (star in stars) {
            star.x += star.dx
            star.y += star.dy

            minX = min(minX, star.x)
            maxX = max(maxX, star.x)
            minY = min(minY, star.y)
            maxY = max(maxY, star.y)
        }

        if (maxY - minY == 9) {
            val grid = Array(maxY - minY + 1) { CharArray(maxX - minX + 1) { '.' } }
            for (star in stars)
                grid[star.y - minY][star.x - minX] = '#'

            println(i)
            println(grid.joinToString("\n") { String(it) })
            break
        }
    }
}