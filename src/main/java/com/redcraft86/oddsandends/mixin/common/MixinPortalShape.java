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
    @Unique private ShapelessPortal one_portal;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void one_init(LevelAccessor level, BlockPos bottomLeft, Direction.Axis axis, CallbackInfo ci) {
        if (one_portal == null && CommonCfg.SHAPELESS_NETHER_PORTALS.get()) {
            one_portal = new ShapelessPortal(level, bottomLeft, axis);
        }
    }

    @Inject(method = "calculateBottomLeft", at = @At(value = "HEAD"), cancellable = true)
    private void one_calcBottomLeft(BlockPos blockPos, CallbackInfoReturnable<BlockPos> cir) {
        if (one_portal != null) {
            cir.setReturnValue(one_portal.calcBottomLeft());
        }
    }

    @Inject(method = "calculateHeight", at = @At(value = "HEAD"), cancellable = true)
    private void one_calcHeight(CallbackInfoReturnable<Integer> cir) {
        if (one_portal != null) {
            cir.setReturnValue(one_portal.calcHeight());
        }
    }

    @Inject(method = "calculateWidth", at = @At(value = "HEAD"), cancellable = true)
    private void one_calcWidth(CallbackInfoReturnable<Integer> cir) {
        if (one_portal != null) {
            cir.setReturnValue(one_portal.calcWidth());
        }
    }

    @Inject(method = "createPortalBlocks", at = @At(value = "HEAD"), cancellable = true)
    private void one_createPortal(CallbackInfo ci) {
        if (one_portal != null) {
            one_portal.createPortal();
            ci.cancel();
        }
    }

    @Inject(method = "isComplete", at = @At(value = "HEAD"), cancellable = true)
    private void one_checkComplete(CallbackInfoReturnable<Boolean> cir) {
        if (one_portal != null) {
            cir.setReturnValue(one_portal.isComplete());
        }
    }

    @Inject(method = "isValid", at = @At(value = "HEAD"), cancellable = true)
    private void one_checkValid(CallbackInfoReturnable<Boolean> cir) {
        if (one_portal != null) {
            cir.setReturnValue(one_portal.isValid());
        }
    }
}
