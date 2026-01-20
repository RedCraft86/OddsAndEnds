package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.lanternlib.configs.ValidationUtils;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonCfg {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ModConfigSpec.BooleanValue MIX_ENCHANTMENTS;

    public static final ModConfigSpec.BooleanValue NO_FRIENDLY_FIRE;
    public static final ModConfigSpec.BooleanValue NO_VILLAGER_HIT;
    public static final ModConfigSpec.BooleanValue NO_TEMPT_COOLDOWN;
    public static final ModConfigSpec.BooleanValue INFINITE_TRADES;

    public static final ModConfigSpec.BooleanValue HIDE_EXPERIMENTAL_WARNING;

    public static final ModConfigSpec.ConfigValue<String> SPAWN_STRUCTURE;
    public static final ModConfigSpec.IntValue SPAWN_SEARCH_RADIUS;

    public static final ModConfigSpec.BooleanValue SHAPELESS_NETHER_PORTALS;
    public static final ModConfigSpec.BooleanValue BONEMEAL_DIRT_TO_GRASS;
    public static final ModConfigSpec.IntValue CAMPFIRE_RANGE;
    public static final ModConfigSpec.BooleanValue CAMPFIRE_SOULFIRE;
    public static final ModConfigSpec.BooleanValue CAMPFIRE_CLEAR_DEBUFFS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CAMPFIRE_EFFECTS;

    static {
        BUILDER.push("enchantments");

        TRUE_INFINITY = BUILDER.comment("Makes infinity on bows work without needing at least one arrow.")
                .define("trueInfinity", true);

        MIX_ENCHANTMENTS = BUILDER.comment("Allows mixing of any enchantment no matter their compatibility.")
                .comment("Note: This may lead to unexpected behavior if used incorrectly.")
                .define("mixEnchantments", true);

        BUILDER.pop();
        BUILDER.push("entities");

        NO_FRIENDLY_FIRE = BUILDER.comment("Prevents players from hitting tamed mobs.")
                .define("noFriendlyFire", true);

        NO_VILLAGER_HIT = BUILDER.comment("Prevents players from hitting villagers with an empty hand.")
                .define("noVillagerHit", true);

        NO_TEMPT_COOLDOWN = BUILDER.comment("Prevents breedable mobs from losing interest and entering cooldown in food items.")
                .define("noTemptCooldown", true);

        INFINITE_TRADES = BUILDER.comment("Prevents villager trades from ever running out of stock.")
                .define("infiniteTrades", true);

        BUILDER.pop();
        BUILDER.push("world");

        HIDE_EXPERIMENTAL_WARNING = BUILDER.comment("Hides the experimental warning when creating a modded world.")
                .define("hideExperimentalWarning", true);

        BUILDER.comment("Locates specified structure(s) within a radius and sets the world spawn at or near them.");
        BUILDER.push("spawnStructure");
        SPAWN_STRUCTURE = BUILDER.comment("The structure to find and set the spawn at.")
                .comment("Either an ID or a tag (prefixed by #). Empty to disable feature.")
                .define("structure", "#oddsandends:spawn_structure",
                        ValidationUtils::isResourceLocOrTag);

        SPAWN_SEARCH_RADIUS = BUILDER.comment("The radius (in chunks) around 0, 0 that should be searched.")
                .defineInRange("radius", 128, 32, 512);
        BUILDER.pop();

        BUILDER.pop();
        BUILDER.push("misc");

        SHAPELESS_NETHER_PORTALS = BUILDER.comment("Allows nether portals to be built in any shape.")
                .comment("Yes, even a 1x1 portal, or a circular portal. Bounding square limit is 32x32.")
                .define("shapelessNetherPortals", true);

        BONEMEAL_DIRT_TO_GRASS = BUILDER.comment("Whether dirt blocks can be converted to grass blocks with bonemeal.")
                .define("dirtToGrass", true);

        BUILDER.comment("Makes campfires give effects to nearby players");
        BUILDER.push("campfire");
        CAMPFIRE_RANGE = BUILDER.comment("The radius (in blocks) around the campfire to give effects. Set 0 to disable.")
                .defineInRange("range", 3, 0, 8);

        CAMPFIRE_SOULFIRE = BUILDER.comment("Whether to use a Soul Campfire instead of a Campfire for this feature.")
                .define("useSoulfire", true);

        CAMPFIRE_CLEAR_DEBUFFS = BUILDER.comment("Whether debuffs should be cleared around campfires.")
                .define("clearDebuffs", true);

        CAMPFIRE_EFFECTS = BUILDER.comment("Positive effects to give around campfires. (Requires world reload)")
                .comment("Can be empty. Format as \"effect_id level\" (Level Range: 1 ~ 256)")
                .defineListAllowEmpty("grantEffects", 
                        List.of("minecraft:regeneration 1", "minecraft:saturation 1"),
                        () -> "", ValidationUtils::isResourceLocOrTag);
        BUILDER.pop();

        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}
