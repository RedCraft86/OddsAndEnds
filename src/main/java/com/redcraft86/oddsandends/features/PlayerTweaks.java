package com.redcraft86.oddsandends.features;

import com.redcraft86.oddsandends.ModTags;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

@Mod.EventBusSubscriber(modid = OddsAndEnds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlayerTweaks {
    @SubscribeEvent
    static void handlePlayerAttack(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Level level = target.level();
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        if (CommonCfg.NO_FRIENDLY_FIRE.get() && target instanceof OwnableEntity petMob) {
            event.setCanceled(petMob.getOwner() == player);
        }

        ItemStack item = player.getMainHandItem();
        if (CommonCfg.NO_VILLAGER_HIT.get() && target instanceof AbstractVillager) {
            event.setCanceled(item.isEmpty());
        }
    }

    @SubscribeEvent
    static void handlePlayerFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) {
            return;
        }

        if (CommonCfg.PLAYER_FALL_DAMPEN.get() && entity instanceof Player) {
            BlockState belowState = level.getBlockState(entity.blockPosition().below());
            event.setDamageMultiplier(belowState.is(ModTags.Blocks.DAMPEN_FALL_DAMAGE) ? 0.2f : 1.0f);
        }
    }

    @SubscribeEvent
    static void trueInfinity(ArrowNockEvent event) {
        if (CommonCfg.TRUE_INFINITY.get()) {
            ItemStack bow = event.getBow();
            if (bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0) {
                event.getEntity().startUsingItem(event.getHand());
                event.setAction(InteractionResultHolder.success(bow));
            }
        }
    }
}
