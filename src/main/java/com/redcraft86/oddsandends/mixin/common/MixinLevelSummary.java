package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.configs.CommonCfg;
import net.minecraft.world.level.storage.LevelSummary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSummary.class)
public class MixinLevelSummary {
    @Inject(method = "isExperimental", at = @At("RETURN"), cancellable = true)
    private void one_isExperimental(CallbackInfoReturnable<Boolean> cir) {
        if (CommonCfg.HIDE_EXPERIMENTAL_WARNING.get()) {
            cir.setReturnValue(false);
        }
    }
}
