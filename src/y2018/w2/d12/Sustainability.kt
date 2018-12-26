package y2018.w2.d12

import readText
import java.util.*

val input = readText("y2018/w2/d12/input.txt")

fun posIndex(neg: Int): Int = if (neg >= 0) neg * 2 else neg * -2 + 1

fun posRange(set: BitSet) = (-(set.length() + 10) / 2..(set.length() + 10) / 2)

fun tunnelSum(set: BitSet) = posRange(set).sumBy { if (set[posIndex(it)]) it else 0 }

fun patternToNumber(pattern: List<Boolean>) =
        pattern.asReversed().withIndex().sumBy { (i, b) -> if (b) 1 shl i else 0 }

fun main(args: Array<String>) {
    // load input
    var curr = BitSet()

    val lines = input.lines()
    for ((i, c) in lines[0].split(' ')[2].withIndex())
        curr[posIndex(i)] = c == '#'

    val rules = BooleanArray(32)
    for (line in lines.drop(2)) {
        val (pattern, result) = line.split(" => ")
        rules[patternToNumber(pattern.map { it == '#' })] = result == "#"
    }

    // simulate
    var prev: BitSet
    for (gen in 1 .. 200) {
        prev = curr
        curr = BitSet(prev.size())

        curr.clear()
        for (i in posRange(prev)) {
            val pattern = (i - 2..i + 2).map { prev[posIndex(it)] }
            curr.set(posIndex(i), rules[patternToNumber(pattern)])
        }

        println("$gen".padEnd(10) + tunnelSum(curr))
    }
}