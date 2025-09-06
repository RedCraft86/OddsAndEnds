package com.redcraft86.lanterntweaks;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.lanterntweaks.configs.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(LanternTweaks.MOD_ID)
public class LanternTweaks {
    public static final String MOD_ID = "lanterntweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LanternTweaks(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        //NeoForge.EVENT_BUS.register(this); // Add this if using @SubscribeEvent

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }
}