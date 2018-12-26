package y2017.w4.d25

import readText

val text = readText("y2017/w4/d25/input.txt")

data class Action(val write: Int, val move: Int, val nextState: String)

fun String.secondLastWord() = split(" ").dropLast(1).last().trimEnd('.', ':')
fun String.lastWord() = split(" ").last().trimEnd('.', ':')

val startState = text.lines()[0].lastWord()
val steps = text.lines()[1].secondLastWord().toInt()

val rules = text.lines().filterNot(String::isEmpty).drop(2)
        .map(String::lastWord).chunked(9) { out->
    out[0].lastWord() to out.drop(1).chunked(4).mapIndexed { i, it ->
        val (write, move, next) = it.drop(1)
        i to Action(write.toInt(), if (move == "right") 1 else -1, next)
    }.toMap()
}.toMap()

fun main(args: Array<String>) {
    var state = startState
    var pos = 0
    val tape = mutableMapOf<Int, Int>()

    repeat(steps) {
        val rule = rules[state]!![tape[pos] ?: 0]!!
        tape[pos] = rule.write
        pos += rule.move
        state = rule.nextState
    }

    println(tape.values.sum())
}