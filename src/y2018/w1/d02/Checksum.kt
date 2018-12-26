package y2018.w1.d02

import readText
import kotlin.system.measureNanoTime

val input = readText("y2018/w1/d02/input.txt")

fun main(args: Array<String>) {
    val lines = input.lines()

//part 1
    measureNanoTime {
        var count2 = 0
        var count3 = 0

        for (line in lines) {
            val letters = IntArray(26)
            for (c in line)
                letters[c - 'a']++

            if (2 in letters) count2++
            if (3 in letters) count3++
        }

        println(count2 * count3)

    }.also { println(it) }

//part 2
    measureNanoTime {
        outer@ for (x in lines) {
            inner@ for (y in lines) {
                var diff = false
                for (i in x.indices) {
                    if (x[i] != y[i]) {
                        if (diff) continue@inner
                        diff = true
                    }
                }

                if (diff) {
                    for (i in x.indices)
                        if (x[i] == y[i])
                            print(x[i])
                    println()
                    break@outer
                }
            }
        }
    }.also { println("time: $it") }
}