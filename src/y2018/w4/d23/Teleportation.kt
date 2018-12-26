package y2018.w4.d23

import org.ojalgo.netio.BasicLogger
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable
import readText
import java.math.BigInteger
import kotlin.math.abs

//val input = readText("y2018/w4/d23/input.txt")

val input = """
pos=<10,12,12>, r=2
pos=<12,14,12>, r=2
pos=<16,12,12>, r=4
pos=<14,14,14>, r=6
pos=<50,50,50>, r=200
pos=<10,10,10>, r=5
""".trimIndent()

class Bot(val x: Int, val y: Int, val z: Int, val r: Int) {
    fun dist(other: Bot) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
}

val intRegex = """-?\d+""".toRegex()
fun main(args: Array<String>) {
    val bots = input.lines().map { line ->
        val (x,y,z,r) = intRegex.findAll(line).map { it.value.toInt() }.toList()
        Bot(x,y,z,r)
    }

    val strongest = bots.maxBy { it.r }!!
    val countInRange = bots.count { it.dist(strongest) <= strongest.r }

    println("Part 1: $countInRange")

    val model = ExpressionsBasedModel()

    val x = Variable("x").integer(true)
    val y = Variable("y").integer(true)
    val z = Variable("z").integer(true)
    model.addVariables(arrayOf(x,y,z))

    val signs = arrayOf(-1,1)
    val large = 1_000_000_000.toBigInteger()
    val inRanges = bots.indices.flatMap { i -> (0 until 8).map { Variable("c_${i}_$it") } }
    model.addVariables(inRanges)

    var i = 0
    for ((bi, bot) in bots.withIndex()) {

        for (xs in signs) {
            for (ys in signs) {
                for (zs in signs) {
                    val inRange = inRanges[i++]
                    inRange.weight(1)
                    inRange.binary().weight(1)
                    model.addExpression("")
                            .set(x, xs)
                            .set(y, ys)
                            .set(z, zs)
                            .set(inRange, large)
                            .upper((bot.r + xs * bot.x + ys * bot.y + zs * bot.z).toBigInteger() + large)
                }
            }
        }
    }

    val sol = model.maximise()
    BasicLogger.debug(model)
    BasicLogger.debug(sol)
}