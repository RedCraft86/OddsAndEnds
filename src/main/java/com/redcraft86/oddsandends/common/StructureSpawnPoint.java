package com.redcraft86.oddsandends.common;

import java.util.*;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mojang.datafixers.util.Pair;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class StructureSpawnPoint {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static String locateTarget = null;
    public static String getActiveStructure() { return locateTarget; }
    public static boolean hasActiveStructure() {
        return locateTarget != null && !locateTarget.isBlank();
    }

    public static boolean handle(LevelAccessor levelAccessor) {
        String structure = CommonCfg.STRUCTURE_SPAWNPOINT.get();
        if (CommonCfg.SEARCH_RADIUS.get() < 5 || structure.isBlank()) {
            return false;
        }

        if (levelAccessor instanceof ServerLevel level) {
            if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
                LOGGER.warn("[Structure Spawn Point] World does not allow any structures, skipping.");
                return false;
            }

            locateTarget = structure;
            Pair<BlockPos, Holder<Structure>> result;
            LOGGER.info("[Structure Spawn Point] Attempting to locate structure {}...", structure);
            if (structure.startsWith("#")) {
                result = findStructureByTag(level, structure.substring(1));
            } else {
                result = findStructureByID(level, structure);
            }

            locateTarget = null;
            if (result == null) {
                LOGGER.warn("[Structure Spawn Point] Could not find structure, proceeding as normal.");
                return false;
            } else {
                level.setDefaultSpawnPos(result.getFirst(), 1.0f);
                LOGGER.info("[Structure Spawn Point] Spawn Point set! Continuing world generation.");
                return true;
            }
        }

        return false;
    }

    private static Pair<BlockPos, Holder<Structure>> findStructureByID(ServerLevel level, String id) {
        ResourceLocation resource = ResourceLocation.parse(id);
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<Holder.Reference<Structure>> structure = registry.getHolder(ResourceKey.create(Registries.STRUCTURE, resource));
        return structure.map(ref -> getStructure(level, ref)).orElse(null);
    }

    private static Pair<BlockPos, Holder<Structure>> findStructureByTag(ServerLevel level, String tag) {
        List<? extends String> blacklist = CommonCfg.STRUCTURE_BLACKLIST.get();
        boolean random = CommonCfg.RANDOM_TAGGED_STRUCTURE.get();

        ResourceLocation resource = ResourceLocation.parse(tag);
        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<HolderSet.Named<Structure>> structures = registry.getTag(TagKey.create(Registries.STRUCTURE, resource));
        if (structures.isEmpty()) {
            LOGGER.error("[Structure Spawn Point] Structure tag is empty!");
            return null;
        }

        List<Holder<Structure>> validStructures = new ArrayList<>();
        for (Holder<Structure> holder : structures.get()) {
            Optional<ResourceKey<Structure>> key = holder.unwrapKey();
            if (key.isPresent() && !blacklist.contains(key.get().location().toString())) {
                validStructures.add(holder);
            }
        }

        if (validStructures.isEmpty()) {
            return null;
        }

        if (random) {
            Random rand = new Random(level.getSeed());
            Collections.shuffle(validStructures, rand);
        }

        for (Holder<Structure> holder : validStructures) {
            Pair<BlockPos, Holder<Structure>> result = getStructure(level, holder);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private static Pair<BlockPos, Holder<Structure>> getStructure(ServerLevel level, Holder<Structure> holder) {
        Optional<ResourceKey<Structure>> key = holder.unwrapKey();
        if (key.isEmpty()) {
            return null;
        }

        ResourceLocation id = key.get().location();
        try {
            Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator().findNearestMapStructure(
                    level, HolderSet.direct(holder), BlockPos.ZERO, CommonCfg.SEARCH_RADIUS.get(), false);
            if (result != null) {
                BlockPos Pos = result.getFirst();
                LOGGER.info("[Structure Spawn Point] Structure '{}' found at {}, {}", id, Pos.getX(), Pos.getZ());
                return result;
            }
        } catch(Exception e) {
            LOGGER.error("[Structure Spawn Point] Skipping structure '{}' because of exception {}", id, e);
            return null;
        }

        return null;
    }
}
