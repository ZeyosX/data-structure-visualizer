package org.binarystorm.datastructurevisualizer.generators

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.Random

class MazeGenerator(private val size: Int) {
    private val wallBlock: BlockState = Blocks.WHITE_CONCRETE.defaultBlockState()
    private val pathBlock: BlockState = Blocks.YELLOW_CONCRETE.defaultBlockState()
    private val entranceBlock: BlockState = Blocks.BLACK_CONCRETE.defaultBlockState()
    private val exitBlock: BlockState = Blocks.BLUE_CONCRETE.defaultBlockState()

    private val random = Random()
    private var entrancePos: BlockPos? = null
    private var exitPos: BlockPos? = null

    fun generateMaze(world: ServerLevel, startPos: BlockPos) {
        val maze = Array<BooleanArray?>(size) { BooleanArray(size) }
        carvePassage(1, 1, maze)

        entrancePos = getRandomBorderPosition(startPos)
        do {
            exitPos = getRandomBorderPosition(startPos)
        } while (entrancePos == exitPos)

        for (x in 0 until size) {
            for (z in 0 until size) {
                val blockPos = startPos.offset(x, 0, z)
                if (blockPos == entrancePos) {
                    world.setBlockAndUpdate(blockPos, entranceBlock)
                } else if (blockPos == exitPos) {
                    world.setBlockAndUpdate(blockPos, exitBlock)
                } else if (maze[x]!![z]) {
                    world.setBlockAndUpdate(blockPos, pathBlock)
                } else {
                    world.setBlockAndUpdate(blockPos, wallBlock)
                }
            }
        }
    }

    private fun carvePassage(x: Int, z: Int, maze: Array<BooleanArray?>) {
        maze[x]!![z] = true
        val dirs = intArrayOf(0, 1, 2, 3)
        shuffleArray(dirs)

        for (dir in dirs) {
            var nx = x
            var nz = z
            if (dir == 0) nx += 2
            if (dir == 1) nz += 2
            if (dir == 2) nx -= 2
            if (dir == 3) nz -= 2

            if (nx > 0 && nx < size && nz > 0 && nz < size && !maze[nx]!![nz]) {
                maze[x + (nx - x) / 2]!![z + (nz - z) / 2] = true
                carvePassage(nx, nz, maze)
            }
        }
    }

    private fun shuffleArray(array: IntArray) {
        for (i in array.size - 1 downTo 1) {
            val index = random.nextInt(i + 1)
            val temp = array[index]
            array[index] = array[i]
            array[i] = temp
        }
    }

    private fun getRandomBorderPosition(startPos: BlockPos): BlockPos {
        val side = random.nextInt(4) // Select one of the four sides
        var pos: Int

        when (side) {
            0 -> {
                pos = random.nextInt(size / 2) * 2 + 1 // Ensure the entrance/exit is in a path (odd number)
                return startPos.offset(pos, 0, 0) // Top edge
            }

            1 -> {
                pos = random.nextInt(size / 2) * 2 + 1
                return startPos.offset(pos, 0, size - 1) // Bottom edge
            }

            2 -> {
                pos = random.nextInt(size / 2) * 2 + 1
                return startPos.offset(0, 0, pos) // Left edge
            }

            3 -> {
                pos = random.nextInt(size / 2) * 2 + 1
                return startPos.offset(size - 1, 0, pos) // Right edge
            }

            else -> throw IllegalArgumentException("Invalid side")
        }
    }

}
