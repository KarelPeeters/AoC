package y2018.w3.d20

import readText
import java.util.*

val input = readText("y2018/w3/d20/input.txt")
//val input = "E(N|S)E"

data class Point(val x: Int, val y: Int) {
    fun move(dir: Char) = when (dir) {
        'N' -> Point(x, y + 1)
        'E' -> Point(x + 1, y)
        'S' -> Point(x, y - 1)
        'W' -> Point(x - 1, y)
        else -> error("direction $dir")
    }
}

typealias Connections = MutableMap<Point, MutableSet<Point>>

fun Connections.connect(first: Point, second: Point) {
    getOrPut(first) { mutableSetOf() } += second
    getOrPut(second) { mutableSetOf() } += first
}

sealed class Regex {
    abstract fun walk(conn: Connections, start: Point): Set<Point>

    class Either(val list: List<Regex>) : Regex() {
        override fun walk(conn: Connections, start: Point) =
                list.flatMapTo(mutableSetOf()) { it.walk(conn, start) }

        override fun toString() = list.joinToString("|", "(", ")")
    }

    class Sequence(val list: List<Regex>) : Regex() {
        override fun walk(conn: Connections, start: Point) =
                list.fold(setOf(start)) { points, r ->
                    points.flatMapTo(mutableSetOf()) { r.walk(conn, it) }
                }

        override fun toString() = list.joinToString("")
    }

    class Simple(val str: String) : Regex() {
        override fun walk(conn: Connections, start: Point) =
                setOf(str.fold(start) { point, dir ->
                    point.move(dir).also { conn.connect(point, it) }
                })

        override fun toString() = str
    }
}

val stopChars = listOf(')', '|', null)

fun parseSequence(input: Queue<Char>): Regex {
    val list = mutableListOf<Regex>()
    while (input.peek() !in stopChars) {
        list += parseAtom(input)
    }
    return Regex.Sequence(list)
}

fun parseAtom(input: Queue<Char>): Regex =
        if (input.peek() == '(') {
            val list = mutableListOf<Regex>()
            while (input.poll() != ')') {
                list += parseSequence(input)
            }
            Regex.Either(list)
        } else {
            var str = ""
            while (input.peek()?.isLetter() == true)
                str += input.poll()
            Regex.Simple(str)
        }

fun main(args: Array<String>) {
    val cleanInput = input.replace("^", "").replace("\$", "")
    val inputQueue = LinkedList(cleanInput.toList())
    val regex = parseSequence(inputQueue)

    val conn: Connections = mutableMapOf()
    val start = Point(0, 0)
    regex.walk(conn, start)

    println(pathfind(start, conn))

    println(Day20(input).solvePart1())
}

fun pathfind(start: Point, conn: Connections): Pair<Int, Int> {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Pair<Point, Int>>()

    queue.offer(start to 0)

    var maxDist = 0
    var farCount = 0

    while (queue.isNotEmpty()) {
        val (curr, dist) = queue.poll()
        visited += curr

        maxDist = dist
        if (dist >= 1000)
            farCount++

        queue.addAll(conn.getValue(curr).filter { it !in visited }.map { it to dist + 1 })
    }

    return maxDist to farCount
}

class Day20(rawInput: String) {

    private val grid: Map<Point, Int> = parseGrid(rawInput)

    fun solvePart1(): Int =
            grid.maxBy { it.value }!!.value

    fun solvePart2(): Int =
            grid.count { it.value >= 1000 }


    private fun parseGrid(input: String): Map<Point, Int> {
        val grid = mutableMapOf(startingPoint to 0)
        val stack = ArrayDeque<Point>()
        var current = startingPoint
        input.forEach {
            when (it) {
                '(' -> stack.push(current)
                ')' -> current = stack.pop()
                '|' -> current = stack.peek()
                in movementRules -> {
                    // If we are moving to a spot we haven't seen before, we can
                    // record this as a new distance.
                    val nextDistance = grid.getValue(current) + 1
                    current = movementRules.getValue(it).invoke(current)
                    if (current !in grid) grid[current] = nextDistance
                }
            }
        }
        return grid
    }

    companion object {
        private val startingPoint = Point(0, 0)
        private val movementRules = mapOf<Char, (Point) -> Point>(
                'N' to { p -> p.move('N') },
                'S' to { p -> p.move('S') },
                'E' to { p -> p.move('E') },
                'W' to { p -> p.move('W') }
        )
    }
}