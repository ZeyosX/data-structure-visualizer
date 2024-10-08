package org.binarystorm.datastructurevisualizer.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import org.binarystorm.datastructurevisualizer.searchalgorithms.graph.BreadthFirstSearchGraphSearchAlgorithm
import org.binarystorm.datastructurevisualizer.searchalgorithms.graph.DepthFirstSearchGraphSearchAlgorithm

object PathCommand {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack?>) {
        dispatcher.register(
            Commands.literal("findpath")
                .then(
                    Commands.argument<String>("algorithm", StringArgumentType.word())
                        .then(Commands.argument<Int>("delay in ms", IntegerArgumentType.integer(1, 1000))
                            .executes { context ->
                                val algorithm = StringArgumentType.getString(context, "algorithm").lowercase()
                                val delay = IntegerArgumentType.getInteger(context, "delay in ms")

                                val searchAlgorithm = when (algorithm) {
                                    "bfs" -> BreadthFirstSearchGraphSearchAlgorithm(delay.toLong())
                                    "dfs" -> DepthFirstSearchGraphSearchAlgorithm(delay.toLong())
                                    else -> return@executes 0
                                }

                                val source = context.getSource()
                                val world = source.level
                                val player: Player = source.playerOrException

                                val hitResult = player.pick(20.0, 0.0f, false)
                                if (hitResult is BlockHitResult) {
                                    val entrancePos = hitResult.blockPos
                                    searchAlgorithm.findPath(world, entrancePos)
                                    return@executes 1
                                }

                                source.sendFailure(Component.literal("You are not looking at a valid block."))
                                0
                            }
                        )
                )
        )
    }
}
