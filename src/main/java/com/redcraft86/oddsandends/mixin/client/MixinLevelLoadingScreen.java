package com.redcraft86.oddsandends.mixin.client;

import com.redcraft86.oddsandends.features.SpawnStructure;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.gui.screens.LevelLoadingScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelLoadingScreen.class)
public class MixinLevelLoadingScreen {
    @Unique private static final String TEXT_KEY = "message.oddsandends.structure_locate";

    @Inject(method = "getFormattedProgress", at = @At("RETURN"), cancellable = true)
    private void getProgressText(CallbackInfoReturnable<String> cir) {
        if (SpawnStructure.isLocating()) {
            cir.setReturnValue(I18n.get(TEXT_KEY, SpawnStructure.getLocateTarget()));
        }
    }
}
