package com.redcraft86.oddsandends.common;

import com.redcraft86.oddsandends.mixin.accessor.ItemAccessor;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CommonPatches {
    public static void apply() {
        applyMC151457();
    }

    private static void applyMC151457() {
        Item[] items = {
                Items.AXOLOTL_BUCKET,
                Items.COD_BUCKET,
                Items.POWDER_SNOW_BUCKET,
                Items.PUFFERFISH_BUCKET,
                Items.SALMON_BUCKET,
                Items.TADPOLE_BUCKET,
                Items.TROPICAL_FISH_BUCKET
        };

        for (Item item : items) {
            if (!item.hasCraftingRemainingItem(null)) {
                ((ItemAccessor)item).setCraftRemainder(Items.BUCKET);
            }
        }
    }
}
