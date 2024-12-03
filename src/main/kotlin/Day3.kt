class Day3 : DaySolver(3, "Mull it Over") {
    override val exampleInput = """
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trimIndent().split("\n")

    val mulRegex = Regex("mul\\([0-9]{1,3},[0-9]{1,3}\\)")

    fun String.toMul() = when (this) {
        "do()" -> 0
        "don't()" -> 0
        else -> substringAfter("(").substringBefore(",").toInt() *
                substringAfter(",").substringBefore(")").toInt()
    }

    val mulRegexDoDont = Regex("mul\\([0-9]{1,3},[0-9]{1,3}\\)|don't\\(\\)|do\\(\\)")

    override fun firstPart(input: List<String>) = input.sumOf {
        mulRegex.findAll(it).sumOf { it.groupValues.toString().toMul() }
    }.toString()

    override fun secondPart(input: List<String>): String {
        var enabled = true
        return input.sumOf {
            return@sumOf mulRegexDoDont.findAll(it)
                .map { it.groupValues.toString().removePrefix("[").removeSuffix("]") }
                .sumOf { s ->
                    if (s == "do()") enabled = true
                    else if (s == "don't()") enabled = false
                    if (enabled) s.toMul() else 0
                }
        }.toString()
    }
}

