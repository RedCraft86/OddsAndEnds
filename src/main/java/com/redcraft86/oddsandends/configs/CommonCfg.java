package com.redcraft86.oddsandends.configs;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> GRIEF_BLACKLIST;

    static {
        BUILDER.push("entities");

        GRIEF_BLACKLIST = BUILDER.comment("A list of entities blacklisted from griefing the world.")
                .defineListAllowEmpty("griefingBlacklist",
                        List.of("minecraft:enderman", "minecraft:fireball", "minecraft:wither_skull"),
                        () -> "", CommonCfg::validateResource);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }

    private static boolean validateResource(final Object obj) {
        if (obj instanceof String string) {
            String[] id = string.split(":", 2);
            if (id.length != 2) {
                return false;
            }

            return ResourceLocation.isValidNamespace(id[0])
                    && ResourceLocation.isValidPath(id[1]);
        }

        return false;
    }
}
