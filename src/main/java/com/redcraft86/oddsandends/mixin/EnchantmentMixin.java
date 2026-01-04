package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.item.enchantment.Enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(method = "isCompatibleWith", at = @At("HEAD"), cancellable = true)
    private void checkCompatible(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        if (CommonCfg.MIX_ENCHANTMENTS.get()) {
            cir.setReturnValue(true);
        }
    }
}