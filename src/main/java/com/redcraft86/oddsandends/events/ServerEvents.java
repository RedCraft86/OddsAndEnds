package com.redcraft86.oddsandends.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class ServerEvents {
    @SubscribeEvent
    static void onMobGrief(EntityMobGriefingEvent e) {
        Entity entity = e.getEntity();
        if (!CommonCfg.isLoaded() || entity.level().isClientSide()) {
            return;
        }

        ResourceLocation resource = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        e.setCanGrief(!CommonCfg.GRIEF_BLACKLIST.get().contains(resource.toString()));
    }
}
