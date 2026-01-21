package com.redcraft86.oddsandends.common.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.registries.ModGameRules;
import com.redcraft86.oddsandends.common.features.CommonTweaks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageTypes;

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

        if (level.getGameRules().getBoolean(ModGameRules.NO_ATK_COOLDOWN)
            && target.getLastDamageSource().is(DamageTypes.PLAYER_ATTACK)) {

            target.invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) {
            return;
        }

        event.setDamageMultiplier(CommonTweaks.handlePlayerFall(level, entity));
    }

    @SubscribeEvent
    static void trueInfinity(final LivingGetProjectileEvent event) {
        CommonTweaks.handleTrueInfinity(event);
    }
}
