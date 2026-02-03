package com.redcraft86.oddsandends.features;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public final class MiscTweaks {
    @SubscribeEvent
    static void handleBoneMeal(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !CommonCfg.BONEMEAL_DIRT_TO_GRASS.get()) {
            return;
        }

        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();

        if (!item.is(Items.BONE_MEAL) || !level.getBlockState(pos).is(Blocks.DIRT)) {
            return;
        }

        player.swing(event.getHand(), true);
        level.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
        if (!player.isCreative()) {
            item.shrink(1);
        }

        event.setCanceled(true);
    }
}
