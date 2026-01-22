package com.redcraft86.oddsandends.common.features;

import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

import com.redcraft86.oddsandends.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Partially adapted from BetterNether (Fabric Mod) which is under the MIT license.
 * <a href="https://github.com/quiqueck/BetterNether/blob/1.21/src/main/java/org/betterx/betternether/portals/BNPortalShape.java">Src</a>
 */
public class ShapelessPortal {
    private static final int MAX_SEARCH_DIST = 32;
    private static final Direction[] DIR_X = {Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST};
    private static final Direction[] DIR_Z = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};

    private final List<BlockPos> portalBlocks = new LinkedList<>();
    private final BlockPos.MutableBlockPos min;
    private final BlockPos.MutableBlockPos max;
    private final Direction.Axis axis;
    private final LevelAccessor level;

    private int numPortalBlocks = 0;
    private boolean bValid = true;

    public ShapelessPortal(LevelAccessor levelAccess, BlockPos startBlock, Direction.Axis portalAxis) {
        level = levelAccess;
        axis = portalAxis;

        // Code below check outwards from the initial block, searching all connected portals and valid frames.
        // The bounding box of the structure is tracked within min and max for height and width access later.
        final Direction[] directions = axis.equals(Direction.Axis.X) ? DIR_X : DIR_Z;
        Stack<BlockPos> toExplore = new Stack<>();
        min = startBlock.mutable();
        max = startBlock.mutable();
        portalBlocks.clear();

        toExplore.add(startBlock);
        while (!toExplore.isEmpty()) {
            BlockPos curPos = toExplore.pop();
            if (level.getBlockState(curPos).is(Blocks.NETHER_PORTAL)) {
                numPortalBlocks++;
            }

            // Add to collection and update bounds
            portalBlocks.add(curPos);
            if (curPos.getX() < min.getX()) min.setX(curPos.getX());
            if (curPos.getY() < min.getY()) min.setY(curPos.getY());
            if (curPos.getZ() < min.getZ()) min.setZ(curPos.getZ());
            if (curPos.getX() > max.getX()) max.setX(curPos.getX());
            if (curPos.getY() > max.getY()) max.setY(curPos.getY());
            if (curPos.getZ() > max.getZ()) max.setZ(curPos.getZ());

            // Explore neighbors
            for (Direction dir : directions) {
                BlockPos nextPos = curPos.relative(dir, 1);
                if (portalBlocks.contains(nextPos)) {
                    continue;
                }

                // Validate distance from origin
                if (Math.abs(nextPos.getX() - startBlock.getX()) > MAX_SEARCH_DIST
                        || Math.abs(nextPos.getZ() - startBlock.getZ()) > MAX_SEARCH_DIST
                        || Math.abs(nextPos.getY() - startBlock.getY()) > MAX_SEARCH_DIST) {

                    portalBlocks.clear();
                    bValid = false;
                    return;
                }

                BlockState nextState = level.getBlockState(nextPos);
                if (nextState.isAir() || nextState.is(BlockTags.FIRE)
                        || nextState.is(Blocks.NETHER_PORTAL)) { // Check air block

                    toExplore.add(nextPos);
                } else if (!nextState.is(ModTags.Blocks.NETHER_PORTAL_FRAME)) { // Check non frame
                    portalBlocks.clear();
                    bValid = false;
                    return;
                }
            }
        }
    }

    public BlockPos calcBottomLeft() {
        return (!bValid || max == null || min == null) ? null : min;
    }

    public int calcHeight() {
        if (!bValid || max == null || min == null) {
            return 0;
        }

        return switch (axis) {
            case X, Z -> max.getY() - min.getY();
            default -> 0;
        };
    }

    public int calcWidth() {
        if (!bValid || max == null || min == null) {
            return 0;
        }

        return switch (axis) {
            case X -> max.getX() - min.getX();
            case Z -> max.getZ() - min.getZ();
            default -> 0;
        };
    }

    public void createPortal() {
        final BlockState state = Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        portalBlocks.forEach(p -> level.setBlock(p, state, NetherPortalBlock.UPDATE_ALL));
    }

    public boolean isComplete() {
        return bValid && numPortalBlocks == portalBlocks.size();
    }

    public boolean isValid() {
        return bValid;
    }
}
