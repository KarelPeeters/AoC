package y2018.w2.d09

import timeNano

class Queue<T>(val size: Int) {
    private val array = arrayOfNulls<Any>(size)
    private var head: Int = 0 //the first element
    private var tail: Int = 0 //just past the last element

    fun addHead(value: T) {
        head = (head - 1 + size) % size
        array[head] = value
    }

    fun addTail(value: T) {
        array[tail] = value
        tail = (tail + 1) % size
    }

    fun pollHead(): T {
        val value = array[head]
        head = (head + 1) % size
        return value as T
    }

    fun pollTail(): T {
        tail = (tail - 1 + size) % size
        return array[tail] as T
    }
}

fun topScore(players: Int, marbles: Int): Long {
    val scores = LongArray(players)
    val circle = Queue<Int>(marbles)
    circle.addHead(0)

    for (m in 1..marbles) {
        if (m % 23 == 0) {
            repeat(7) { circle.addTail(circle.pollHead()) }
            scores[m % players] += m.toLong() + circle.pollHead()
            circle.addHead(circle.pollTail())
        } else {
            circle.addHead(circle.pollTail())
            circle.addHead(m)
        }
    }

    return scores.max()!!
}

fun main(args: Array<String>) {
    repeat(100) {

        timeNano {
            println(topScore(458, 72019))
            println(topScore(458, 100 * 72019))
        }
    }
}