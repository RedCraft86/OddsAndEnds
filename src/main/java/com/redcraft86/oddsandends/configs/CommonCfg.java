package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.oddsandends.common.*;
import com.redcraft86.lanternlib.util.LanternUtils;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue CRYING_PORTALS;

    public static final ModConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ModConfigSpec.BooleanValue MIX_ENCHANTMENTS;

    public static final ModConfigSpec.BooleanValue NO_PET_ATTACK;
    public static final ModConfigSpec.BooleanValue NO_VILLAGER_ATTACK;
    public static final ModConfigSpec.BooleanValue NO_TEMPT_COOLDOWN;
    public static final ModConfigSpec.BooleanValue INFINITE_VILLAGERS;

    public static final ModConfigSpec.IntValue SOULFIRE_EFFECT_RANGE;
    public static final ModConfigSpec.BooleanValue SOULFIRE_CLEAR_HARM;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SOULFIRE_EFFECTS;

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
        CRYING_PORTALS = BUILDER.comment("Allows Crying Obsidian to be used for Nether Portals.")
                .define("cryingPortals", true);

        BUILDER.push("enchantments");

        TRUE_INFINITY = BUILDER.comment("Makes the infinity enchantment on bows not need a single arrow.")
                .define("trueInfinity", true);

        MIX_ENCHANTMENTS = BUILDER.comment("Allows you to merge any enchantment together, even if they are incompatible.")
                .define("mixEnchants", true);

        BUILDER.pop();
        BUILDER.push("entities");

        NO_PET_ATTACK = BUILDER.comment("Prevents the player from accidentally attacking their pets.")
                .define("noPetAttack", true);

        NO_VILLAGER_ATTACK = BUILDER.comment("Prevents the player from accidentally attacking villagers unless holding a tool.")
                .define("noVillagerAttack", true);

        NO_TEMPT_COOLDOWN = BUILDER.comment("Makes mobs that can be tempted with food items never enter a tempt cooldown.")
                .define("noTemptCooldown", true);

        INFINITE_VILLAGERS = BUILDER.comment("Makes villager trades never run out of stock.")
                .define("infiniteVillagers", true);

        BUILDER.pop();
        BUILDER.push("betterSoulfires");

        SOULFIRE_EFFECT_RANGE = BUILDER.comment("The range around the soul campfire in which players will receive effects. Set to 0 to disable feature.")
                .defineInRange("soulfireRange", 3, 0, 10);

        SOULFIRE_CLEAR_HARM = BUILDER.comment("If the soul campfire should clear harmful effects.")
                .define("soulfireNoHarm", true);

        SOULFIRE_EFFECTS = BUILDER.comment("Effects to give when near soul campfires. (Requires world reload)")
                .comment("Format: \"effect_id level\" (Level Range: 1 ~ 256)")
                .defineListAllowEmpty("soulfireEffects",
                        List.of("minecraft:regeneration 1", "minecraft:saturation 1"),
                        () -> "", SoulCampfireEffects::validateEntry);

        BUILDER.pop();
        BUILDER.push("improvedBoneMeal");

        DIRT_TO_GRASS = BUILDER.comment("Adds the ability to bone meal a block of dirt to grass.")
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
                        () -> "", LanternUtils::validateResourceOnly);

        BUILDER.pop();
        BUILDER.push("structureSpawnPoint");

        SEARCH_RADIUS = BUILDER.comment("The radius (in chunks) around 0, 0 that should be searched for the structure.")
                .defineInRange("radius", 128, 32, 512);

        STRUCTURE_SPAWNPOINT = BUILDER.comment("Set the world spawn point near a specified structure.")
                .comment("Either an ID or a tag (prefixed by #). Leave empty to disable.")
                .define("structure", "#minecraft:village", LanternUtils::validateResourceOrTag);

        RANDOM_TAGGED_STRUCTURE = BUILDER.comment("If using a tag, whether the structure should be randomly picked instead of going in order.")
                .define("randomize", true);

        STRUCTURE_BLACKLIST = BUILDER.comment("If using a tag, a list of structures to ignore.")
                .defineListAllowEmpty("blacklist", List.of("minecraft:village_snowy"),
                        () -> "", LanternUtils::validateResourceOnly);

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
