package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.configs.CommonCfg;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Unique private static final int TICK_INTERVAL = 5;
    @Unique private int tickCounter = 0;

    @Unique private Player thisObj = (Player)(Object)this;

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        if (tickCounter < TICK_INTERVAL) {
            tickCounter++; // Slow tick
            return;
        }
        tickCounter = 0;

        if (!CommonCfg.NO_POISON_REGEN.get()) {
            return;
        }

        MobEffectInstance poison = thisObj.getEffect(MobEffects.POISON);
        MobEffectInstance regen = thisObj.getEffect(MobEffects.REGENERATION);
        if (poison != null && regen != null) {
            final int OVERCOME_TICKS = 20; // Ticks to overcome a stronger effect
            int ampDiff = poison.getAmplifier() - regen.getAmplifier();
            int durDiff = poison.getDuration() - regen.getDuration();

            /* The logic is simple
             * If amplifier are the same:
             *      Longer duration wins, or if the same, cancels out completely
             * If poison was greater:
             *      Regen needs to overcome with OVERCOME_TICKS to cancel out, otherwise poison wins
             * If regen was greater:
             *      Poison needs to overcome with OVERCOME_TICKS to cancel out, otherwise regen wins
             *
             * This logic does not consider the actual difference of amplifier for reduced complexity though it wouldn't
             * be hard to make it consider since I could just multiply the OVERCOME_TICKS with the amplifier difference
             */

            Holder<MobEffect> winner;
            if (ampDiff == 0) {
                winner = durDiff > 0 ? MobEffects.POISON : (durDiff < 0 ? MobEffects.REGENERATION : null);
            } else if (ampDiff > 0) {
                winner = durDiff <= -OVERCOME_TICKS ? null : MobEffects.POISON;
            } else {
                winner = durDiff >= OVERCOME_TICKS ? null : MobEffects.REGENERATION;
            }

            thisObj.removeEffect(MobEffects.POISON);
            thisObj.removeEffect(MobEffects.REGENERATION);
            if (winner != null) {
                thisObj.addEffect(new MobEffectInstance(winner, Math.abs(durDiff), Math.abs(ampDiff)));
            }
        }

    }
}
