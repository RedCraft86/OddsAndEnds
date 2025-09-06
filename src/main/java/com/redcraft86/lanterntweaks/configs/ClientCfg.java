package com.redcraft86.lanterntweaks.configs;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue EXAMPLE_BOOL;
    public static final ModConfigSpec.IntValue EXAMPLE_INT;
    public static final ModConfigSpec.ConfigValue<String> EXAMPLE_STRING;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> EXAMPLE_LIST;

    static {
        EXAMPLE_BOOL = BUILDER.comment("Client Example Boolean")
                .define("exampleBool", true);

        EXAMPLE_INT = BUILDER.comment("Client Example Integer")
                .defineInRange("exampleInt", 42, 0, Integer.MAX_VALUE);

        BUILDER.push("ClientCategoryTest");

        EXAMPLE_STRING = BUILDER.comment("Client Example String")
                .define("exampleString", "Foo");

        EXAMPLE_LIST = BUILDER.comment("Client Example List")
                .defineListAllowEmpty("exampleList", List.of("client test"), () -> "", ClientCfg::validateEntry);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateEntry(final Object obj) {
        return obj instanceof String;
    }
}
