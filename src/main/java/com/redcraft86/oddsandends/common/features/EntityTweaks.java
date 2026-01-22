package com.redcraft86.oddsandends.common.features;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.registries.ModGameRules;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.damagesource.DamageTypes;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class EntityTweaks {
    @SubscribeEvent
    static void handleTrades(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        if (level.isClientSide() || !CommonCfg.INFINITE_TRADES.get()) {
            return;
        }

        if (event.getTarget() instanceof AbstractVillager villager) {
            for (MerchantOffer offer : villager.getOffers()) {
                offer.maxUses = Integer.MAX_VALUE;
                offer.resetUses();
            }
        }
    }

    @SubscribeEvent
    static void handleNoAtkCooldown(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        if (level.isClientSide() || target.getLastDamageSource() == null) {
            return;
        }

        if (level.getGameRules().getBoolean(ModGameRules.NO_ATK_COOLDOWN)
                && target.getLastDamageSource().is(DamageTypes.PLAYER_ATTACK)) {

            target.invulnerableTime = 0;
        }
    }
}
