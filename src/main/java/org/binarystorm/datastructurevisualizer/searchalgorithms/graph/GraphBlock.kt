package org.binarystorm.datastructurevisualizer.searchalgorithms.graph

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.HashMap

abstract class GraphBlock(pos: BlockPos, val type: GraphBlockType, val blockState: BlockState?) {
    @JvmField
    val blockPos: BlockPos = pos
    val neighbors: MutableMap<BlockPos, GraphBlock?> = HashMap<BlockPos, GraphBlock?>()

    abstract fun isTraversable(): Boolean

    fun addNeighbor(neighbor: GraphBlock) {
        neighbors.put(neighbor.blockPos, neighbor)
    }

    fun removeNeighbor(neighbor: GraphBlock) {
        neighbors.remove(neighbor.blockPos)
    }

    fun hasNeighbor(neighbor: GraphBlock): Boolean {
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
        fun createBlock(world: ServerLevel, pos: BlockPos): GraphBlock? {
            val worldBlockState = world.getBlockState(pos)

            return if (worldBlockState == GraphWallBlock.Companion.BLOCK_STATE) {
                GraphWallBlock(pos)
            } else if (worldBlockState == GraphPathBlock.Companion.BLOCK_STATE) {
                GraphPathBlock(pos)
            } else if (worldBlockState == GraphEntranceBlock.Companion.BLOCK_STATE) {
                GraphEntranceBlock(pos)
            } else if (worldBlockState == GraphExitBlock.Companion.BLOCK_STATE) {
                GraphExitBlock(pos)
            } else {
                null
            }
        }
    }
}

internal class GraphWallBlock(pos: BlockPos) : GraphBlock(pos, GraphBlockType.WALL, BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return false
    }

    companion object {
        @JvmField
        val BLOCK_STATE: BlockState = Blocks.WHITE_CONCRETE.defaultBlockState()
    }
}

internal class GraphPathBlock(pos: BlockPos) : GraphBlock(pos, GraphBlockType.PATH,BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        val BLOCK_STATE: BlockState = Blocks.YELLOW_CONCRETE.defaultBlockState()
    }
}

internal class GraphEntranceBlock(pos: BlockPos) : GraphBlock(pos, GraphBlockType.ENTRANCE, BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        @JvmField
        val BLOCK_STATE: BlockState = Blocks.BLACK_CONCRETE.defaultBlockState()
    }
}

internal class GraphExitBlock(pos: BlockPos) : GraphBlock(pos, GraphBlockType.EXIT, BLOCK_STATE) {
    override fun isTraversable(): Boolean {
        return true
    }

    companion object {
        @JvmField
        val BLOCK_STATE: BlockState = Blocks.BLUE_CONCRETE.defaultBlockState()
    }
}

