package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.oddsandends.client.StartupSound;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> STARTUP_SOUNDS;

    public static final ModConfigSpec.BooleanValue MEMORY_USAGE_TITLE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_TITLE;

    static {
        STARTUP_SOUNDS = BUILDER.comment("A random sound from this list will be played when first entering the title screen.")
                .comment("Leave empty to disable. Format: \"namespace:id volume\"")
                .defineListAllowEmpty("startupSounds",
                        List.of("minecraft:entity.experience_orb.pickup 0.7", "minecraft:entity.player.levelup 0.3"),
                                () -> "", StartupSound::validateEntry);

        BUILDER.push("TitleBar");

        MEMORY_USAGE_TITLE = BUILDER.comment("Show memory usage, total, and allocation in title bar.")
                .define("memoryTitle", true);

        CUSTOM_TITLE = BUILDER.comment("Show a custom string in the title bar in place of the default text.")
                .define("customTitle", "DEFAULT");

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
