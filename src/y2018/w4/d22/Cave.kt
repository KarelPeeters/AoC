package y2018.w4.d22

import y2018.w4.d22.Terrain.*
import y2018.w4.d22.Tool.TORCH
import java.util.*
import kotlin.math.max

data class Point(val x: Int, val y: Int) {
    fun neighbors() = listOf(Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1))
            .filter { it.x >= 0 && it.y >= 0 }
}

enum class Terrain(val str: String) {
    ROCKY("."), WET("="), NARROW("|"),
}

enum class Tool(vararg val allowed: Terrain) {
    CLIMB(ROCKY, WET),
    TORCH(ROCKY, NARROW),
    NEITHER(WET, NARROW)
}

class Grid(val depth: Int, val target: Point) {
    private var erosion = IntArray(10) { -1 }

    private fun index(x: Int, y: Int) = (x + y) * (x + y + 1) / 2 + y

    private fun getErosion(x: Int, y: Int): Int {
        val index = index(x, y)

        //resize if necessary
        if (index > erosion.lastIndex) {
            val old = erosion
            erosion = IntArray(max(erosion.size, index) * 2) { -1 }
            old.copyInto(erosion, 0, 0)
        }

        //return if already calculated
        if (erosion[index] != -1)
            return erosion[index]

        //calculate
        val tmp = when {
            x == target.x && y == target.y -> 0
            y == 0 -> x * 16807
            x == 0 -> y * 48271
            else -> getErosion(x - 1, y) * getErosion(x, y - 1)
        }
        val new = (tmp + depth) % 20183
        erosion[index] = new
        return new
    }

    operator fun get(point: Point): Terrain {
        return Terrain.values()[getErosion(point.x, point.y) % 3]
    }

    //override fun toString() = erosion.joinToString("\n") { it.joinToString("") { Terrain.values()[it % 3].str } }
}


data class State(val point: Point, val tool: Tool, val time: Int) : Comparable<State> {
    override fun compareTo(other: State) = time.compareTo(other.time)
}

fun minTimeToTarget(grid: Grid, target: Point): Int? {
    val queue = PriorityQueue<State>()
    val visited = mutableMapOf<Pair<Point, Tool>, Int>()
    queue.add(State(Point(0, 0), TORCH, 0))

    while (queue.isNotEmpty()) {
        val (cPos, cTool, cTime) = queue.poll()
        if (cPos == target && cTool == TORCH)
            return cTime

        val nextStates = mutableListOf<State>()

        //move a tile
        for (nPos in cPos.neighbors())
            if (grid[nPos] in cTool.allowed)
                nextStates += State(nPos, cTool, cTime + 1)

        //switch tool
        for (nTool in Tool.values())
            if (nTool != cTool && grid[cPos] in nTool.allowed)
                nextStates += State(cPos, nTool, cTime + 7)

        //add if not already seen earlier
        for (next in nextStates) {
            val pair = next.point to next.tool
            val prevTime = visited[pair]
            if (prevTime == null || prevTime > next.time) {
                queue.add(next)
                visited[pair] = next.time
            }
        }
    }

    return null
}

fun main(args: Array<String>) {
    val depth = 3879
    val target = Point(8, 713)


    val grid = Grid(depth, target)
    println((0..target.x).sumBy { x -> (0..target.y).sumBy { y -> grid[Point(x, y)].ordinal } })
    println(minTimeToTarget(grid, target))
}