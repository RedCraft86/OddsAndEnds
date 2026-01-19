package com.redcraft86.oddsandends.client.features;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mojang.datafixers.util.Pair;

import com.redcraft86.oddsandends.configs.ClientCfg;
import com.redcraft86.lanternlib.util.ValidationUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class StartupSound {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean bPlayed = false;

    public static void handle(Screen screen) {
        if (bPlayed || !(screen instanceof TitleScreen)) {
            return;
        }

        bPlayed = true;
        Pair<SoundEvent, Float> sound = getRandomSound(new ArrayList<>(ClientCfg.STARTUP_SOUNDS.get()));
        if (sound != null) {
            Minecraft.getInstance().getSoundManager().playDelayed(SimpleSoundInstance.forUI(
                    sound.getFirst(), 1, sound.getSecond()), 50);
        }
    }

    public static boolean validateEntry(final Object obj) {
        if (obj instanceof String str) {
            String[] parts = str.split(" ", 2);
            if (parts.length != 2) {
                return false;
            }

            try {
                Float.parseFloat(parts[1]);
                return ValidationUtils.isResourceLoc(parts[0]);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    private static Pair<SoundEvent, Float> getRandomSound(List<? extends String> entries) {
        if (entries == null || entries.isEmpty()) {
            return null;
        }

        int idx = 0;
        if (entries.size() > 1) {
            idx = RANDOM.nextInt(entries.size());
        }

        String entry = entries.remove(idx);

        String[] values = entry.split(" ", 2);
        if (values.length != 2) {
            LOGGER.warn("Failed to parse startup sound: {}", entry);
            return getRandomSound(entries);
        }

        float volume;
        try {
            volume = Float.parseFloat(values[1]);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid startup sound volume in entry: {}", entry);
            return getRandomSound(entries);
        }

        ResourceLocation location = ResourceLocation.tryParse(values[0]);
        SoundEvent sound = (location == null) ? null : BuiltInRegistries.SOUND_EVENT.get(location);
        if (sound == null) {
            LOGGER.warn("Invalid startup sound in entry: {}", entry);
            return getRandomSound(entries);
        }

        if (volume > 0.05f) {
            return Pair.of(sound, volume);
        } else {
            return getRandomSound(entries);
        }
    }
}
