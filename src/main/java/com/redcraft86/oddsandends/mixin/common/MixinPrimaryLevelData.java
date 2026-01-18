package com.redcraft86.oddsandends.mixin.common;

import net.minecraft.world.level.storage.PrimaryLevelData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public class MixinPrimaryLevelData {
    @Inject(method = "hasConfirmedExperimentalWarning", at = @At("HEAD"), cancellable = true)
    private void checkExperimentalWarning(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true); // TODO: config
    }
}
