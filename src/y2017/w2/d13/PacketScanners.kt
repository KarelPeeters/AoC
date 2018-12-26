package y2017.w2.d13

import readText

val input = readText("y2017/w2/d13/input.txt")

val layers = input.lines()
        .map { it.split(": ").map { it.toInt() } }
        .map { (layer, depth) -> layer to depth }

fun main(args: Array<String>) {
    println(layers.sumBy { (layer, depth) -> if ((layer) % (2 * (depth - 1)) == 0) layer * depth else 0 })

    println(generateSequence(0) { it + 1 }
            .find { delay -> layers.none { (layer, depth) -> (layer + delay) % (2 * (depth - 1)) == 0 }})
}