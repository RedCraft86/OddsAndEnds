package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.oddsandends.common.features.CozyCampfire;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class MixinCampfireBlockEntity {
    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void onCookTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity, CallbackInfo ci) {
        Block matchType = switch (CommonCfg.CAMPFIRE_TYPE.get()) {
            case REGULAR -> Blocks.CAMPFIRE;
            case SOULFIRE -> Blocks.SOUL_CAMPFIRE;
            case ANY -> null;
        };
        if (matchType == null || state.is(matchType)) {
            CozyCampfire.applyEffects(level, pos);
        }
    }
}
