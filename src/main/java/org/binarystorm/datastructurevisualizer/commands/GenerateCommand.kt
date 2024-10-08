package org.binarystorm.datastructurevisualizer.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import org.binarystorm.datastructurevisualizer.generators.MazeGenerator

object GenerateCommand {
    infix fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("generate")
                .then(
                    Commands.literal("maze")
                        .then(
                            Commands
                                .argument<Int>("size", IntegerArgumentType.integer(1, 100))
                                .executes { context -> generateMaze(context) }
                        )
                )
        )
    }

    private fun generateMaze(context: CommandContext<CommandSourceStack>): Int {
        val size = IntegerArgumentType.getInteger(context, "size")
        val world = context.source.level
        val startPos = context.source.playerOrException.blockPosition()
        MazeGenerator(size).generateMaze(world, startPos)
        return 0
    }
}