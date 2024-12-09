import kotlin.math.sign

class Day9 : DaySolver(9, "Disk Fragmenter") {
    override val exampleInput = "2333133121414131402".lines()

    private fun String.printBlock() {
        var emptyBlock = false
        var blockID = 0
        forEach {
            print((if (emptyBlock) "." else "*").repeat(it.digitToInt()))
            if (!emptyBlock) blockID++
            emptyBlock = !emptyBlock
        }
        println()
    }


    data class Block(val id: Int, var position: Long, var length: Long) {
        fun checksum() = (position until (position + length)).sumOf { it * id }
    }

    override fun firstPart(input: List<String>): String {
        val blockList: MutableList<Block> = buildList {
            var blockID = 0
            var position = 0L
            input.first().forEachIndexed { index, c ->
                if (index % 2 == 0) {
                    add(Block(blockID++, position, c.digitToInt().toLong()))
                }
                position += c.digitToInt()
            }
        }.toMutableList()
        var indexFirstBlockBeforeEmpty = 0
        fun getEmptyBlockSize(i: Int) = blockList[i + 1].position - (blockList[i].position + blockList[i].length)
        while (indexFirstBlockBeforeEmpty + 1 < blockList.size) {
            if (getEmptyBlockSize(indexFirstBlockBeforeEmpty) == 0L) {
                indexFirstBlockBeforeEmpty++
                continue
            }

            val emptyBlockSize = getEmptyBlockSize(indexFirstBlockBeforeEmpty)
            if (emptyBlockSize >= blockList.last().length) {
                blockList.add(indexFirstBlockBeforeEmpty + 1, blockList.removeLast())
                blockList[indexFirstBlockBeforeEmpty + 1].position =
                    blockList[indexFirstBlockBeforeEmpty].let { it.position + it.length }
                indexFirstBlockBeforeEmpty++
            } else {
                val temp = blockList.last()

                blockList.add(
                    indexFirstBlockBeforeEmpty + 1,
                    Block(
                        temp.id,
                        blockList[indexFirstBlockBeforeEmpty].let { it.position + it.length },
                        emptyBlockSize
                    )
                )
                temp.length -= emptyBlockSize
                indexFirstBlockBeforeEmpty++
            }
        }

        return blockList.sumOf { it.checksum() }.toString()

    }

    override fun secondPart(input: List<String>): String {
        val blockList: MutableList<Block> = buildList {
            var blockID = 0
            var position = 0L
            input.first().forEachIndexed { index, c ->
                if (index % 2 == 0) {
                    add(Block(blockID++, position, c.digitToInt().toLong()))
                }
                position += c.digitToInt()
            }
        }.toMutableList()
        var indexLastBlockToMove = blockList.size - 1
        fun getEmptyBlockSize(i: Int) = blockList[i + 1].position - (blockList[i].position + blockList[i].length)
        while (indexLastBlockToMove > 0) {
            var indexFirstBlockBeforeEmpty = 0
            while (indexFirstBlockBeforeEmpty + 1 < blockList.size && indexFirstBlockBeforeEmpty < indexLastBlockToMove) {
                if (getEmptyBlockSize(indexFirstBlockBeforeEmpty) < blockList[indexLastBlockToMove].length) {
                    indexFirstBlockBeforeEmpty++
                    continue
                }
                else {
                    blockList.add(indexFirstBlockBeforeEmpty + 1, blockList.removeAt(indexLastBlockToMove))
                    blockList[indexFirstBlockBeforeEmpty + 1].position =
                        blockList[indexFirstBlockBeforeEmpty].let { it.position + it.length }
                    indexLastBlockToMove++
                    break
                }
            }
            indexLastBlockToMove--
        }

        return blockList.sumOf { it.checksum() }.toString()

    }
}

