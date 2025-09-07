package com.redcraft86.oddsandends.mixin;

import com.redcraft86.oddsandends.configs.ClientCfg;
import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Unique private static final int TICK_INTERVAL = 5;
    @Unique private int tickCounter = 0;

    @Inject(method = "createTitle", at = @At("RETURN"), cancellable = true)
    private void onCreateTitle(CallbackInfoReturnable<String> cir) {
        if (!ClientCfg.isLoaded()) {
            return;
        }

        // Not complex enough to require a builder/buffer
        String result = cir.getReturnValue();

        String customTitle = ClientCfg.CUSTOM_TITLE.get();
        if (!customTitle.isBlank() && !customTitle.equalsIgnoreCase("default")) {
            result = customTitle;
        }

        if (ClientCfg.MEMORY_USAGE_TITLE.get()) {
            Runtime runtime = Runtime.getRuntime();
            long totalMem = runtime.totalMemory();
            long usedMem = (totalMem - runtime.freeMemory()) / (1024 * 1024);
            result += String.format(" | Memory: %,d MB / %,d MB (%,d MB Allocated)",
                    usedMem, runtime.maxMemory() / (1024 * 1024), totalMem / (1024 * 1024));
        }

        cir.setReturnValue(result);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (tickCounter < TICK_INTERVAL) {
            tickCounter++; // Slow tick
            return;
        }
        tickCounter = 0;

        ((Minecraft)(Object)this).updateTitle();
    }
}
