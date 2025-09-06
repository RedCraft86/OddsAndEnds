package com.redcraft86.oddsandends.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MEMORY_USAGE_TITLE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_TITLE;

    static {
        BUILDER.push("TitleBar");

        MEMORY_USAGE_TITLE = BUILDER.comment("Show memory usage, total, and allocation in title bar.")
                .define("memoryTitle", true);

        CUSTOM_TITLE = BUILDER.comment("Show a custom string in the title bar instead of \"Minecraft NeoForge*...\"")
                .define("customTitle", "DEFAULT");

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
