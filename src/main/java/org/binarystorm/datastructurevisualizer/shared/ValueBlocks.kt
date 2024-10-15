package org.binarystorm.datastructurevisualizer.shared

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class ValueBlocks(private val value: Int) : Comparable<ValueBlocks> {
    val blockState: BlockState
        get() {
            return when (value) {
                0 -> Blocks.WHITE_CONCRETE.defaultBlockState()
                1 -> Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState()
                2 -> Blocks.GRAY_CONCRETE.defaultBlockState()
                3 -> Blocks.BLACK_CONCRETE.defaultBlockState()
                4 -> Blocks.BROWN_CONCRETE.defaultBlockState()
                5 -> Blocks.RED_CONCRETE.defaultBlockState()
                6 -> Blocks.ORANGE_CONCRETE.defaultBlockState()
                7 -> Blocks.YELLOW_CONCRETE.defaultBlockState()
                8 -> Blocks.LIME_CONCRETE.defaultBlockState()
                9 -> Blocks.GREEN_CONCRETE.defaultBlockState()
                10 -> Blocks.CYAN_CONCRETE.defaultBlockState()
                11 -> Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState()
                12 -> Blocks.BLUE_CONCRETE.defaultBlockState()
                13 -> Blocks.PURPLE_CONCRETE.defaultBlockState()
                14 -> Blocks.MAGENTA_CONCRETE.defaultBlockState()
                15 -> Blocks.PINK_CONCRETE.defaultBlockState()
                else -> Blocks.AIR.defaultBlockState()
            }
        }

    override fun compareTo(other: ValueBlocks): Int {
        return this.value.compareTo(other.value)
    }

    companion object {
        fun createValueBlock(blockState: BlockState): ValueBlocks {
            return when (blockState.block) {
                Blocks.WHITE_CONCRETE -> ValueBlocks(0)
                Blocks.LIGHT_GRAY_CONCRETE -> ValueBlocks(1)
                Blocks.GRAY_CONCRETE -> ValueBlocks(2)
                Blocks.BLACK_CONCRETE -> ValueBlocks(3)
                Blocks.BROWN_CONCRETE -> ValueBlocks(4)
                Blocks.RED_CONCRETE -> ValueBlocks(5)
                Blocks.ORANGE_CONCRETE -> ValueBlocks(6)
                Blocks.YELLOW_CONCRETE -> ValueBlocks(7)
                Blocks.LIME_CONCRETE -> ValueBlocks(8)
                Blocks.GREEN_CONCRETE -> ValueBlocks(9)
                Blocks.CYAN_CONCRETE -> ValueBlocks(10)
                Blocks.LIGHT_BLUE_CONCRETE -> ValueBlocks(11)
                Blocks.BLUE_CONCRETE -> ValueBlocks(12)
                Blocks.PURPLE_CONCRETE -> ValueBlocks(13)
                Blocks.MAGENTA_CONCRETE -> ValueBlocks(14)
                Blocks.PINK_CONCRETE -> ValueBlocks(15)
                else -> ValueBlocks(-1)
            }
        }
    }
}

