package com.redcraft86.oddsandends.common;

import java.util.*;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.util.Mth;
import net.minecraft.server.TickTask;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;

public class ImprovedBoneMeal {
    private static final Random RANDOM = new Random();
    public static List<Block> flowers = new ArrayList<>();

    public static boolean handleInteract(Level level, Player player, ItemStack item, BlockPos pos) {
        if (!CommonCfg.DIRT_TO_GRASS.get() || !item.is(Items.BONE_MEAL)) {
            return false;
        }

        if (level.getBlockState(pos).is(Blocks.DIRT)) {
            level.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
            player.swing(InteractionHand.MAIN_HAND, true);
            if (!player.isCreative()) {
                item.shrink(1);
            }
            return true;
        }

        return false;
    }

    public static void handleBoneMeal(Level level, Player player, BlockPos originPos) {
        float shortGrass = Mth.clamp((float) CommonCfg.SHORT_GRASS_CHANCE.get() / 100.0f, 0.0f, 100.0f);
        float tallGrass = Mth.clamp((float) CommonCfg.SHORT_GRASS_CHANCE.get() / 100.0f, 0.0f, 100.0f);
        if (shortGrass >= 0.99f || tallGrass >= 0.99f) {
            return;
        }

        if (CommonCfg.SNEAKY_GRASS_CHANCE.get() && !player.isShiftKeyDown()) {
            return;
        }

        if (flowers.isEmpty()) {
            generateFlowerList(level);
        }

        int x = originPos.getX();
        int y = originPos.getY();
        int z = originPos.getZ();

        level.getServer().tell(new TickTask(level.getServer().getTickCount(), () -> {
            Iterator<BlockPos> it = BlockPos.betweenClosedStream(x-6, y, z-6, x+6, y+1, z+6).iterator();
            while (it.hasNext()) {
                BlockPos pos = it.next();
                Block block = level.getBlockState(pos).getBlock();
                if (block == Blocks.SHORT_GRASS && RANDOM.nextFloat() > shortGrass) {
                    level.setBlockAndUpdate(pos, airOrRandomFlower().defaultBlockState());
                } else if (block == Blocks.TALL_GRASS && RANDOM.nextFloat() > tallGrass) {
                    // If tall grass, silently break the top block first to avoid breaking effect
                    boolean atBottom = level.getBlockState(pos.above()).is(Blocks.TALL_GRASS);
                    BlockPos upperPos = atBottom ? pos.above() : pos;
                    BlockPos lowerPos = atBottom ? pos : pos.below();

                    level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 2 | 16);
                    level.setBlockAndUpdate(lowerPos, airOrRandomFlower().defaultBlockState());
                }
            }
        }));
    }

    private static Block airOrRandomFlower() {
        float chance = Mth.clamp((float) CommonCfg.RANDOM_FLOWER_CHANCE.get() / 100.0f, 0.0f, 100.0f);
        if (RANDOM.nextFloat() < chance) {
            return flowers.get(RANDOM.nextInt(flowers.size()));
        } else {
            return Blocks.AIR;
        }
    }

    public static void generateFlowerList(Level level) {
        if (level == null) {
            return;
        }

        flowers.clear();
        Registry<Block> blockRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK);
        for (Block block : blockRegistry) {
            if (block instanceof FlowerBlock) {
                ResourceLocation id = blockRegistry.getKey(block);
                if (id != null && !CommonCfg.FLOWER_BLACKLIST.get().contains(id.toString())) {
                    flowers.add(block);
                }
            }
        }
    }
}
