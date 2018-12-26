package y2017.w2.d14

import y2017.w2.d10.knotHash
import java.math.BigInteger

val input = "nbysizxe"

fun bigIntKnotHash(input: String) = BigInteger(knotHash(input), 16)

fun main(args: Array<String>) {
    println((0 until 128).map { bigIntKnotHash("$input-$it").bitCount() }.sum())

    val grid = Grid((0 until 128).flatMap { x ->
        val int = bigIntKnotHash("$input-$x")
        (0 until 128).map { y ->
            Coord(x, y) to int.testBit(y)
        }
    }.toMap())

    println((0 until 128).sumBy { x ->
        (0 until 128).count { y ->
            clearConnected(Coord(x, y), grid)
        }
    })
}

fun clearConnected(start: Coord, grid: Grid): Boolean {
    if (!grid[start]) return false
    grid[start] = false

    listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)
            .map { Coord(start.x + it.first, start.y + it.second) }
            .filter { grid[it] }
            .forEach { clearConnected(it, grid) }

    return true
}

data class Coord(val x: Int, val y: Int)

class Grid(grid: Map<Coord, Boolean>) {
    private val grid = grid.toMutableMap()

    operator fun get(coord: Coord) = grid[coord] ?: false

    operator fun set(coord: Coord, value: Boolean) {
        grid[coord] = value
    }
}