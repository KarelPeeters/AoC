package y2017.w1.d04

import readText

val text = readText("y2017/w1/d04/input.txt")

fun main(args: Array<String>) {
    println(text.lines().count {
        it.split(" ")
                .let { it == it.distinct() }
    })

    println(text.lines().count {
        it.split(" ")
                .map { it.toCharArray().sorted() }
                .let { it == it.distinct() }
    })
}