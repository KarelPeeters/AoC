package y2017.w1.d07

import readText

val text = readText("y2017/w1/d07/input.txt")

data class Node(val name: String, val weight: Int, val children: List<String>) {
    val totalWeight: Int by lazy { weight + children.sumBy { nodes[it]!!.totalWeight } }
}


/*val regex = """([a-z]+)\((\d+)\)(?:->([a-z,]+))?""".toRegex()
val nodes = text.lines().map {
    val match = regex.matchEntire(it.replace(" ", ""))!!.groupValues
    val node = Node(match[1], match[2].toInt(), if (match.size >= 3) match[3].split(",") else listOf())

    println(match[3].split(","))

    node.name to node
}.toMap()*/

//TODO what?
val pattern = """([a-z]+)\((\d+)\)(?:->([a-z,]+))?""".toPattern()
val nodes = text.lines().map {
    val match = pattern.matcher(it.replace(" ", "")).apply { assert(matches()) }
    val node = Node(match.group(1), match.group(2).toInt(), match.group(3)?.split(",") ?: emptyList())

    println(match.group(3)?.split(",") ?: emptyList<String>())
    //println(match[3].split(","))

    node.name to node
}.toMap()

fun main(args: Array<String>) {
    println(nodes.values.root().name)

    val wrongParents = nodes.values.filter {
        it.children.map { nodes[it]!!.totalWeight }.distinct().size > 1
    }
    val firstWrong = wrongParents.leaf()

    val children = firstWrong.children.map { nodes[it]!! }
    val weightCounts = children.map { a -> a.totalWeight to children.count { it.totalWeight == a.totalWeight } }.toMap()

    val rightChild = children.maxBy { weightCounts[it.totalWeight]!! }!!
    val wrongChild = children.minBy { weightCounts[it.totalWeight]!! }!!

    println(wrongChild.weight - (wrongChild.totalWeight - rightChild.totalWeight))
}

fun Collection<Node>.root() = find { (name) -> (none { name in it.children }) }!!

fun Collection<Node>.leaf() = find { it.children.none { child -> any { child == it.name } } }!!
