package y2018.w3.d18

import readText

val input = readText("y2018/w3/d18/input.txt")

class Grid(val arr: Array<CharArray>) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as Grid

        return arr.contentDeepEquals(other.arr)
    }

    override fun hashCode() = arr.contentDeepHashCode()

    override fun toString() = arr.joinToString("\n") { String(it) }
}

val dirs = (-1..1).flatMap { dx -> (-1..1).map { dy -> dx to dy } }.filterNot { it.first == 0 && it.second == 0 }

fun calcResourceValue(iterations: Int): Int {
    var grid = input.lines().map { it.toCharArray() }.toTypedArray()
    val size = grid.size

    val seen = mutableMapOf<Grid, Int>()

    var i = 0
    while (i < iterations) {
        seen[Grid(grid)] = i
        val next = Array(size) { CharArray(size) }

        for (x in 0 until size) {
            for (y in 0 until size) {
                val treeCount = dirs.count { (dx, dy) -> grid.getOrNull(y + dy)?.getOrNull(x + dx) == '|' }
                val lumberCount = dirs.count { (dx, dy) -> grid.getOrNull(y + dy)?.getOrNull(x + dx) == '#' }

                val curr = grid[y][x]
                next[y][x] = when {
                    curr == '.' && treeCount >= 3 -> '|'
                    curr == '|' && lumberCount >= 3 -> '#'
                    curr == '#' && (treeCount == 0 || lumberCount == 0) -> '.'
                    else -> grid[y][x]
                }
            }
        }
        i++

        val nextGrid = Grid(next)
        val prevSeenIndex = seen[nextGrid]
        grid = nextGrid.arr

        if (prevSeenIndex != null) {
            i = iterations - ((iterations - i) % (i - prevSeenIndex))
        }
    }

    val treeCount = grid.sumBy { it.count { it == '|' } }
    val lumberCount = grid.sumBy { it.count { it == '#' } }

    return treeCount * lumberCount
}

fun main(args: Array<String>) {
    println(calcResourceValue(10))
    println(calcResourceValue(1_000_000_000))
}