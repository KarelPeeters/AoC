package y2018.w1.d05

import readText
import timeMili
import kotlin.math.abs

val input = readText("y2018/w1/d05/input.txt")
//val input = "dabAcCaCBAcCcaDA"

fun main(args: Array<String>) {
    timeMili {
        //part 1
        println(lengthLeft(input))

        //part 2
        input.toLowerCase().toSet().map { c ->
            lengthLeft(input.filter { !it.equals(c, ignoreCase = true) })
        }.min().also { println(it) }
    }
}

fun lengthLeft(polymer: String): Int {
    val arr = IntArray(input.length)
    var i = 0
    var j = 0
    val length = polymer.length

    while (j < length) {
        arr[i] = polymer[j++].toInt()

        if (i > 0 && abs(arr[i - 1] - arr[i]) == 32)
            i--
        else
            i++
    }

    return i
}