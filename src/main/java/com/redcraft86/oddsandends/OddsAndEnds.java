package com.redcraft86.oddsandends;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.*;
import com.redcraft86.oddsandends.common.registries.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OddsAndEnds.MOD_ID)
public class OddsAndEnds {
    public static final String MOD_ID = "oddsandends";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OddsAndEnds(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        OddsAndEndsRules.registerRules();
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        context.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        ItemRegistry.ITEMS.addCreative(event);
        BlockRegistry.BLOCKS.addCreative(event);
    }
}
