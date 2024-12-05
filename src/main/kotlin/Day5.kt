class Day5 : DaySolver(5, "Print Queue") {
    override val exampleInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()

    fun List<String>.getRules() = filter { "|" in it }.map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
    fun List<String>.getUpdates() = filter { "," in it }.map { it.split(",").map { it.toInt() } }

    fun List<Int>.isCorrect(rules: Map<Int, List<Int>>): Boolean {
        for (i in indices) {
            if (get(i) in rules) {
                for (following in rules[get(i)]!!) {
                    if (following in this && indexOf(following) < i) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun MutableList<Int>.correct(rules: Map<Int, List<Int>>): Boolean {
        for (i in indices) {
            if (get(i) in rules) {
                for (following in rules[get(i)]!!) {
                    if (following in this) {
                        val j = indexOf(following)
                        if (j < i) {
                            set(j, get(i))
                            set(i, following)
                            return false
                        }
                    }
                }
            }
        }
        return true
    }


    override fun firstPart(input: List<String>): String {
        val rules = buildMap<Int, MutableList<Int>> {
            input.getRules().forEach { (key, value) ->
                if (key !in this)
                    put(key, mutableListOf())
                get(key)!!.add(value)
            }
        }
        val updates = input.getUpdates()
        val validUpdates = updates.filter { it.isCorrect(rules) }
        return validUpdates.sumOf { it[it.size / 2] }.toString()
    }

    override fun secondPart(input: List<String>): String {
        val rules = buildMap<Int, MutableList<Int>> {
            input.getRules().forEach { (key, value) ->
                if (key !in this)
                    put(key, mutableListOf())
                get(key)!!.add(value)
            }
        }
        val updates = input.getUpdates()
        val invalidUpdates = updates.filter { !it.isCorrect(rules) }.map { it.toMutableList() }
        invalidUpdates.forEach {
            while (! it.correct(rules)) { /* do this */ }
        }
        return invalidUpdates.sumOf { it[it.size / 2] }.toString()
    }
}
