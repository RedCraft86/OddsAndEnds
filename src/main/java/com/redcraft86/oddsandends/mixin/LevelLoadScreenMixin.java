package com.redcraft86.oddsandends.mixin;

import com.redcraft86.lanternlib.TransientFlags;
import com.redcraft86.oddsandends.FlagKeys;

import com.redcraft86.oddsandends.common.StructureSpawnPoint;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadScreenMixin {
    @Inject(method = "getFormattedProgress", at = @At("RETURN"), cancellable = true)
    private void getLoadString(CallbackInfoReturnable<Component> cir) {
        if (StructureSpawnPoint.hasActiveStructure()) {
            cir.setReturnValue(Component.translatable("oddsandends.worldload.locate",
                    StructureSpawnPoint.getActiveStructure()));
        } else {
            cir.setReturnValue(Component.translatable(TransientFlags.hasFlag(FlagKeys.GENERATING_WORLD) ?
                    "oddsandends.worldload.generating" : "oddsandends.worldload.loading",
                    cir.getReturnValue()));
        }
    }
}
