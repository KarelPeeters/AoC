package y2017.w2.d12

import readText

val text = readText("y2017/w2/d12/input.txt")

val connections = text.lines().map {
    it.split(" <-> ").let { (id, connections) ->
        id.toInt() to connections.split(", ").map { it.toInt() }
    }
}.toMap()

fun main(args: Array<String>) {
    println(getGroup(0).size)
    println(connections.keys.map { getGroup(it) }.distinct().size)
}

fun getGroup(program: Int): Set<Int> {
    val visited = mutableSetOf<Int>()
    val toVisit = mutableListOf(program)

    while (!toVisit.isEmpty()) {
        toVisit.removeAt(0).also {
            visited += it
            toVisit.addAll(connections[it]!! - visited)
        }
    }

    return visited
}