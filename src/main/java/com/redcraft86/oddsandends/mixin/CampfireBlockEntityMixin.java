package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.common.SoulCampfireEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void onCookTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity, CallbackInfo ci) {
        if (!level.isClientSide() && state.is(Blocks.SOUL_CAMPFIRE)) {
            SoulCampfireEffects.applyEffects(level, pos);
        }
    }
}
