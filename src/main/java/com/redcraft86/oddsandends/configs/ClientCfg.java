package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.oddsandends.client.*;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientCfg {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STARTUP_SOUNDS;

    static {
        STARTUP_SOUNDS = BUILDER.comment("A random sound from this list will be played when first entering the title screen.")
                .comment("Leave empty to disable. Format: \"sound_id volume\"")
                .defineListAllowEmpty("startupSounds",
                        List.of("minecraft:entity.experience_orb.pickup 0.7",
                                "minecraft:entity.player.levelup 0.3"
                        ),
                        StartupSound::validateEntry);
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
