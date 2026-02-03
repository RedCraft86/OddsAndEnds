package com.redcraft86.oddsandends.features;

import java.util.List;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.lanternlib.utils.ValidationUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.Tags;

@Mod.EventBusSubscriber(modid = OddsAndEnds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CozyCampfire {
    public enum CampfireType {
        REGULAR, SOULFIRE, ANY
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int EFFECT_TIME = 20 * 2; // 20 ticks/second * 2 seconds

    public static Object2IntOpenHashMap<MobEffect> effects = new Object2IntOpenHashMap<>();
    public static ObjectOpenHashSet<MobEffect> hostileEffects = new ObjectOpenHashSet<>();

    @SubscribeEvent
    static void onServerStart(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide()) {
            generateEffectList();
        }
    }

    public static void applyEffects(Level level, BlockPos pos) {
        int range = CommonCfg.CAMPFIRE_RANGE.get();
        boolean clearDebuff = CommonCfg.CAMPFIRE_CLEAR_DEBUFFS.get();
        boolean repelEnemies = CommonCfg.CAMPFIRE_REPEL_HOSTILES.get();
        if (range <= 0 && effects.isEmpty() && !clearDebuff && !repelEnemies) {
            return;
        }

        List<LivingEntity> entities = level.getEntitiesOfClass(
            LivingEntity.class, new AABB(
                pos.getX() - range,
                pos.getY() - range,
                pos.getZ() - range,
                pos.getX() + range,
                pos.getY() + range,
                pos.getZ() + range
            )
        );

        for (LivingEntity entity : entities) {
            if (entity instanceof Player player) {
                if (player.isSpectator()) {
                    continue;
                }

                if (clearDebuff) {
                    player.getActiveEffects().stream().filter(CozyCampfire::isDebuff)
                            .map(MobEffectInstance::getEffect).toList().forEach(player::removeEffect);
                    // NOTE: toList() is needed as otherwise it directly reads and writes activeEffects causing a crash
                }

                effects.forEach((effect, power) ->
                        player.addEffect(new MobEffectInstance(
                                effect, EFFECT_TIME, power, false, false, true
                        ))
                );
            } else if (repelEnemies && isHostile(entity) && entity instanceof Mob mob) {
                mob.setTarget(null);
                mob.getNavigation().stop();
                mob.setRemainingFireTicks(EFFECT_TIME);
                hostileEffects.forEach(effect -> mob.addEffect(new MobEffectInstance(
                        effect, EFFECT_TIME, 9, false, true, true)
                ));
            }
        }
    }

    public static boolean validateEntry(final Object obj) {
        if (obj instanceof String str) {
            String[] parts = str.split(" ", 2);
            if (parts.length != 2) {
                return false;
            }

            try {
                int level = Integer.parseInt(parts[1]);
                return level >= 1 && level <= 256 && ValidationUtils.isResourceLoc(parts[0]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private static boolean isDebuff(MobEffectInstance instance) {
        return instance != null && instance.getEffect().getCategory() == MobEffectCategory.HARMFUL;
    }

    private static boolean isBoss(LivingEntity entity) {
        // Why is the Warden not a boss? Buddy can two tap you even with full enchanted netherite armor
        return entity != null && (entity.getType().is(Tags.EntityTypes.BOSSES) || entity instanceof Warden);
    }

    private static boolean isHostile(LivingEntity entity) {
        // Early false return if null OR a BOSS OR not a MONSTER
        if (entity == null || isBoss(entity) || entity.getType().getCategory() != MobCategory.MONSTER) {
            return false;
        }

        // Typically monsters aren't tamable but this safety is added in case there happens to be a modded one that is
        return !(entity instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null);
    }

    private static void generateEffectList() {
        effects.clear();
        List<? extends String> entries = CommonCfg.CAMPFIRE_EFFECTS.get();
        for (String entry : entries) {
            String[] values = entry.split(" ", 2);
            if (values.length != 2) {
                LOGGER.warn("Failed to parse effect entry: {}", entry);
                continue;
            }

            int level;
            try {
                level = Integer.parseInt(values[1]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid effect power in entry: {}", entry);
                continue;
            }

            ResourceLocation loc = ResourceLocation.tryParse(values[0]);
            MobEffect effect = (loc == null) ? null : ForgeRegistries.MOB_EFFECTS.getValue(loc);
            if (effect == null) {
                LOGGER.warn("Invalid effect in entry: {}", entry);
                continue;
            }

            // Subtract 1 power as effect amplifiers in MC start at 0 (lv 1) and end at 255 (lv 256)
            if (level >= 1 && level <= 256) {
                effects.put(effect, level - 1);
            } else {
                LOGGER.warn("Effect level out of bounds. Must be in range 1 ~ 256: {}", entry);
            }
        }

        hostileEffects.clear();
        hostileEffects.add(MobEffects.WEAKNESS);
        hostileEffects.add(MobEffects.BLINDNESS);
        hostileEffects.add(MobEffects.MOVEMENT_SLOWDOWN);
    }
}
