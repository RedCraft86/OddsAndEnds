package com.redcraft86.oddsandends.events;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.client.*;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    static void onScreenOpen(ScreenEvent.Opening event) {
        StartupSound.handle(event.getNewScreen());
    }
}
