package y2018.w1.d03

import readText
import java.util.*
import kotlin.system.measureTimeMillis

val input = readText("y2018/w1/d03/input.txt")

class Plot(val id: Int, val x: Int, val y: Int, val w: Int, val h: Int)

fun main(args: Array<String>) {
    val pattern = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex()
    val plots = input.lines().map {
        val (id, x, y, w, h) = pattern.matchEntire(it)!!.groupValues.drop(1).map(String::toInt)
        Plot(id, x, y, w, h)
    }

    val single = BitSet(1000*1000)
    val double = BitSet(1000*1000)

    for (plot in plots) {
        plot.forEach { index ->
            if (single.get(index))
                double.set(index)
            else
                single.set(index)
        }
    }

    for (plot in plots) {
        var intersects = false
        plot.forEach { index ->
            if (double.get(index))
                intersects = true
        }
        if (!intersects)
            println(plot.id)
    }

    println(double.cardinality())
}

private inline fun Plot.forEach(block: (index: Int) -> Unit) {
    for (x in this.x until (this.x + this.w))
        for (y in this.y until (this.y + this.h))
            block((y shl 16) + x)
}