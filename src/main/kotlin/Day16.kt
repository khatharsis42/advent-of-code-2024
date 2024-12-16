class Day16 : DaySolver(16, "Reinder Maze") {
    override val exampleInput = """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent().lines()

    data class State(
        val position: Coordinates,
        val direction: Direction = East,
        val score: Int = 0,
        val prevState: State? = null
    ) {
        fun getNextStates(path: Set<Coordinates>): List<State> {
            val turningPoints = mutableListOf<State>()
            fun checkNewPosition(newDirection: Direction, score: Int) {
                if (position + newDirection in path) {
                    turningPoints.add(State(position + newDirection, newDirection, score, this))
                }
            }
            checkNewPosition(direction, score + 1)
            checkNewPosition(direction.rotate90(), score + 1001)
            checkNewPosition(direction.rotateOpp90(), score + 1001)

            return turningPoints
        }

        fun getPath() =
            buildList {
                var temp = prevState
                while (temp != null) {
                    add(temp.position)
                    temp = temp.prevState
                }
            }
    }

        lateinit var startingCoordinates: Coordinates
        lateinit var endingCoordinates: Coordinates
        var bestScore = 0

        val queue = mutableListOf<State>()
        val visited = mutableMapOf<Coordinates, MutableSet<Direction>>()
        override fun firstPart(input: List<String>): String {

            val path = buildSet {
                input.forEachIndexed { line, s ->
                    s.forEachIndexed { col, c ->
                        if (c == '.') add(Coordinates(line, col))
                        if (c == 'S') startingCoordinates = Coordinates(line, col).also { add(it) }
                        if (c == 'E') endingCoordinates = Coordinates(line, col).also { add(it) }
                    }
                }
            }
            queue.add(State(startingCoordinates))
            while (queue.isNotEmpty()) {
                val curState = queue.minBy { it.score }
                if (curState.position == endingCoordinates) {
                    bestScore = curState.score
                    return curState.score.toString()
                }
                queue.addAll(
                    curState.getNextStates(path)
                        .filter { it.position !in visited || it.direction !in visited[it.position]!! })
                queue.remove(curState)
                if (curState.position !in visited) {
                    visited[curState.position] = mutableSetOf(curState.direction)
                } else {
                    visited[curState.position]!!.add(curState.direction)
                }
            }

            return "No possible path"
        }

        override fun secondPart(input: List<String>): String {
            val bestTiles = mutableSetOf<Coordinates>()
            while (queue.isNotEmpty()) {
                val curState = queue.minBy { it.score }
                if (curState.score > bestScore) {
                    break
                }
                if (curState.position == endingCoordinates) {
                    bestTiles.addAll(curState.getPath())
                }
                queue.remove(curState)
            }
            return (bestTiles.size + 1).toString()
        }
    }
