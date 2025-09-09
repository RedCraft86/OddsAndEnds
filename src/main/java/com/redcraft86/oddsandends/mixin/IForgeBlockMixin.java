package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = IForgeBlock.class, remap = false)
public interface IForgeBlockMixin {
    @Overwrite
    default boolean isPortalFrame(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.OBSIDIAN) || (CommonCfg.CRYING_PORTALS.get() && state.is(Blocks.CRYING_OBSIDIAN));
    }
}
