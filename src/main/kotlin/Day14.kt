import kotlinx.coroutines.delay
import kotlin.math.pow

class Day14 : DaySolver(14, "Restroom Redoubt") {
    private data class Robot(var position: Coordinates, val speed: Coordinates) {
        fun updateCoordinates(size: Coordinates) {
            //assert(n % step == 0)
            position += speed
            position %= size
        }
    }

    override val exampleInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines()

    private val robotList = mutableListOf<Robot>()
    override fun firstPart(input: List<String>): String {
        input.forEach {
            robotList.add(Robot(
                it.substringAfter("p=").substringBefore(" v")
                    .split(",").map { it.toInt() }
                    .let { it.first() to it.last() },
                it.substringAfter("v=")
                    .split(",").map { it.toInt() }
                    .let { it.first() to it.last() }
            ))
        }
        val limits = 101 to 103
        val futureRobotList = robotList.map {
            for (i in 1..100)
                it.updateCoordinates(limits)
            it.position
        }
        val fstQuadrant = futureRobotList.count { it.first < (limits.first / 2) && it.second < (limits.second / 2) }
        val scdQuadrant = futureRobotList.count { it.first < (limits.first / 2) && it.second > (limits.second / 2) }
        val trdQuadrant = futureRobotList.count { it.first > (limits.first / 2) && it.second < (limits.second / 2) }
        val fthQuadrant = futureRobotList.count { it.first > (limits.first / 2) && it.second > (limits.second / 2) }
        return (fstQuadrant * scdQuadrant * trdQuadrant * fthQuadrant).toString()
    }

    private fun printRobotList(limits: Coordinates) {
        for (col in 1..limits.first) {
            for (line in 1..limits.second) {
                if (robotList.any { it.position == line to col }) print('#')
                else print(" ")
            }
            println()
        }

    }

    override fun secondPart(input: List<String>): String {
        var timer = 100
        val limits = 101 to 103
        val center = 51 to 51
        val statisticsList = arrayListOf<Double>()
        var mean = 0.0

        while (true) {
            robotList.forEach { it.updateCoordinates(limits) }
            timer++
            val score = robotList.sumOf { it.position.manhattan(center) }.toDouble() / robotList.size
            if (timer < 1_000) {
                statisticsList.add(score)
            }
            if (timer == 1_000) {
                mean = statisticsList.sum() / 1_000
            }
            if (timer > 1_000) {
                if (score < mean * .8) {
                    // printRobotList(limits)
                    return timer.toString()
                }
            }
        }
    }
}
