package com.redcraft86.oddsandends.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
