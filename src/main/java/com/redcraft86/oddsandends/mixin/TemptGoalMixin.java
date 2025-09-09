package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.entity.ai.goal.TemptGoal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {
    @Shadow private int calmDown;

    @Inject(method = "stop", at = @At("TAIL"))
    private void onStop(CallbackInfo ci) {
        if (CommonCfg.NO_TEMPT_COOLDOWN.get()) {
            calmDown = 0;
        }
    }
}