package y2017.w3.d19

import readText

val input = readText("y2017/w3/d19/input.txt")

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    operator fun unaryMinus() = Coord(-x, -y)
}

object Grid {
    val lines = input.lines()
    operator fun get(coord: Coord) = lines.getOrNull(coord.y)?.getOrNull(coord.x) ?: ' '
}

fun main(args: Array<String>) {
    var letters = ""
    var steps = 0

    var pos = Coord(Grid.lines[0].indexOf("|"), 0)
    var dir = Coord(0, 1)

    while (Grid[pos] != ' ') {
        steps++
        when (Grid[pos]) {
            '|', '-' -> Unit
            '+' -> dir = (listOf(Coord(1, 0), Coord(0, 1), Coord(-1, 0), Coord(0, -1)) - (-dir))
                    .first { Grid[pos + it] != ' ' }
            else -> letters += Grid[pos]
        }

        pos += dir
    }

    println(letters)
    println(steps)
}