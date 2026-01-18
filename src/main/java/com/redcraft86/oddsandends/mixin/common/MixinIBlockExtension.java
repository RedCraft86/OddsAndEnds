package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IBlockExtension.class)
public interface MixinIBlockExtension {
    @Overwrite
    default boolean isPortalFrame(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModTags.Blocks.NETHER_PORTAL_FRAME);
    }
}
