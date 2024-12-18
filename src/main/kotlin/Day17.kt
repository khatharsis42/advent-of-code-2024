import kotlin.math.pow

class Day17 : DaySolver(17, "Chronospatial Computer") {
    override val exampleInput = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent().lines()

    private class Computer(
        var registerA: Long,
        var registerB: Long,
        var registerC: Long,
        var program: List<Int>
    ) {
        var curIndex = 0
        val output = mutableListOf<Int>()

        fun runProgram() {
            while (curIndex + 1 < program.size) {
                runInstruction(Instruction.values()[program[curIndex]], program[curIndex + 1])
                curIndex += 2
            }
        }

        private fun runInstruction(i: Instruction, lo: Int) {
            assert(lo < 8)
            val co: Long = when (lo) {
                4 -> registerA
                5 -> registerB
                6 -> registerC
                else -> lo.toLong()
            }
            when (i) {
                Instruction.ADV -> {
                    registerA = (registerA / 2L.pow(co))
                }

                Instruction.BXL -> {
                    registerB = (registerB xor lo.toLong())
                }

                Instruction.BST -> {
                    registerB = (co.mod(8)).toLong()
                }

                Instruction.JNZ -> {
                    if (registerA != 0L) {
                        curIndex = lo - 2
                    }
                }

                Instruction.BXC -> {
                    registerB = registerB xor registerC
                }

                Instruction.OUT -> {
                    output.add(co.mod(8))
                }

                Instruction.BDV -> {
                    registerB = (registerA / 2L.pow(co))

                }

                Instruction.CDV -> {
                    registerC = (registerA / 2L.pow(co))
                }
            }
        }
    }

    private enum class Instruction { ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV }

    override fun firstPart(input: List<String>): String {
        val machine = Computer(
            input[0].substringAfter(": ").toLong(),
            input[1].substringAfter(": ").toLong(),
            input[2].substringAfter(": ").toLong(),
            input[4].substringAfter(": ").split(",").map { it.toInt() }
        )
        machine.runProgram()
        return machine.output.joinToString(",")
    }

    override fun secondPart(input: List<String>): String {
        val program = input[4].substringAfter(": ").split(",").map { it.toInt() }
        var base = 0L
        var i = 0
        while (i in program.indices) {
            var found = false
            for (j in (base.mod(8))..7) {
                val init = base + j - base.mod(8)
                val computer = Computer(init, 0, 0, program)
                computer.runProgram()
                if (computer.output == program.subList(program.size - 1 - i, program.size)) {
                    // println(init.toString(8).padEnd(program.size) + " -> ${computer.output}")
                    base = 8 * init
                    found = true
                    i ++
                    break
                }
            }
            if (!found) {
                base /= 8
                base += 1
                i -= 1
            }
        }
        base /= 8
        return base.toString()
    }
}
