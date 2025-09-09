package com.redcraft86.oddsandends;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OddsAndEnds.MOD_ID)
public class OddsAndEnds {
    public static final String MOD_ID = "oddsandends";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OddsAndEnds(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        OddsAndEndsRules.registerRules();

        context.registerConfig(ModConfig.Type.CLIENT, ClientCfg.SPEC);
        context.registerConfig(ModConfig.Type.COMMON, CommonCfg.SPEC);
    }
}
