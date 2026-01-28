package com.redcraft86.oddsandends.common.registries;

import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.blocks.*;
import com.redcraft86.lanternlib.api.blocks.BlockRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlocks {
    public static final BlockRegister BLOCKS = new BlockRegister(
            OddsAndEnds.MOD_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, ModItems.ITEMS);

    public static final DeferredBlock<Block> RAINBOW_BEACON = BLOCKS.addBlock(
            "rainbow_beacon", RainbowBeaconBlock::new, null, null);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
