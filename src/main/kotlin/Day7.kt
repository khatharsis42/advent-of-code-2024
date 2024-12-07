class Day7 : DaySolver(7, "Bridge Repair") {
    override val exampleInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()

    class Operation(val result: Long, val values: List<Long>) {
        constructor(s: String) : this(
            s.substringBefore(':').toLong(),
            s.substringAfter(": ").split(" ").map { it.toLong() })

        fun checkPossible(radix: Int = 2): Boolean {
            val nPossibleOperations = values.size - 1
            var buffer = 1
            while (buffer.toString(radix).length < nPossibleOperations) {
                buffer *= radix
            }
            buffer *= radix
            for (i in 0 until buffer) {
                val operators = (i + buffer).toString(radix)
                    .map { c ->
                        when (c) {
                            '0' -> '+'
                            '1' -> '*'
                            '2' -> '|'
                            else -> error("Unauthorized digit $c")
                        }
                    }.reversed()
                val obtainedValue = applyLeftToRight(operators)
                if (obtainedValue == result) {
                    return true
                }
            }
            return false
        }

        fun applyLeftToRight(operation: List<Char>) =
            operation.dropLast(1).foldIndexed(values.first()) { index, acc, c ->
                when (c) {
                    '+' -> acc + values[index + 1]
                    '*' -> acc * values[index + 1]
                    '|' -> (acc.toString() + values[index + 1].toString()).toLong()
                    else -> error("Unauthorized operator: $c")
                }
            }

        override fun hashCode() = result.toInt() * values.hashCode()
        override fun equals(other: Any?) = other is Operation && (other.result == result && other.values == values)
    }

    val inputMap = mutableMapOf<Operation, Int>()


    override fun firstPart(input: List<String>): String {
        input.map { Operation(it) }
            .forEach {
                inputMap[it] = if (it.checkPossible(2)) 2 else 0
            }
        return inputMap.filter { (key, value) -> value != 0 }.keys.sumOf { it.result }.toString()

    }

    override fun secondPart(input: List<String>): String {
        inputMap
            .filter { it.value != 2 }
            .keys.forEach { op ->
                inputMap[op] = if (op.checkPossible(3)) 3 else 0
            }
        return inputMap.filter { (key, value) -> value != 0 }.keys.sumOf { it.result }.toString()
    }
}
