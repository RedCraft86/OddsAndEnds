package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.FlagKeys;
import com.redcraft86.oddsandends.common.StructureSpawnPoint;
import com.redcraft86.lanternlib.TransientFlags;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.gui.screens.LevelLoadingScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin {
    @Inject(method = "getFormattedProgress", at = @At("RETURN"), cancellable = true)
    private void getLoadString(CallbackInfoReturnable<String> cir) {
        if (StructureSpawnPoint.hasActiveStructure()) {
            cir.setReturnValue(I18n.get("oddsandends.worldload.locate",
                    StructureSpawnPoint.getActiveStructure()));
        } else {
            cir.setReturnValue(I18n.get(TransientFlags.hasFlag(FlagKeys.GENERATING_WORLD) ?
                    "oddsandends.worldload.generating" : "oddsandends.worldload.loading",
                    cir.getReturnValue()));
        }
    }
}
