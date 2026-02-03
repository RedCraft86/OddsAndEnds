package com.redcraft86.oddsandends.features;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.registries.ModRules;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.damagesource.DamageTypes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = OddsAndEnds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EntityTweaks {
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
    static void handleNoAtkCooldown(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        if (level.isClientSide() || target.getLastDamageSource() == null) {
            return;
        }

        if (level.getGameRules().getBoolean(ModRules.NO_ATK_COOLDOWN)
                && target.getLastDamageSource().is(DamageTypes.PLAYER_ATTACK)) {

            target.invulnerableTime = 0;
        }
    }
}
