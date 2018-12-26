package y2018.w2.d13

import readText
import y2018.w2.d13.Dir.*
import java.util.*

val input = readText("y2018/w2/d13/input.txt")

enum class Dir(val dx: Int, val dy: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    val turnLeft get() = values()[(ordinal - 1 + values().size) % values().size]
    val turnRight get() = values()[(ordinal + 1) % values().size]
}

data class Cart(var x: Int, var y: Int, var dir: Dir, var state: Int)

fun main(args: Array<String>) {
    val compare = compareBy<Cart>({ it.y }, { it.x })
    val carts = mutableListOf<Cart>()

    val grid = input.lines().mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            when (c) {
                '^' -> {
                    carts.add(Cart(x, y, UP, 0)); '|'
                }
                'v' -> {
                    carts.add(Cart(x, y, DOWN, 0)); '|'
                }
                '<' -> {
                    carts.add(Cart(x, y, LEFT, 0)); '-'
                }
                '>' -> {
                    carts.add(Cart(x, y, RIGHT, 0)); '-'
                }
                else -> c
            }
        }
    }

    val set = TreeSet<Cart>(compare)
    var seenFirst = false

    while (true) {
        set.addAll(carts)
        while (set.isNotEmpty()) {
            val cart = set.pollFirst()

            // move
            cart.x += cart.dir.dx
            cart.y += cart.dir.dy

            // collision
            val hit = carts.find { it !== cart && it.x == cart.x && it.y == cart.y }
            if (hit != null) {
                if (!seenFirst) {
                    println("${cart.x},${cart.y}")
                    seenFirst = true
                }

                carts.remove(hit)
                set.remove(hit)
                carts.remove(cart)
            }

            // turn
            cart.dir = when (grid[cart.y][cart.x]) {
                '/' -> when (val dir = cart.dir) {
                    UP, DOWN -> dir.turnRight
                    LEFT, RIGHT -> dir.turnLeft
                }
                '\\' -> when (val dir = cart.dir) {
                    UP, DOWN -> dir.turnLeft
                    LEFT, RIGHT -> dir.turnRight
                }
                '+' -> when ((cart.state++) % 3) {
                    0 -> cart.dir.turnLeft
                    1 -> cart.dir
                    2 -> cart.dir.turnRight
                    else -> error("cart has illegal state")
                }
                '|', '-' -> cart.dir
                else -> error("cart fell of the track")
            }
        }

        if (carts.size == 1) {
            val cart = carts.first()
            println("${cart.x},${cart.y}")
            break
        }
    }
}