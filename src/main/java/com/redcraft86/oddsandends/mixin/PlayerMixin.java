package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.OddsAndEndsRules;

import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Unique private final Player thisObj = (Player)(Object)this;

    @Inject(method = "resetAttackStrengthTicker", at = @At("HEAD"), cancellable = true)
    public void cancelStrengthTicker(CallbackInfo ci) {
        if (thisObj.level().getGameRules().getBoolean(OddsAndEndsRules.NO_ATK_COOLDOWN)) {
            ci.cancel();
        }
    }

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    public void getMaxAtkStrength(float adjustTicks, CallbackInfoReturnable<Float> cir) {
        if (thisObj.level().getGameRules().getBoolean(OddsAndEndsRules.NO_ATK_COOLDOWN)) {
            cir.setReturnValue(1.0f);
        }
    }
}
