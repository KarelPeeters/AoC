package y2018.w4.d24

import readText

val input = readText("y2018/w4/d24/input.txt")

data class Group(
        var count: Int,
        val hp: Int,
        val weakTo: Set<String>,
        val immuneTo: Set<String>,
        val dmg: Int,
        val attack: String,
        val initiative: Int,
        val system: Boolean,
        val id: Int
) {
    fun power() = count * dmg
}

fun Group.theoreticalDmg(target: Group) = power() * when (attack) {
    in target.weakTo -> 2
    in target.immuneTo -> 0
    else -> 1
}

val listPattern = """((?:\w+)(?:, \w+)*)"""
val weakToRegex = "weak to $listPattern".toRegex()
val immuneToRegex = "immune to $listPattern".toRegex()

fun parseBuffs(str: String): Pair<Set<String>, Set<String>> {
    fun MatchResult?.toSet() =
            this?.let { it.groupValues[1].split(", ").toSet() } ?: emptySet()
    return weakToRegex.find(str).toSet() to immuneToRegex.find(str).toSet()
}

val groupRegex = """(\d+) units each with (\d+) hit points (?:\((.*)\) )?with an attack that does (\d+) (\w+) damage at initiative (\d+)""".toRegex()

fun parseInput(): List<Group> {
    var currentSystem = true
    var nextId = 1

    return input.lines().mapNotNullTo(mutableListOf()) m@{ line ->
        if (line.isEmpty()) {
            currentSystem = false
            nextId = 1
            return@m null
        }

        val match = groupRegex.matchEntire(line) ?: return@m null
        val (count, hp, buffs, dmg, attack, initiative) = match.destructured
        val (weakTo, immuneTo) = parseBuffs(buffs)
        Group(
                count.toInt(), hp.toInt(),
                weakTo, immuneTo,
                dmg.toInt(), attack, initiative.toInt(),
                currentSystem, nextId++
        )
    }
}

fun runBattle(groups: MutableList<Group>): Pair<Boolean?, Int> {
    var changed = true
    while (changed && groups.groupBy { it.system }.size == 2) {

        groups.sortWith(compareBy({ -it.power() }, { -it.initiative }))

        //target selection
        val picked = mutableSetOf<Group?>()
        val targets = groups.associateWith { curr ->
            val target = groups
                    .filter { it.system != curr.system && it !in picked }
                    .maxWith(compareBy({ curr.theoreticalDmg(it) }, { it.power() }, { it.initiative }))
                    ?.takeIf { curr.theoreticalDmg(it) > 0 }
            picked.add(target)
            target
        }


        //attack
        changed = false
        for ((attacker, defender) in targets.entries.sortedBy { -it.key.initiative }) {
            if (attacker in groups && defender != null) {
                val kill = attacker.theoreticalDmg(defender) / defender.hp

                defender.count -= kill
                if (defender.count <= 0)
                    groups.remove(defender)

                if (kill > 0)
                    changed = true
            }
        }
    }

    return (if (changed) groups.first().system else null) to groups.sumBy { it.count }
}

fun main(args: Array<String>) {
    val groups = parseInput()

    val copy = groups.mapTo(mutableListOf()) { it.copy() }
    println("Part 1: " + runBattle(copy))

    for (boost in generateSequence(1) { it + 1 }) {
        val boosted = groups.mapTo(mutableListOf()) {
            if (it.system) it.copy(dmg = it.dmg + boost)
            else it.copy()
        }
        val result = runBattle(boosted)
        if (result.first == true) {
            println("Part 2: $result")
            break
        }
    }
}