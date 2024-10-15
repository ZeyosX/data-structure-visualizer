package org.binarystorm.datastructurevisualizer.shared

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class ArrayUtils {
    companion object {
        fun getValue(blockState: BlockState) : Int{
            return when (blockState.block) {
                Blocks.WHITE_CONCRETE -> 0
                Blocks.LIGHT_GRAY_CONCRETE -> 1
                Blocks.GRAY_CONCRETE -> 2
                Blocks.BLACK_CONCRETE -> 3
                Blocks.BROWN_CONCRETE -> 4
                Blocks.RED_CONCRETE -> 5
                Blocks.ORANGE_CONCRETE -> 6
                Blocks.YELLOW_CONCRETE -> 7
                Blocks.LIME_CONCRETE -> 8
                Blocks.GREEN_CONCRETE -> 9
                Blocks.CYAN_CONCRETE -> 10
                Blocks.LIGHT_BLUE_CONCRETE -> 11
                Blocks.BLUE_CONCRETE -> 12
                Blocks.PURPLE_CONCRETE -> 13
                Blocks.MAGENTA_CONCRETE -> 14
                Blocks.PINK_CONCRETE -> 15
                else -> -1
            }
        }
    }
}