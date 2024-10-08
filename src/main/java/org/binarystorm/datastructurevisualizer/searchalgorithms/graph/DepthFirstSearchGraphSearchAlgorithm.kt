package org.binarystorm.datastructurevisualizer.searchalgorithms.graph

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import java.util.Stack

class DepthFirstSearchGraphSearchAlgorithm(delay: Long = 50L) : GraphSearchAlgorithm(delay) {
    override fun searchGraph(
        world: ServerLevel,
        start: BlockPos
    ): ArrayList<ArrayList<GraphBlock>> {
        val graphLayout = ArrayList<ArrayList<GraphBlock>>()

        val stack: Stack<GraphBlock> = Stack()

        val entranceBlock = GraphEntranceBlock(start)
        stack.push(entranceBlock)

        while (stack.isNotEmpty()) {
            val currentBlock = stack.pop()
            graphLayout.add(ArrayList(listOf(currentBlock)))

            if (world.getBlockState(currentBlock.blockPos) == GraphExitBlock.Companion.BLOCK_STATE) {
                world.players().forEach { player ->
                    player.sendSystemMessage(Component.literal("Exit reached at position ${currentBlock.blockPos}"))
                }
                break
            }

            for (neighborPos in currentBlock.getNeighborPositions(world)) {
                val neighborBlock = GraphBlock.Companion.createBlock(world, neighborPos)

                if (neighborBlock != null && neighborBlock.isTraversable() && !isBlockInLayout(
                        graphLayout,
                        neighborPos
                    )
                ) {
                    currentBlock.addNeighbor(neighborBlock)
                    stack.push(neighborBlock)
                    graphLayout.add(ArrayList())
                }
            }
        }

        return graphLayout
    }

    private fun isBlockInLayout(mazeLayout: ArrayList<ArrayList<GraphBlock>>, blockPos: BlockPos): Boolean {
        for (row in mazeLayout) {
            for (node in row) {
                if (node.blockPos == blockPos) {
                    return true
                }
            }
        }
        return false
    }
}