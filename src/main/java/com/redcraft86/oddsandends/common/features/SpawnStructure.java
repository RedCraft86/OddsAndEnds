package com.redcraft86.oddsandends.common.features;

import java.util.*;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mojang.datafixers.util.Pair;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class SpawnStructure {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static String locateTarget = null;

    public static String getLocateTarget() { return locateTarget; }
    public static boolean isLocating() { return locateTarget != null && !locateTarget.isBlank(); }

    @SubscribeEvent(receiveCanceled = true)
    static void onCreateSpawn(LevelEvent.CreateSpawnPosition event) {
        if (findStructure(event.getLevel())) {
            event.setCanceled(true);
        }
    }

    public static boolean findStructure(LevelAccessor levelAccessor) {
        if (levelAccessor.isClientSide()) {
            return false;
        }

        String structure = CommonCfg.SPAWN_STRUCTURE.get();
        if (CommonCfg.SPAWN_SEARCH_RADIUS.get() < 25 || structure.isBlank()) {
            LOGGER.warn("Search radius too small to feasibly locate any structures");
            return false;
        }

        if (levelAccessor instanceof ServerLevel level) {
            if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
                LOGGER.warn("World does not allow any structures, skipping structure finder");
                return false;
            }

            locateTarget = structure;
            LOGGER.info("Attempting to locate structure '{}'...", structure);

            Pair<BlockPos, Holder<Structure>> result;
            if (structure.startsWith("#")) {
                result = findStructureByTag(level, structure);
            } else {
                result = findStructureByID(level, structure);
            }

            locateTarget = null;
            if (result == null) {
                LOGGER.warn("Could not find structure, proceeding as normal");
                return false;
            } else {
                level.setDefaultSpawnPos(result.getFirst(), 1.0f);
                LOGGER.info("Spawn Point set! Continuing world generation");
                return true;
            }
        }

        return false;
    }

    private static Pair<BlockPos, Holder<Structure>> findStructureByID(ServerLevel level, String id) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        if (loc == null) {
            LOGGER.error("Structure id {} is invalid", id);
            return null;
        }

        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<Holder.Reference<Structure>> holder = registry.getHolder(ResourceKey.create(Registries.STRUCTURE, loc));
        return holder.map(ref -> getStructure(level, ref)).orElse(null);
    }

    private static Pair<BlockPos, Holder<Structure>> findStructureByTag(ServerLevel level, String tag) {
        ResourceLocation loc = ResourceLocation.tryParse(tag.startsWith("#") ? tag.substring(1) : tag);
        if (loc == null) {
            LOGGER.error("Structure tag {} is invalid", tag);
            return null;
        }

        Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<HolderSet.Named<Structure>> holders = registry.getTag(TagKey.create(Registries.STRUCTURE, loc));
        if (holders.isEmpty()) {
            LOGGER.error("Structure tag {} is empty", loc);
            return null;
        }

        List<Holder<Structure>> structures = new ArrayList<>();
        for (Holder<Structure> holder : holders.get()) {
            Optional<ResourceKey<Structure>> key = holder.unwrapKey();
            if (key.isPresent()) structures.add(holder);
        }

        if (structures.isEmpty()) {
            LOGGER.error("Structure tag {} has no valid structures", loc);
            return null;
        }

        Collections.shuffle(structures, new Random(level.getSeed()));

        for (Holder<Structure> holder : structures) {
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
        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator().findNearestMapStructure(
            level, HolderSet.direct(holder), BlockPos.ZERO, CommonCfg.SPAWN_SEARCH_RADIUS.get(), false);

        if (result != null) {
            BlockPos Pos = result.getFirst();
            LOGGER.info("Structure '{}' found at {}, {}", id, Pos.getX(), Pos.getZ());
            return result;
        }

        LOGGER.info("Structure '{}' could not be found, finding another structure in tag", id);
        return null;
    }
}
