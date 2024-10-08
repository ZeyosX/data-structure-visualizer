package org.binarystorm.datastructurevisualizer.searchalgorithms

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.HashMap

abstract class MazeBlock(pos: BlockPos, val type: MazeBlockType, val blockState: BlockState?) {
    @JvmField
    val blockPos: BlockPos = pos
    val neighbors: MutableMap<BlockPos, MazeBlock?> = HashMap<BlockPos, MazeBlock?>()

    abstract fun isTraversable(): Boolean

    fun addNeighbor(neighbor: MazeBlock) {
        neighbors.put(neighbor.blockPos, neighbor)
    }

    fun removeNeighbor(neighbor: MazeBlock) {
        neighbors.remove(neighbor.blockPos)
    }

    fun hasNeighbor(neighbor: MazeBlock): Boolean {
        return neighbors.containsKey(neighbor.blockPos)
    }

    fun getNeighborPositions(world: ServerLevel): List<BlockPos> {
        val neighborPositions = ArrayList<BlockPos>()
        neighborPositions.add(blockPos.north())
        neighborPositions.add(blockPos.south())
        neighborPositions.add(blockPos.east())
        neighborPositions.add(blockPos.west())
        return neighborPositions
    }

    companion object {
        fun createBlock(world: ServerLevel, pos: BlockPos): MazeBlock? {
            val worldBlockState = world.getBlockState(pos)

            return if (worldBlockState == WallBlock.Companion.WALL_BLOCK_STATE) {
                WallBlock(pos)
            } else if (worldBlockState == PathBlock.Companion.PATH_BLOCK_STATE) {
                PathBlock(pos)
            } else if (worldBlockState == EntranceBlock.Companion.BLOCK_STATE) {
                EntranceBlock(pos)
            } else if (worldBlockState == ExitBlock.Companion.EXIT_BLOCK_STATE) {
                ExitBlock(pos)
            } else {
                null
            }
        }
    }
}

internal class WallBlock(pos: BlockPos) : MazeBlock(pos, MazeBlockType.WALL, WALL_BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return false
    }

    companion object {
        @JvmField
        val WALL_BLOCK_STATE: BlockState = Blocks.WHITE_CONCRETE.defaultBlockState()
    }
}

internal class PathBlock(pos: BlockPos) : MazeBlock(pos, MazeBlockType.PATH, PATH_BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        val PATH_BLOCK_STATE: BlockState = Blocks.YELLOW_CONCRETE.defaultBlockState()
    }
}

internal class EntranceBlock(pos: BlockPos) : MazeBlock(pos, MazeBlockType.ENTRANCE, BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        @JvmField
        val BLOCK_STATE: BlockState = Blocks.BLACK_CONCRETE.defaultBlockState()
    }
}

internal class ExitBlock(pos: BlockPos) : MazeBlock(pos, MazeBlockType.EXIT, EXIT_BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        @JvmField
        val EXIT_BLOCK_STATE: BlockState = Blocks.BLUE_CONCRETE.defaultBlockState()
    }
}

