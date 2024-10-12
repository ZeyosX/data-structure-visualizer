package org.binarystorm.datastructurevisualizer.generators

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.Random

class MazeGenerator(size: Int, world: ServerLevel, startPos: BlockPos) : Generator(size, world, startPos) {
    private val wallBlock: BlockState = Blocks.WHITE_CONCRETE.defaultBlockState()
    private val pathBlock: BlockState = Blocks.YELLOW_CONCRETE.defaultBlockState()
    private val entranceBlock: BlockState = Blocks.BLACK_CONCRETE.defaultBlockState()
    private val exitBlock: BlockState = Blocks.BLUE_CONCRETE.defaultBlockState()

    private val random = Random()

    override fun doGenerate(world: ServerLevel, startPos: BlockPos): ArrayList<ArrayList<Pair<BlockPos, BlockState>>> {
        val maze = Array(size) { BooleanArray(size) } // No need for nullable arrays
        carvePath(1, 1, maze)

        val generatedBlocks = ArrayList<ArrayList<Pair<BlockPos, BlockState>>>()

        var entrancePos = getRandomBorderPosition(startPos)
        var exitPos: BlockPos
        do {
            exitPos = getRandomBorderPosition(startPos)
        } while (entrancePos == exitPos)

        for (x in 0 until size) {
            val rowBlocks = ArrayList<Pair<BlockPos, BlockState>>()
            for (z in 0 until size) {
                val blockPos = startPos.offset(x, 0, z)
                val newBlockState: BlockState = when {
                    blockPos == entrancePos -> entranceBlock
                    blockPos == exitPos -> exitBlock
                    maze[x][z] -> pathBlock
                    else -> wallBlock
                }

                val oldBlockState = world.getBlockState(blockPos)
                rowBlocks.add(Pair(blockPos, oldBlockState))
                world.setBlockAndUpdate(blockPos, newBlockState)

            }
            generatedBlocks.add(rowBlocks)
        }

        return generatedBlocks
    }


    private fun carvePath(x: Int, z: Int, maze: Array<BooleanArray>) {
        maze[x][z] = true
        val directions = intArrayOf(0, 1, 2, 3)
        shuffleArray(directions)

        for (dir in directions) {
            var newX = x
            var newZ = z
            when (dir) {
                0 -> newX += 2
                1 -> newZ += 2
                2 -> newX -= 2
                3 -> newZ -= 2
            }

            if (newX > 0 && newX < size && newZ > 0 && newZ < size && !maze[newX][newZ]) {
                maze[x + (newX - x) / 2][z + (newZ - z) / 2] = true
                carvePath(newX, newZ, maze)
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
        val side = random.nextInt(4)
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
