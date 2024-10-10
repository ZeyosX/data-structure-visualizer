package org.binarystorm.datastructurevisualizer.generators

import net.minecraft.world.level.block.Block
import java.util.Stack

abstract class Generator(val size: Int) {
    fun generate() {
        val generatedBlock = doGenerate()
        if (history.size > 10) {
            history.removeAt(0)
        }
        history.add(generatedBlock)
    }

    abstract fun doGenerate(): ArrayList<ArrayList<Block>>

    fun undo() {
        if (history.isEmpty()){
            return
        }

        val undoBlock = history.pop()

    }

    companion object {
        private val history = Stack<ArrayList<ArrayList<Block>>>()
    }
}