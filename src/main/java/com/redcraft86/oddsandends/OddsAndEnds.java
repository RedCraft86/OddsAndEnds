package com.redcraft86.oddsandends;

import com.redcraft86.oddsandends.registries.ModBlocks;
import com.redcraft86.oddsandends.registries.ModItems;
import com.redcraft86.oddsandends.registries.ModRules;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.*;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(OddsAndEnds.MOD_ID)
public final class OddsAndEnds {
    public static final String MOD_ID = "oddsandends";
    public static final Logger LOGGER = LogUtils.getLogger();

    public OddsAndEnds(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        ModRules.registerGameRules();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        ModItems.ITEMS.addCreative(event);
        ModBlocks.BLOCKS.addCreative(event);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
