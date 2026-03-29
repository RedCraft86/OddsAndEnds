package com.redcraft86.oddsandends.mixin.common;

import com.redcraft86.oddsandends.registries.ModRules;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Unique private final Player one_self = (Player)(Object)this;

    @Inject(method = "resetAttackStrengthTicker", at = @At("HEAD"), cancellable = true)
    public void one_cancelStrengthTicker(CallbackInfo ci) {
        if (one_self.level().getGameRules().getBoolean(ModRules.NO_ATK_COOLDOWN)) {
            ci.cancel();
        }
    }

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    public void one_getMaxAtkStrength(float adjustTicks, CallbackInfoReturnable<Float> cir) {
        if (one_self.level().getGameRules().getBoolean(ModRules.NO_ATK_COOLDOWN)) {
            cir.setReturnValue(1.0f);
        }
    }
}
