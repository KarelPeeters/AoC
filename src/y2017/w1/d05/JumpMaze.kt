package y2017.w1.d05

import readText

val text = readText("y2017/w1/d05/input.txt")

fun main(args: Array<String>) {
    val input = text.lines().map(String::toInt)

    println(countJumps(input, { it + 1 }))
    println(countJumps(input, { if (it >= 3) it - 1 else it + 1 }))
}

fun countJumps(offsets: List<Int>, map: (Int) -> Int): Int {
    val list = offsets.toMutableList()

    var count = 0
    var index = 0
    while (0 <= index && index < list.size) {
        count++
        index += list[index].also {
            list[index] = map(list[index])
        }
    }

    return count
}