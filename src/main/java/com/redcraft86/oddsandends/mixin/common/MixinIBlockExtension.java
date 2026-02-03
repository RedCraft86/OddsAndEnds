package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IBlockExtension.class, priority = 1001)
public interface MixinIBlockExtension {
    @Inject(method = "isPortalFrame", at = @At("HEAD"), cancellable = true)
    default void checkFrameBlock(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(state.is(ModTags.Blocks.NETHER_PORTAL_FRAME));
    }
}
