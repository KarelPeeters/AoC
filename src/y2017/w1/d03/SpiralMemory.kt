package y2017.w1.d03

val input = 289326

fun main(args: Array<String>) {
    //part 1
    for ((i, coord) in indexedSpiral()) {
        if (i == input - 1) {
            println(coord.manhattan)
            break
        }
    }

    //part 2
    val grid = Grid()
    grid[Coord(0,0)] = 1
    for ((_, coord) in indexedSpiral()) {
        val sum = grid.adjacentSum(coord)
        if (sum > input) {
            println(sum)
            break
        } else {
            grid[coord] = sum
        }
    }
}

data class Coord(val x: Int, val y: Int) {
    val manhattan = Math.abs(x) + Math.abs(y)
    operator fun plus(other: Coord) = Coord(x + other.x, y + other.y)
}

fun indexedSpiral() = object : Iterator<Pair<Int, Coord>> {
    var x: Int = 0
    var y: Int = 0

    var delta = 1
    var index = 0

    val queue = mutableListOf<Pair<Int, Coord>>()

    override fun next(): Pair<Int, Coord> {
        if (queue.isEmpty()) {
            for (x in x until x + delta) queue += index++ to Coord(x, y)
            x += delta
            for (y in y until y + delta) queue += index++ to Coord(x, y)
            y += delta
            delta++

            for (x in x downTo x - delta + 1) queue += index++ to Coord(x, y)
            x -= delta
            for (y in y downTo y - delta + 1) queue += index++ to Coord(x, y)
            y -= delta
            delta++
        }

        return queue.removeAt(0)
    }

    override fun hasNext() = true
}

class Grid {
    private val map = mutableMapOf<Int, MutableMap<Int, Int>>()

    operator fun set(coord: Coord, value: Int) {
        map.getOrPut(coord.x) { mutableMapOf() }[coord.y] = value
    }

    operator fun get(coord: Coord): Int = map[coord.x]?.get(coord.y) ?: 0

    fun adjacentSum(coord: Coord) = (-1..1).sumBy { dx ->
        (-1..1).sumBy { dy -> get(coord + Coord(dx, dy)) }
    }
}