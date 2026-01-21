package com.redcraft86.oddsandends.common.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.features.CommonTweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class PlayerEvents {
    @SubscribeEvent
    static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();

        boolean bHandled = false;
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            bHandled = CommonTweaks.handleBonemeal(level, player, item, pos);
        }

        if (bHandled) {
            player.swing(event.getHand(), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        Entity target = event.getTarget();
        ItemStack item = event.getItemStack();

        boolean bHandled = false;
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            CommonTweaks.handleTrades(target);
        }

        if (bHandled) {
            player.swing(event.getHand(), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onAttackEntity(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Level level = target.level();
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        ItemStack item = player.getMainHandItem();

        event.setCanceled(CommonTweaks.handlePlayerAttack(player, target, item));
    }
}
