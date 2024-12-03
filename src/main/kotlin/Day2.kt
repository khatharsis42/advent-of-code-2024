class Day2: DaySolver(2, "Red-Nosed Reports") {
    override val exampleInput = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().split("\n")


    fun String.isSafe(): Boolean =
        split(" ").map { it.toInt() }.zipWithNext()
            .let {
                val order = if (it.first().first - it.first().second >= 0) -1 else 1
                it.all { (a,b) -> (b - a) * order in 1..3 }
            }

    fun String.isSafeDampened(): Boolean = isSafe().takeIf { it } ?:
        split(" ").map { it.toInt() }.let { numList ->
            (0..numList.size).any { i ->
                numList.filterIndexed { index, _ -> index != i }.joinToString(separator = " ").isSafe()
            }
        }


    override fun firstPart(input: List<String>): String = input
        .map { it.isSafe() }
        .count {it}.toString()

    override fun secondPart(input: List<String>): String = input
        .map { it.isSafeDampened() }
        .count {it}.toString()
}
