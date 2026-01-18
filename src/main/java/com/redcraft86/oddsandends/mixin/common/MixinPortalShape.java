package com.redcraft86.oddsandends.mixin.common;

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
import net.minecraft.world.level.portal.PortalShape;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Code partially adapted from BetterNether (Fabric Mod) which is under the MIT license:
 * <a href="https://github.com/quiqueck/BetterNether/blob/1.21/src/main/java/org/betterx/betternether/portals/BNPortalShape.java">Src</a>
 */
@Mixin(PortalShape.class)
public class MixinPortalShape {
    @Unique private static final Direction[] DIR_X = {Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST};
    @Unique private static final Direction[] DIR_Z = {Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};
    @Unique private static final int MAX_SEARCH_DIST = 32;

    @Unique private final List<BlockPos> portalBlocks = new LinkedList<>();
    @Unique private BlockPos.MutableBlockPos min = null;
    @Unique private BlockPos.MutableBlockPos max = null;
    @Unique private boolean bValid = true;

    @Shadow @Final private LevelAccessor level;
    @Shadow @Final private Direction.Axis axis;
    @Shadow private int numPortalBlocks;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void PortalShape(LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis, CallbackInfo ci) {
        // Code below check outwards from the initial block, searching all connected portals and valid frames.
        // The bounding box of the structure is tracked within min and max for height and width access later.
        final Direction[] directions = axis.equals(Direction.Axis.X) ? DIR_X : DIR_Z;
        Stack<BlockPos> toExplore = new Stack<>();
        min = bottomLeft.mutable();
        max = bottomLeft.mutable();
        portalBlocks.clear();

        toExplore.add(bottomLeft);
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
                if (Math.abs(nextPos.getX() - bottomLeft.getX()) > MAX_SEARCH_DIST
                    || Math.abs(nextPos.getZ() - bottomLeft.getZ()) > MAX_SEARCH_DIST
                    || Math.abs(nextPos.getY() - bottomLeft.getY()) > MAX_SEARCH_DIST) {

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

    @Inject(method = "calculateBottomLeft", at = @At(value = "HEAD"), cancellable = true)
    private void calcBottomLeft(BlockPos blockPos, CallbackInfoReturnable<BlockPos> cir) {
        if (!bValid || max == null || min == null) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "calculateHeight", at = @At(value = "HEAD"), cancellable = true)
    private void calcHeight(CallbackInfoReturnable<Integer> cir) {
        if (!bValid || max == null || min == null) {
            cir.setReturnValue(0);
        }

        cir.setReturnValue(switch (axis) {
            case Direction.Axis.X, Direction.Axis.Z -> max.getY() - min.getY();
            default -> 0;
        });
    }

    @Inject(method = "calculateWidth", at = @At(value = "HEAD"), cancellable = true)
    private void calcWidth(CallbackInfoReturnable<Integer> cir) {
        if (!bValid || max == null || min == null) {
            cir.setReturnValue(0);
        }

        cir.setReturnValue(switch (axis) {
            case Direction.Axis.X -> max.getX() - min.getX();
            case Direction.Axis.Z -> max.getZ() - min.getZ();
            default -> 0;
        });
    }

    @Inject(method = "createPortalBlocks", at = @At(value = "HEAD"), cancellable = true)
    private void createPortal(CallbackInfo ci) {
        final BlockState state = Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        portalBlocks.forEach(p -> level.setBlock(p, state, NetherPortalBlock.UPDATE_ALL));
        ci.cancel();
    }

    @Inject(method = "isComplete", at = @At(value = "HEAD"), cancellable = true)
    private void checkComplete(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(bValid && numPortalBlocks == portalBlocks.size());
    }

    @Inject(method = "isValid", at = @At(value = "HEAD"), cancellable = true)
    private void checkValid(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(bValid);
    }
}
