package y2017.w3.d21

import readText

val text = readText("y2017/w3/d21/input.txt")

fun parseGrid(str: String) = str.split("/").map { it.toCharArray().map { it == '#' } }.let { Grid(it.size, it) }
val rules = text.lines().map { it.split(" => ").map(::parseGrid) }.map { (i, o) -> i to o }.toMap()

fun main(args: Array<String>) {
    var curr = parseGrid(".#./..#/###")

    repeat(5) { curr = curr.step() }
    println("part 1: " + curr.countSet())
    repeat(18 - 5) { curr = curr.step() }
    println("part 2: " + curr.countSet())
}

fun Grid.step() = subGrids().map { it.map(Grid::applyRule) }.mergeGrids()

fun Grid.countSet() = tiles.sumBy { it.count { it } }

fun Grid.applyRule() = permutations().mapNotNull { rules[it] }.first()

fun List<List<Grid>>.mergeGrids(): Grid {
    val gridSize = this[0][0].size
    val totalSize = gridSize * this.size

    val tiles = xyMap(totalSize) { x, y ->
        this[x / gridSize][y / gridSize].tiles[x % gridSize][y % gridSize]
    }
    return Grid(totalSize, tiles)
}

data class Grid(val size: Int, val tiles: List<List<Boolean>>) {
    fun mirrored() = Grid(size, tiles.asReversed())
    fun rotated() = Grid(size, (0 until size).map { x -> (0 until size).map { y -> tiles[(size - 1) - y][x] } })

    fun permutations() = (0 until 4).flatMap {
        val curr = (0 until it).fold(this) { c, _ -> c.rotated() }
        listOf(curr, curr.mirrored())
    }

    fun subGrids(): List<List<Grid>> {
        val new = when {
            size % 2 == 0 -> 2
            size % 3 == 0 -> 3
            else -> throw IllegalStateException()
        }
        val count = size / new

        return xyMap(count) { mx, my ->
            Grid(new, xyMap(new) { sx, sy ->
                tiles[mx * new + sx][my * new + sy]
            })
        }
    }
}

fun <R> xyMap(size: Int, transform: (x: Int, y: Int) -> R) =
        (0 until size).map { x -> (0 until size).map { y -> transform(x, y) } }