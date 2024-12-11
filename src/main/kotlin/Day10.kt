class Day10 : DaySolver(10, "Hoof It") {
    private class Node(val height: Int) {
        val neighbours = mutableListOf<Node>()
        fun addNeighbour(n: Node) = neighbours.add(n)
        val reachablePeaks: Set<Node> by lazy {
            if (height == 9) {
                setOf(this)
            } else {
                neighbours.filter { it.height == height + 1 }.flatMap { it.reachablePeaks }.toSet()
            }
        }

        val rating: Int by lazy {
            if (height == 9) {
                1
            } else {
                neighbours.filter { it.height == height + 1 }.sumOf { it.rating }
            }
        }

        override fun toString() = "Node(h=$height,s=${reachablePeaks.size},r=$rating)"
    }


    private fun parse(input: List<String>): List<Node> {
        val nodes = input.map { it.map { c -> Node(c.digitToInt()) } }
        nodes.forEachIndexed { line, nodeLine ->
            nodeLine.forEachIndexed { col, node ->
                if (line - 1 >= 0)
                    node.addNeighbour(nodes[line - 1][col])
                if (col - 1 >= 0)
                    node.addNeighbour(nodes[line][col - 1])
                if (line + 1 < input.size)
                    node.addNeighbour(nodes[line + 1][col])
                if (col + 1 < input.first().length)
                    node.addNeighbour(nodes[line][col + 1])
            }
        }

        nodes.forEach { println(it) }
        return nodes.flatten()
    }


    override val exampleInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()


    private lateinit var nodeList: List<Node>


    override fun firstPart(input: List<String>): String {
        nodeList = parse(input)
        return nodeList.filter { it.height == 0 }.sumOf { it.reachablePeaks.size }.toString()
    }

    override fun secondPart(input: List<String>) =  nodeList.filter { it.height == 0 }.sumOf { it.rating }.toString()
}
