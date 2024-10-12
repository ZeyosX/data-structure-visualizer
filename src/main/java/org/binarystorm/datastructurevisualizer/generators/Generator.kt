package org.binarystorm.datastructurevisualizer.generators

import com.mojang.brigadier.context.CommandContext
import net.minecraft.client.Minecraft
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import java.util.Stack

abstract class Generator(val size: Int, val world: ServerLevel, val startPos: BlockPos) {
    fun generate() {
        val generatedBlocks = doGenerate(world, startPos)

        if (history.size > 10) {
            history.removeAt(0)
        }

        val flattenedGeneratedBlocks = generatedBlocks.flatten()

        history.add(flattenedGeneratedBlocks)
    }

    abstract fun doGenerate(world: ServerLevel, startPos: BlockPos): ArrayList<ArrayList<Pair<BlockPos, BlockState>>>


    companion object {
        private val history = Stack<List<Pair<BlockPos, BlockState>>>()
        fun undo(world: ServerLevel) {
            if (history.isEmpty()) {
                return
            }

            val undoBlocks = history.pop()
            undoBlocks.forEach { undoBlock ->
                world.setBlockAndUpdate(undoBlock.first, undoBlock.second)
            }
        }
    }
}