import kotlin.math.abs

class Day1: DaySolver(1, "Historian Hysteria") {
    override val exampleInput = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().split("\n")

    override fun firstPart(input: List<String>): String {
        val firstList = input.map {it.split(" ").first().toInt() }.sorted()
        val secondList = input.map {it.split(" ").last().toInt() }.sorted()
        return firstList.zip(secondList).sumOf { abs(it.first - it.second) }.toString()
    }

    override fun secondPart(input: List<String>): String {
        val firstMap = input.map {it.split(" ").first().toInt() }
            .let { list -> list.toSet().associateWith { value -> list.count { it == value } } }
        val secondMap = input.map {it.split(" ").last().toInt() }
            .let { list -> list.toSet().associateWith { value -> list.count { it == value } } }
        return firstMap
            .map {(key, value) -> (secondMap[key] ?: 0) * key * value}
            .sum()
            .toString()
    }
}
