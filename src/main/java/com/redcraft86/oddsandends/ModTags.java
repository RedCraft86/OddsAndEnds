package com.redcraft86.oddsandends;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NETHER_PORTAL_FRAME = TagKey.create(Registries.BLOCK,
                ResourceLocation.parse("minecraft:nether_portal_frame"));
    }
}
