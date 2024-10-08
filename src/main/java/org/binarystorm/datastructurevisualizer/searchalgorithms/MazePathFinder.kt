package org.binarystorm.datastructurevisualizer.searchalgorithms

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import java.util.HashMap
import java.util.LinkedList
import java.util.List
import java.util.Queue

class MazePathFinder(private val entrancePos: BlockPos?, private val exitPos: BlockPos?) {
    private val searchBlock: BlockState = Blocks.ORANGE_STAINED_GLASS.defaultBlockState()
    private val shortestPathBlock: BlockState = Blocks.GREEN_STAINED_GLASS.defaultBlockState()

    // BFS for shortest path with visualization
    fun searchShortestPath(world: ServerLevel) {
        val queue: Queue<BlockPos> = LinkedList<BlockPos>()
        val cameFrom: MutableMap<BlockPos?, BlockPos?> =
            HashMap<BlockPos?, BlockPos?>() // Track where we came from for backtracking

        queue.add(entrancePos)
        cameFrom.put(entrancePos, null) // Start with no parent for the entrance

        while (!queue.isEmpty()) {
            val current = queue.poll()

            // Visualize current search position with orange stained glass
            world.setBlockAndUpdate(current, searchBlock)

            // If we reached the exit, backtrack to mark the shortest path
            if (current == exitPos) {
                markShortestPath(world, cameFrom, current)
                return
            }

            // Explore neighbors (4 directions: up, down, left, right)
            for (neighbor in getNeighbors(current)) {
                if (!cameFrom.containsKey(neighbor) && isPathBlock(world, neighbor)) {
                    queue.add(neighbor)
                    cameFrom.put(neighbor, current)
                }
            }
        }
    }

    private fun getNeighbors(pos: BlockPos): Iterable<BlockPos> {
        return List.of<BlockPos?>(
            pos.offset(1, 0, 0),  // Right
            pos.offset(-1, 0, 0),  // Left
            pos.offset(0, 0, 1),  // Down
            pos.offset(0, 0, -1) // Up
        )
    }

    private fun isPathBlock(world: ServerLevel, pos: BlockPos): Boolean {
        return world.getBlockState(pos).`is`(Blocks.AIR) // Check if the block is an air block (path)
    }

    // Backtrack from the exit to the entrance and mark the shortest path with green stained glass
    private fun markShortestPath(world: ServerLevel, cameFrom: MutableMap<BlockPos?, BlockPos?>, current: BlockPos) {
        var current = current
        while (cameFrom.get(current) != null) { // Stop when we reach the entrance (null parent)
            world.setBlockAndUpdate(current, shortestPathBlock)
            current = cameFrom.get(current)!!
        }
    }
}



