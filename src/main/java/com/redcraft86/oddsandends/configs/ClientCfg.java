package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.oddsandends.features.StartupSound;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> STARTUP_SOUNDS;

    static {
        STARTUP_SOUNDS = BUILDER.comment("List sounds to choose from when playing a sound at startup.")
                .comment("Empty to disable. Format entries as \"sound_id volume\"")
                .defineListAllowEmpty("startupSounds",
                    List.of("minecraft:entity.experience_orb.pickup 0.7", "minecraft:entity.player.levelup 0.3"),
                    () -> "", StartupSound::validateEntry
                );
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
