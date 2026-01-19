package com.redcraft86.oddsandends.common.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.features.CommonTweaks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class EntityEvents {
    @SubscribeEvent
    static void onLivingDamage(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        if (level.isClientSide() || target.getLastDamageSource() == null) {
            return;
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity target = event.getEntity();
        Level level = target.level();
        if (level.isClientSide()) {
            return;
        }

        // TODO: noAttackCooldown
    }

    @SubscribeEvent
    static void trueInfinity(final LivingGetProjectileEvent event) {
        CommonTweaks.handleTrueInfinity(event);
    }
}
