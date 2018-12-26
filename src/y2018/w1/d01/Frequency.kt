package y2018.w1.d01

import readText

val text = readText("y2018/w1/d01/input.txt")

fun main(args: Array<String>) {
    val numbers = text.lines().mapNotNull { it.toIntOrNull() }

    println(numbers.sum())

    val seen = mutableSetOf(0)
    var curr = 0
    outer@ while (true) {
        for (number in numbers) {
            curr += number
            if (!seen.add(curr)) {
                println(curr)
                break@outer
            }
        }
    }
}