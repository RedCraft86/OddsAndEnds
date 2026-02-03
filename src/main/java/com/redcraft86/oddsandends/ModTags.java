package com.redcraft86.oddsandends;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class ModTags {
    public static final class Blocks {
        public static final TagKey<Block> DAMPEN_FALL_DAMAGE = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(OddsAndEnds.MOD_ID, "dampen_fall_damage"));

        public static final TagKey<Block> NETHER_PORTAL_FRAME = TagKey.create(Registries.BLOCK,
                ResourceLocation.parse("minecraft:nether_portal_frame"));
    }
    public static final class Structures {
        public static final TagKey<Structure> SPAWN_STRUCTURE = TagKey.create(Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(OddsAndEnds.MOD_ID, "spawn_structure"));
    }
}
