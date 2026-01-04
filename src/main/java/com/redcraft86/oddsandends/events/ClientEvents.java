package com.redcraft86.oddsandends.events;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.FlagKeys;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.client.StartupSound;
import com.redcraft86.lanternlib.TransientFlags;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = OddsAndEnds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    static void onScreenOpen(ScreenEvent.Opening e) {
        TransientFlags.removeFlag(FlagKeys.GENERATING_WORLD);
        StartupSound.handle(e.getNewScreen());
    }
}
