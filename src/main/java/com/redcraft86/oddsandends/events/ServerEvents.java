package com.redcraft86.oddsandends.events;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.lanternlib.TransientFlags;
import com.redcraft86.oddsandends.FlagKeys;
import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.*;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class ServerEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent(receiveCanceled = true)
    static void onWorldCreate(LevelEvent.CreateSpawnPosition e) {
        LevelAccessor levelAccessor = e.getLevel();
        if (!levelAccessor.isClientSide()) {
            return;
        }

        TransientFlags.addFlag(FlagKeys.GENERATING_WORLD);
        if (StructureSpawnPoint.handle(levelAccessor)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onMobGrief(EntityMobGriefingEvent e) {
        Entity entity = e.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        ResourceLocation resource = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        e.setCanGrief(!CommonCfg.GRIEF_BLACKLIST.get().contains(resource.toString()));
    }
}
