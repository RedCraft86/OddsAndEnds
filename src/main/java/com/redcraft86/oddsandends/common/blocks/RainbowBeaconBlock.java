package com.redcraft86.oddsandends.common.blocks;

import java.util.List;
import net.minecraft.util.Mth;

import com.redcraft86.lanternlib.api.blocks.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class RainbowBeaconBlock extends ModBlock {
    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 8, 16);
    private static final String TOOLTIP_KEY = "tooltip.oddsandends.rainbow_beacon";

    public RainbowBeaconBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    }

    @Override
    public List<Component> getTooltips() {
        return List.of(Component.translatable(TOOLTIP_KEY).withStyle(ChatFormatting.GRAY));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        if (level instanceof Level world) {
            float hue = (world.getGameTime() % 240) / 240.0f;
            return Mth.hsvToRgb(hue, 1.0f, 1.0f);
        }
        return 0xFFFFFF;
    }
}
