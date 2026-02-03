package com.redcraft86.oddsandends;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.*;
import com.redcraft86.oddsandends.registries.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

@Mod(OddsAndEnds.MOD_ID)
public class OddsAndEnds {
    public static final String MOD_ID = "oddsandends";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OddsAndEnds(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModRules.registerGameRules();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        context.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        ModItems.ITEMS.addCreative(event);
        ModBlocks.BLOCKS.addCreative(event);
    }
}