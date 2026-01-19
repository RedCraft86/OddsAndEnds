package com.redcraft86.oddsandends.common.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.features.SpawnStructure;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class ServerEvents {
    @SubscribeEvent(receiveCanceled = true)
    static void onWorldCreate(LevelEvent.CreateSpawnPosition e) {
        if (SpawnStructure.handle(e.getLevel())) {
            e.setCanceled(true);
        }
    }
}
