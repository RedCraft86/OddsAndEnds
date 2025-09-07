package com.redcraft86.oddsandends.mixin;

import java.util.Iterator;

import com.redcraft86.oddsandends.configs.ClientCfg;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Redirect(method = "getTooltipFromContainerItem",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/Iterator;hasNext()Z"
            )
    )
    private boolean redirectHasNext(Iterator<CreativeModeTab> iterator) {
        return !ClientCfg.HIDE_CREATIVE_TAB_TOOLTIPS.get() && iterator.hasNext();
    }
}
