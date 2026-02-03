package com.redcraft86.oddsandends.registries;

import com.redcraft86.oddsandends.blocks.*;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.lanternlib.api.blocks.BlockRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;

import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final BlockRegister BLOCKS = new BlockRegister(
            OddsAndEnds.MOD_ID, CreativeModeTabs.FUNCTIONAL_BLOCKS, ModItems.ITEMS);

    public static final RegistryObject<Block> RAINBOW_BEACON = BLOCKS.addBlock(
            "rainbow_beacon", RainbowBeaconBlock::new, null, null);
}
