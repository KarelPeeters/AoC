@file:Suppress("NAME_SHADOWING")

package y2018.w3.d17

import readText
import y2018.w3.d17.Tile.*

val input = readText("y2018/w3/d17/input.txt")

fun parseRange(str: String): IntRange {
    val nums = str.split("..").map { it.toInt() }
    return nums[0]..nums.getOrElse(1) { nums[0] }
}

enum class Tile(val symbol: String) {
    SAND(" "), CLAY("#"), STILL("~"), FLOWING("|")
}

class Grid {
    var xMin: Int = -1
    var xMax: Int = -1
    var yMin: Int = -1
    var yMax: Int = -1

    val tiles = mutableMapOf<Pair<Int, Int>, Tile>()

    operator fun get(x: Int, y: Int) = tiles[x to y] ?: SAND
    operator fun set(x: Int, y: Int, tile: Tile) {
        tiles[x to y] = tile
    }

    override fun toString(): String {
        return "x=$xMin..$xMax, y=$yMin..$yMax\n" + (yMin .. yMax).joinToString("\n") { y ->
            (xMin .. xMax).joinToString("") { x -> get(x,y).symbol }
        }
    }

    fun calcBounds() {
        xMin = tiles.keys.minBy { it.first }!!.first
        xMax = tiles.keys.maxBy { it.first }!!.first
        yMin = tiles.keys.minBy { it.second }!!.second
        yMax = tiles.keys.maxBy { it.second }!!.second
    }
}

fun canFlowOn(tile: Tile) = tile == CLAY ||tile == STILL

fun findEnd(grid: Grid, x: Int, y: Int, dir: Int): Int {
    var cx = x
    while (grid[cx + dir, y] != CLAY && canFlowOn(grid[cx+ dir, y + 1])) {
        grid[cx+dir, y] = FLOWING
        cx += dir
    }
    val free = grid[cx + dir, y] != CLAY
    if (free)
        flow(grid, cx+dir, y)
    return cx
}

fun flow(grid: Grid, x: Int, srcY: Int) {
    //move down until we hit clay, flowing water or the end of the map
    var cy = srcY
    while (true) {
        grid[x, cy] = FLOWING

        if (cy + 1 > grid.yMax || grid[x,cy+1] == FLOWING)
            return
        if (grid[x, cy+1] == CLAY)
            break
        cy++
    }

    do {
        //don't fill above the source Y
        if (cy <= srcY)
            break

        var free: Boolean

        var leftEnd = findEnd(grid, x, cy, -1)
        var rightEnd = findEnd(grid, x, cy, 1)

        //repeatedly find ends
        while (true) {
            val newLeftEnd = findEnd(grid, x, cy, -1)
            val newRightEnd = findEnd(grid, x, cy, 1)

            free = grid[newLeftEnd-1, cy] != CLAY || grid[newRightEnd+1, cy] != CLAY

            if (newLeftEnd == leftEnd && newRightEnd == rightEnd)
                break
            leftEnd = newLeftEnd
            rightEnd = newRightEnd
        }

        if (!free) {
            for (cx in leftEnd .. rightEnd)
                grid[cx, cy] = STILL
        }

        cy--
    } while (!free)
}

fun main(args: Array<String>) {
    val grid = Grid()

    for (line in input.lineSequence()) {
        val ranges = line.split(", ").associate {
            val (coord, range) = it.split("=")
            coord to parseRange(range)
        }

        for (x in ranges.getValue("x"))
            for (y in ranges.getValue("y"))
                grid[x,y] = CLAY
    }

    grid.calcBounds()
    flow(grid, 500, grid.yMin)

    println(grid.tiles.values.count { it == STILL || it == FLOWING })
    println(grid.tiles.values.count { it == STILL })
}