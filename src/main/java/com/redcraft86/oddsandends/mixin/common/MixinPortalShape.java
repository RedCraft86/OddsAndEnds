package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.oddsandends.features.ShapelessPortal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public class MixinPortalShape {
    @Unique
    private ShapelessPortal portal;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void PortalShape(LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis, CallbackInfo ci) {
        if (portal == null && CommonCfg.SHAPELESS_NETHER_PORTALS.get()) {
            portal = new ShapelessPortal(level, bottomLeft, axis);
        }
    }

    @Inject(method = "calculateBottomLeft", at = @At(value = "HEAD"), cancellable = true)
    private void calcBottomLeft(BlockPos blockPos, CallbackInfoReturnable<BlockPos> cir) {
        if (portal != null) {
            cir.setReturnValue(portal.calcBottomLeft());
        }
    }

    @Inject(method = "calculateHeight", at = @At(value = "HEAD"), cancellable = true)
    private void calcHeight(CallbackInfoReturnable<Integer> cir) {
        if (portal != null) {
            cir.setReturnValue(portal.calcHeight());
        }
    }

    @Inject(method = "calculateWidth", at = @At(value = "HEAD"), cancellable = true)
    private void calcWidth(CallbackInfoReturnable<Integer> cir) {
        if (portal != null) {
            cir.setReturnValue(portal.calcWidth());
        }
    }

    @Inject(method = "createPortalBlocks", at = @At(value = "HEAD"), cancellable = true)
    private void createPortal(CallbackInfo ci) {
        if (portal != null) {
            portal.createPortal();
            ci.cancel();
        }
    }

    @Inject(method = "isComplete", at = @At(value = "HEAD"), cancellable = true)
    private void checkComplete(CallbackInfoReturnable<Boolean> cir) {
        if (portal != null) {
            cir.setReturnValue(portal.isComplete());
        }
    }

    @Inject(method = "isValid", at = @At(value = "HEAD"), cancellable = true)
    private void checkValid(CallbackInfoReturnable<Boolean> cir) {
        if (portal != null) {
            cir.setReturnValue(portal.isValid());
        }
    }
}
