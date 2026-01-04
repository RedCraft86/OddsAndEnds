package com.redcraft86.oddsandends.common;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistries;

public class SoulCampfireEffects {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static Map<MobEffect, Integer> effects = new HashMap<>();

    public static void applyEffects(Level level, BlockPos pos) {
        int range = CommonCfg.SOULFIRE_EFFECT_RANGE.get();
        if (range <= 0 || CommonCfg.SOULFIRE_EFFECTS.get().isEmpty()) {
            return;
        }

        AABB box = new AABB(pos).inflate(Math.max(1, CommonCfg.SOULFIRE_EFFECT_RANGE.get()));
        List<Player> players = level.getEntitiesOfClass(Player.class, box);
        for (Player player : players) {
            if (player == null || player.isSpectator()) {
                continue;
            }

            if (CommonCfg.SOULFIRE_CLEAR_HARM.get()) {
                player.getActiveEffects().stream()
                        .filter(effect -> effect != null
                                && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                        .map(MobEffectInstance::getEffect).toList().forEach(player::removeEffect);
            }

            for (Map.Entry<MobEffect, Integer> entry : effects.entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    player.addEffect(new MobEffectInstance(entry.getKey(), 40,
                            entry.getValue(), false, false, true));
                }
            }
        }
    }

    public static boolean validateEntry(final Object obj) {
        if (obj instanceof String string) {
            String[] parts = string.split(" ", 2);
            if (parts.length != 2) {
                return false;
            }

            try {
                Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return false;
            }

            String[] id = parts[0].split(":", 2);
            return id.length == 2
                    && ResourceLocation.isValidNamespace(id[0])
                    && ResourceLocation.isValidPath(id[1]);
        }

        return false;
    }

    public static void generateEffectList() {
        effects.clear();
        List<? extends String> entries = CommonCfg.SOULFIRE_EFFECTS.get();
        for (String entry : entries) {
            String[] values = entry.split(" ", 2);
            if (values.length != 2) {
                LOGGER.warn("[Soulfire Effects] Failed to parse effect: {}", entry);
                continue;
            }

            ResourceLocation resource = ResourceLocation.tryParse(values[0]);
            int level;
            try {
                level = Integer.parseInt(values[1]);
            } catch (NumberFormatException e) {
                level = 0;
            }

            if (resource == null || level <= 0) {
                LOGGER.warn("[Soulfire Effects] Invalid effect entry: {}", entry);
                continue;
            }

            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(resource);
            if (effect == null) {
                LOGGER.warn("[Soulfire Effects] Mob Effect: {} does not exist!", resource);
                continue;
            }

            effects.put(effect, level - 1);
        }
    }
}
