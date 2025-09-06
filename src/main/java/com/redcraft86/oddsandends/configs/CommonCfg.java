package com.redcraft86.oddsandends.configs;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> GRIEF_BLACKLIST;

    public static final ModConfigSpec.BooleanValue DIRT_TO_GRASS;
    public static final ModConfigSpec.BooleanValue SNEAKY_GRASS_CHANCE;
    public static final ModConfigSpec.IntValue SHORT_GRASS_CHANCE;
    public static final ModConfigSpec.IntValue TALL_GRASS_CHANCE;
    public static final ModConfigSpec.IntValue RANDOM_FLOWER_CHANCE;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> FLOWER_BLACKLIST;

    public static final ModConfigSpec.IntValue SEARCH_RADIUS;
    public static final ModConfigSpec.ConfigValue<String> STRUCTURE_SPAWNPOINT;
    public static final ModConfigSpec.BooleanValue RANDOM_TAGGED_STRUCTURE;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> STRUCTURE_BLACKLIST;

    static {
        BUILDER.push("entities");

        GRIEF_BLACKLIST = BUILDER.comment("A list of entities blacklisted from griefing the world.")
                .defineListAllowEmpty("griefingBlacklist",
                        List.of("minecraft:enderman", "minecraft:fireball", "minecraft:wither_skull"),
                        () -> "", CommonCfg::validateResource);

        BUILDER.pop();
        BUILDER.push("improvedBoneMeal");

        DIRT_TO_GRASS = BUILDER.comment("Adds the ability to bone meal a block of dirt to grass")
                .define("dirtToGrass", true);

        SNEAKY_GRASS_CHANCE = BUILDER.comment("If the player should be crouched for the grass chances below to be active.")
                .define("sneakyGrassChance", true);

        SHORT_GRASS_CHANCE = BUILDER.comment("Chance of short grass spawning from bonemeal.")
                .defineInRange("shortGrassChance", 50, 0, 100);

        TALL_GRASS_CHANCE = BUILDER.comment("Chance of tall grass spawning from bonemeal.")
                .defineInRange("tallGrassChance", 25, 0, 100);

        RANDOM_FLOWER_CHANCE = BUILDER.comment("Chance of spawning a random flower instead of air in place of grass.")
                .defineInRange("randomFlowerChance", 75, 0, 100);

        FLOWER_BLACKLIST = BUILDER.comment("Flowers that should not spawn from bone meal. (Requires world reload)")
                .defineListAllowEmpty("flowerBlacklist", List.of("minecraft:wither_rose"),
                        () -> "", CommonCfg::validateResource);

        BUILDER.pop();
        BUILDER.push("structureSpawnPoint");

        SEARCH_RADIUS = BUILDER.comment("The radius (in chunks) around 0, 0 that should be searched for the structure.")
                .defineInRange("radius", 128, 32, 512);

        STRUCTURE_SPAWNPOINT = BUILDER.comment("Set the world spawn point near a specified structure.")
                .comment("Either an ID or a tag (prefixed by #). Leave empty to disable.")
                .define("structure", "#minecraft:village", CommonCfg::validateResourceOrTag);

        RANDOM_TAGGED_STRUCTURE = BUILDER.comment("If using a tag, whether the a structure should be randomly picked instead of going in order.")
                .define("randomize", true);

        STRUCTURE_BLACKLIST = BUILDER.comment("If using a tag, a list of structures to ignore.")
                .defineListAllowEmpty("blacklist", List.of("minecraft:village_snowy"),
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

            if (id[0].isBlank() || id[1].isBlank()) {
                return false;
            }

            return ResourceLocation.isValidNamespace(id[0])
                    && ResourceLocation.isValidPath(id[1]);
        }

        return false;
    }

    private static boolean validateResourceOrTag(final Object obj) {
        if (obj instanceof String string) {
            if (string.startsWith("#")) {
                return validateResource(string.substring(1));
            } else {
                return validateResource(obj);
            }
        }

        return false;
    }
}
