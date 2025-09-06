package com.redcraft86.oddsandends.client;

import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.configs.ClientCfg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class StartupSound {
    private static boolean hasPlayed = false;
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handle(Screen screen) {
        if (!hasPlayed && screen instanceof TitleScreen) {
            hasPlayed = true;

            if (!ClientCfg.isLoaded()) {
                LOGGER.warn("[Odds And Ends] Failed to play startup sound: Client Config has not been loaded!");
                return;
            }

            List<? extends String> entries = ClientCfg.STARTUP_SOUNDS.get();
            if (entries.isEmpty()) {
                return;
            }

            String entry = entries.get(RANDOM.nextInt(entries.size()));
            String[] values = entry.split(" ", 2);
            if (values.length != 2) {
                LOGGER.warn("[Odds And Ends] Failed to parse startup sound: {}", entry);
                return;
            }

            ResourceLocation resource = ResourceLocation.tryParse(values[0]);
            float volume = Float.parseFloat(values[1]);
            if (resource == null || volume <= 0.05f) {
                LOGGER.warn("[Odds And Ends] Invalid startup sound entry: {}", entry);
                return;
            }

            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(resource);
            if (soundEvent != null) {
                Minecraft.getInstance().getSoundManager().playDelayed(
                        SimpleSoundInstance.forUI(soundEvent, 1, volume), 50);
            } else {
                LOGGER.warn("[Odds And Ends] Failed to play startup sound: {} does not exist!", resource);
            }
        }
    }

    public static boolean validateEntry(final Object obj) {
        if (obj instanceof String string) {
            String[] result = string.split(" ", 2);
            if (result.length != 2) {
                return false;
            }

            try {
                Float.parseFloat(result[1]);
            } catch (NumberFormatException e) {
                return false;
            }

            String[] id = result[0].split(":", 2);
            if (id.length != 2) {
                return false;
            }

            return ResourceLocation.isValidNamespace(id[0])
                    && ResourceLocation.isValidPath(id[1]);
        }

        return false;
    }
}
