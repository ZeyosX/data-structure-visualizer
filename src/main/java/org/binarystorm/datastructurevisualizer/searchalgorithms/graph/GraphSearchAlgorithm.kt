package org.binarystorm.datastructurevisualizer.searchalgorithms.graph

import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks

abstract class GraphSearchAlgorithm(private val delay: Long = 50L) {
    fun findPath(world: ServerLevel, start: BlockPos) {
        val mazeLayout = buildGraph(world, start)
        if (mazeLayout.isEmpty()) {
            return
        }

        Thread {
            while (mazeLayout.isNotEmpty()) {
                val row = mazeLayout.removeAt(0)
                for (node in row) {
                    world.setBlock(node.blockPos, Blocks.GREEN_CONCRETE.defaultBlockState(), 3)
                    try {
                        Thread.sleep(delay)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()


    }

    fun buildGraph(world: ServerLevel, start: BlockPos): ArrayList<ArrayList<GraphBlock>> {
        if (world.getBlockState(start) != GraphEntranceBlock.Companion.BLOCK_STATE) {
            world.players().forEach { player ->
                player.sendSystemMessage(Component.literal("Entrance block is not valid"))
            }
            return ArrayList()
        }

        val graphLayout = searchGraph(world, start)
        return graphLayout
    }

    abstract fun searchGraph(world: ServerLevel, start: BlockPos): ArrayList<ArrayList<GraphBlock>>
}