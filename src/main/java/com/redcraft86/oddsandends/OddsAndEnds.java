package com.redcraft86.oddsandends;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.*;
import com.redcraft86.oddsandends.common.CommonPatches;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(OddsAndEnds.MOD_ID)
public class OddsAndEnds {
    public static final String MOD_ID = "oddsandends";
    public static final Logger LOGGER = LogUtils.getLogger();

    public OddsAndEnds(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        OddsAndEndsRules.registerRules();
        CommonPatches.apply();

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }
}