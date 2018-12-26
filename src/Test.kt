import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    measureTimeMillis {
        var r = 0
        for (i in 0..100_000_000)
            if (i % 2 == 0)
                r += i
        println(r)
    }.also { println(it) }

    measureTimeMillis {
        val y = (0..100_000_100).asSequence().filter { it % 2 == 0 }.sum()
        println(y)
    }.also { println(it) }

    measureTimeMillis {
        val x = (0..100_000_100).filter { it % 2 == 0 }.sum()
        println(x)
    }.also { println(it) }
}