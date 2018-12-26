package y2018.w2.d08

import readText
import timeNano
import kotlin.system.measureNanoTime

//val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
val input = readText("y2018/w2/d08/input.txt")

class Node(val children: Array<Node>, val meta: IntArray)

fun parseNode(tape: Iterator<Int>): Node {
    val childCount = tape.next()
    val metaCount = tape.next()
    return Node(Array(childCount) { parseNode(tape) }, IntArray(metaCount) { tape.next() })
}

fun Node.metaSum(): Int = meta.sum() + children.sumBy { it.metaSum() }
fun Node.value(): Int =
        if (children.isEmpty()) meta.sum()
        else meta.sumBy { children.getOrNull(it - 1)?.value() ?: 0 }

fun main(args: Array<String>) {
    repeat(100_000) {
        timeNano {
            val numbers = input.split(' ').map { it.toInt() }

            val root = parseNode(numbers.iterator())
            /*println*/(root.metaSum())
            /*println*/(root.value())
        }
    }
}