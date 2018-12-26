package y2018.infi

import java.util.*
import kotlin.math.abs

val input = """
╔╗═╩╚╗═╚║╦╣═╦║║╬║╩╗╔
║╔╩╗╚╔╚╗╩╗╔╗╦║═╩╩╩╝═
╠╔╣╬╠╗╣╩╚╩╝╚╠╠══╠╠╝╔
╚╝╠╩╗╠╣╝═╝╚╣╔╠╔║╦╣╩╠
╚╗╬╠╣╩╣╗╝╚╣╣╝═╩╗║╚╠═
╩╠═╠╣╠╔╗╔╠╔║═╗╝╝║║╚╗
╩╝║╠╣╩╦║╗╠╩╔╦╝══╦╗╦║
╝╠╔╬╝═╚══╦═╩╔╗╦╦╚╠╔║
║═╠╠╬╗╦╚╚╚╝║═╚╔╔═╦╣╠
╣╬╠╠═╗╚╗╝╝║╔╠╔╦╠╚╬╗╔
║══║║╣╩╠╠╔╬╦╠║╚╚╝╗╗╦
║╬╠║╦║╚╝╩║╣╚╝╝║╩╝╔╦╣
═╔╠╚╩╦═╬╣║╦╝╚╝╗║╠╣║╠
╬╣║╝╦╠╗║║╦╝══╝║╔═╝╠╩
╩═╚╣╗║╣╬╚╦╣╔╗║╬═║╦╩╬
╗╦║╩╗╔╗╗═╝╚╗╗╗║╝║╝╠╝
╝╚╦╦╩═╠╠╗╩╚╠╣╔╩╔╣═╣╚
╝╚╣║╣╗╚╚╠╣╔║╝╗═╚╩╩═╦
╦╣╔╚╚╚║╔╠╝═║╦═╬═╬╔═╣
╠╝╬╠╠═╚╗╗╣╚╩╩╣╩╩╬╬╩║
""".trimIndent()

val charMap = mapOf(
        '║' to b(1, 0, 0, 1),
        '═' to b(0, 1, 1, 0),
        '╔' to b(0, 1, 0, 1),
        '╝' to b(1, 0, 1, 0),
        '╗' to b(0, 0, 1, 1),
        '╚' to b(1, 1, 0, 0),
        '╠' to b(1, 1, 0, 1),
        '╣' to b(1, 0, 1, 1),
        '╦' to b(0, 1, 1, 1),
        '╩' to b(1, 1, 1, 0),
        '╬' to b(1, 1, 1, 1)
)

val dirs = arrayOf(
        0 to -1,
        1 to 0,
        -1 to 0,
        0 to 1
)


fun b(up: Int, right: Int, left: Int, down: Int) = booleanArrayOf(up != 0, right != 0, left != 0, down != 0)

data class Maze(
        val grid: List<List<BooleanArray>>,
        val type: Boolean = true,
        val n: Int = 0
) {
    val size = grid.size

    private var shifted: Maze? = null

    fun shifted(x: Int, y: Int): Triple<Maze, Int, Int> {
        var nx = x
        var ny = y
        if (type && y == n) nx = (x + 1) % size
        else if (!type && x == n) ny = (y + 1) % size

        if (shifted == null) {
            shifted = Maze(if (type) {
                grid.mapIndexed { y, row -> if (y == n) List(size) { row[(it - 1 + size) % size] } else row }
            } else {
                grid.mapIndexed { y, row -> row.mapIndexed { x, cell -> if (x == n) grid[(y - 1 + size) % size][x] else cell } }
            }, !type, (n + 1) % size)
        }

        return Triple(shifted!!, nx, ny)
    }

    override fun toString() = grid.joinToString("\n") { it.joinToString("") { b -> charMap.entries.first { Arrays.equals(it.value, b) }.key.toString() } }
}

data class State(val x: Int, val y: Int, val dist: Int, val maze: Maze, val parent: State?, val lastStep: Int?) : Comparable<State> {
    fun score() = dist + (abs(maze.size - x) + abs(maze.size - y)) / 2

    override fun compareTo(other: State): Int {
        return this.score().compareTo(other.score())
    }
}

fun calcLength(start: Maze, shift: Boolean): State {
    val size = start.size

    val toVisit = PriorityQueue<State>()
    toVisit.add(State(0, 0, 0, start, null, null))

    val visited = mutableSetOf<Int>()
    while (true) {
        val state = toVisit.poll()
        val (x, y, d, maze) = state
        visited.add(31 * (31 * x + y) + maze.hashCode())

        for ((i, delta) in dirs.withIndex()) {
            val (dx, dy) = delta
            val nx = x + dx
            val ny = y + dy

            if (nx in 0 until size && ny in 0 until size && maze.grid[y][x][i] && maze.grid[ny][nx][3 - i]) {
                val (nextMaze, sx, sy) = if (shift) maze.shifted(nx, ny) else Triple(maze, nx, ny)

                if ((nx == size - 1 && ny == size - 1) || (sx == size - 1 && sy == size - 1))
                    return State(sx, sy, d + 1, nextMaze, state, i)

                if ((31 * (31 * sx + sy) + nextMaze.hashCode()) !in visited)
                    toVisit.add(State(sx, sy, d + 1, nextMaze, state, i))
            }
        }
    }
}

fun main(args: Array<String>) {
    val maze = Maze(input.lines().map { s -> s.map { charMap[it]!! } })


    println(calcLength(maze, false).dist)
    println(calcLength(maze, true).dist)
}