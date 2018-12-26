package y2017.w2.d09

import readText

val text = readText("y2017/w2/d09/input.txt")

fun main(args: Array<String>) {
    val cancelled = text.replace("!.".toRegex(), "")
    val degarbaged = cancelled.replace("<.*?>".toRegex(), "<>")

    var sum = 0
    var depth = 1
    for (c in degarbaged) {
        when (c) {
            '{' -> sum += depth++
            '}' -> depth--
        }
    }

    println("score: $sum")
    println("garbage size: ${cancelled.length - degarbaged.length}")
}