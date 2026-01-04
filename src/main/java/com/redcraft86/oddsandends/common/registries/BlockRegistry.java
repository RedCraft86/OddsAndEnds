package com.redcraft86.oddsandends.common.registries;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.blocks.*;
import com.redcraft86.lanternlib.common.registry.BlockRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;

import net.neoforged.neoforge.registries.DeferredBlock;

public class BlockRegistry {
    public static final BlockRegister BLOCKS = new BlockRegister(OddsAndEnds.MOD_ID, ItemRegistry.ITEMS);

    public static final DeferredBlock<Block> RAINBOW_BEACON = BLOCKS.addBlock("rainbow_beacon",
            RainbowBeaconBlock::new, CreativeModeTabs.FUNCTIONAL_BLOCKS);
}