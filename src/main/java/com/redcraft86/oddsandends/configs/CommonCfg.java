package com.redcraft86.oddsandends.configs;

import java.util.List;

import com.redcraft86.lanternlib.utils.ValidationUtils;
import com.redcraft86.oddsandends.features.CozyCampfire;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonCfg {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue TRUE_INFINITY;
    public static final ForgeConfigSpec.BooleanValue MIX_ENCHANTMENTS;

    public static final ForgeConfigSpec.BooleanValue NO_FRIENDLY_FIRE;
    public static final ForgeConfigSpec.BooleanValue NO_VILLAGER_HIT;
    public static final ForgeConfigSpec.BooleanValue NO_TEMPT_COOLDOWN;
    public static final ForgeConfigSpec.BooleanValue INFINITE_TRADES;
    public static final ForgeConfigSpec.BooleanValue PLAYER_FALL_DAMPEN;

    public static final ForgeConfigSpec.BooleanValue HIDE_EXPERIMENTAL_WARNING;

    public static final ForgeConfigSpec.ConfigValue<String> SPAWN_STRUCTURE;
    public static final ForgeConfigSpec.IntValue SPAWN_SEARCH_RADIUS;

    public static final ForgeConfigSpec.BooleanValue SHAPELESS_NETHER_PORTALS;
    public static final ForgeConfigSpec.BooleanValue BONEMEAL_DIRT_TO_GRASS;

    public static final ForgeConfigSpec.IntValue CAMPFIRE_RANGE;
    public static final ForgeConfigSpec.EnumValue<CozyCampfire.CampfireType> CAMPFIRE_TYPE;
    public static final ForgeConfigSpec.BooleanValue CAMPFIRE_REPEL_HOSTILES;
    public static final ForgeConfigSpec.BooleanValue CAMPFIRE_CLEAR_DEBUFFS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CAMPFIRE_EFFECTS;

    static {
        ///---ENCHANTMENTS---///
        BUILDER.push("enchantments");

        TRUE_INFINITY = BUILDER.comment("Makes infinity on bows work without needing at least one arrow.")
                .define("trueInfinity", true);

        MIX_ENCHANTMENTS = BUILDER.comment("Allows mixing of any enchantment no matter their compatibility.")
                .comment("Note: This may lead to unexpected behavior if used incorrectly.")
                .define("mixEnchantments", true);

        BUILDER.pop();
        ///~~~ENCHANTMENTS~~~///

        ///---ENTITIES---///
        BUILDER.push("entities");

        NO_FRIENDLY_FIRE = BUILDER.comment("Prevents players from hitting tamed mobs.")
                .define("noFriendlyFire", true);

        NO_VILLAGER_HIT = BUILDER.comment("Prevents players from hitting villagers with an empty hand.")
                .define("noVillagerHit", true);

        NO_TEMPT_COOLDOWN = BUILDER.comment("Prevents breedable mobs from entering a cooldown after losing interest in food items.")
                .define("noTemptCooldown", true);

        INFINITE_TRADES = BUILDER.comment("Prevents villager trades from ever running out of stock.")
                .define("infiniteTrades", true);

        PLAYER_FALL_DAMPEN = BUILDER.comment("Reduces player fall damage by 80% on Hay Bales and Leaves.")
                .comment("Tag a block with 'oddsandends:dampen_fall_damage' to add more blocks.")
                .define("dampenPlayerFall", true);

        BUILDER.pop();
        ///~~~ENTITIES~~~///

        ///---WORLD---///
        BUILDER.push("world");

        HIDE_EXPERIMENTAL_WARNING = BUILDER.comment("Hides the experimental warning when creating a modded world.")
                .define("hideExperimentalWarning", true);

        ///---WORLD/SpawnStructure---///
        BUILDER.comment("Locates specified structure(s) within a radius and sets the world spawn at or near them.");
        BUILDER.push("spawnStructure");

        SPAWN_STRUCTURE = BUILDER.comment("The structure to find and set the spawn at.")
                .comment("Either an ID or a tag (prefixed by #). Empty to disable feature.")
                .define("structure", "#oddsandends:spawn_structure",
                        ValidationUtils::isResourceLocOrTag);

        SPAWN_SEARCH_RADIUS = BUILDER.comment("The radius (in chunks) around 0, 0 that should be searched.")
                .defineInRange("radius", 128, 32, 512);

        BUILDER.pop();
        ///~~~WORLD/SpawnStructure~~~///

        BUILDER.pop();
        ///~~~WORLD~~~///

        ///---MISC---///
        BUILDER.push("misc");

        SHAPELESS_NETHER_PORTALS = BUILDER.comment("Allows nether portals to be built in any shape.")
                .comment("Yes, even a 1x1 portal, or a circular portal. Bounding square limit is 32x32.")
                .define("shapelessNetherPortals", true);

        BONEMEAL_DIRT_TO_GRASS = BUILDER.comment("Whether dirt blocks can be converted to grass blocks with bonemeal.")
                .define("dirtToGrass", true);

        ///---MISC/CozyCampfire---///
        BUILDER.comment("Makes campfires give effects to nearby players.");
        BUILDER.push("cozyCampfires");

        CAMPFIRE_RANGE = BUILDER.comment("The radius (in blocks) around the campfire to give effects. Set 0 to disable.")
                .defineInRange("range", 5, 0, 16);

        CAMPFIRE_TYPE = BUILDER.comment("The type of campfire that will provide the player with effects.")
                .comment("REGULAR: Normal Campfires\nSOULFIRE: Soul Campfires\nANY: Any campfire block that uses CampfireBlockEntity")
                .defineEnum("campfireType", CozyCampfire.CampfireType.SOULFIRE);

        CAMPFIRE_REPEL_HOSTILES = BUILDER.comment("Whether hostiles should be slowed, weakened, and burned around campfires.")
                .comment("NOTE: This option does not affect bosses, they will not be repelled.")
                .define("repelHostiles", true);

        CAMPFIRE_CLEAR_DEBUFFS = BUILDER.comment("Whether debuffs should be cleared around campfires.")
                .define("clearDebuffs", true);

        CAMPFIRE_EFFECTS = BUILDER.comment("Effects to give around campfires. (Requires world reload)")
                .comment("Can be empty. Format as \"effect_id level\" (Level Range: 1 ~ 256)")
                .defineListAllowEmpty("grantEffects",
                        List.of("minecraft:regeneration 1", "minecraft:saturation 1"),
                        CozyCampfire::validateEntry);

        BUILDER.pop();
        ///~~~MISC/CozyCampfire~~~///

        BUILDER.pop();
        ///~~~MISC~~~///
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean isLoaded() { return SPEC.isLoaded(); }
}