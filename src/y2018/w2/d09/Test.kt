package y2018.w2.d09

import kotlin.system.measureTimeMillis

class Marble {
    var next = this
    var prev = this

    fun add(marble: Marble): Marble {
        next.prev = marble
        marble.next = next
        marble.prev = this
        next = marble
        return marble
    }
}

fun main(args: Array<String>) {
    repeat(100) {

        measureTimeMillis {
            var current = Marble()
            for (i in 0..9_000_000) {
                current = if (i % 23 == 0) {
              generateSequence(current) { it.prev }.take(8).last()
//                    current.prev.prev.prev.prev.prev.prev.prev
                } else {
                    current.add(Marble())
                }
            }
        }.also { println(it) }
    }
}