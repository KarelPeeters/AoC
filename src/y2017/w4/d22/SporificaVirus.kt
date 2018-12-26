package y2017.w4.d22

import readText
import y2017.w4.d22.State.*

val text = readText("y2017/w4/d22/input.txt")
val inputSize = text.lines()[0].length

fun main(args: Array<String>) {
    readGrid()
    println(walk(10_000, CLEAN, INFECTED, CLEAN))
    readGrid()
    println(walk(10_000_000, CLEAN, WEAK, INFECTED, FLAGGED, CLEAN))
}

fun readGrid() {
    grid.reset()
    text.lineSequence().withIndex().forEach { (y, l) ->
        l.toCharArray().withIndex().forEach { (x, c) ->
            grid[Coord(x, inputSize - y - 1)] = if (c == '#') INFECTED else CLEAN
        }
    }
}

fun walk(steps: Int, vararg mapping: State): Int {
    var count = 0
    var pos = Coord(inputSize / 2, inputSize / 2)
    var dir = Coord(0, 1)

    repeat(steps) {
        dir = when (grid[pos]) {
            CLEAN -> dir.left()
            WEAK -> dir
            INFECTED -> dir.right()
            FLAGGED -> dir.reverse()
        }

        grid[pos] = mapping.asList().zipWithNext().first { it.first == grid[pos] }.second
        if (grid[pos] == INFECTED) count++

        pos += dir
    }

    return count
}

enum class State {
    CLEAN, WEAK, INFECTED, FLAGGED
}

data class Coord(val x: Int, val y: Int) {
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)

    fun left() = Coord(-y, x)
    fun right() = Coord(y, -x)
    fun reverse() = Coord(-x, -y)
}

object grid {
    var map = mutableMapOf<Coord, State>()

    operator fun get(coord: Coord) = map[coord] ?: CLEAN
    operator fun set(coord: Coord, value: State) {
        map[coord] = value
    }

    fun reset() {
        map = mutableMapOf()
    }
}