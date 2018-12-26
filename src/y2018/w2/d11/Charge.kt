package y2018.w2.d11

val serial = 3214

fun main(args: Array<String>) {
    val max = product(1 until 20, 0 until 300 - 3, 0 until 300 - 3).maxBy { (s, x, y) ->
        product(0 until s, 0 until s).sumBy { (dx, dy) ->
            val rack = (x + dx) + 10
            val full = (rack * (y + dy) + serial) * rack
            (full / 100) % 10 - 5
        }
    }
    println(max)
}

fun <L, R> product(left: Iterable<L>, right: Iterable<R>) =
        left.asSequence().flatMap { l -> right.asSequence().map { r -> l to r } }

fun <L, C, R> product(left: Iterable<L>, center: Iterable<C>, right: Iterable<R>) =
        left.asSequence().flatMap { l -> center.asSequence().flatMap { c -> right.asSequence().map { r -> Triple(l, c, r) } } }