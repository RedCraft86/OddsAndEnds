package com.redcraft86.oddsandends.configs;

import com.redcraft86.lanternlib.util.ValidationUtils;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> SPAWN_STRUCTURE;
    public static final ModConfigSpec.IntValue SPAWN_SEARCH_RADIUS;

    static {
        BUILDER.push("spawnStructure");

        SPAWN_STRUCTURE = BUILDER.comment("Set the world spawn point near a specified structure.")
                .comment("Either an ID or a tag (prefixed by #). Empty to disable feature.")
                .define("structure", "#oddsandends:spawn_structure", ValidationUtils::isResourceLocOrTag);

        SPAWN_SEARCH_RADIUS = BUILDER
                .comment("The radius (in chunks) around 0, 0 that should be searched for the structure.")
                .defineInRange("radius", 128, 32, 512);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
