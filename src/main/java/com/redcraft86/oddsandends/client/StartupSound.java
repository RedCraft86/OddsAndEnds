package com.redcraft86.oddsandends.client;

import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.mojang.datafixers.util.Pair;

import com.redcraft86.oddsandends.configs.ClientCfg;
import com.redcraft86.oddsandends.OddsAndEnds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class StartupSound {
    private static boolean hasPlayed = false;
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    static void onScreenOpen(ScreenEvent.Opening event) {
        if (!hasPlayed && event.getNewScreen() instanceof TitleScreen) {
            hasPlayed = true;

            Pair<ResourceLocation, Float> sound = getRandomSound();
            if (sound == null) {
                return;
            }

            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(sound.getFirst());
            if (soundEvent != null) {
                Minecraft.getInstance().getSoundManager().playDelayed(
                        SimpleSoundInstance.forUI(soundEvent, 1, sound.getSecond()), 50);
            } else {
                LOGGER.warn("[Odds And Ends] Failed to play startup sound: {} does not exist!", sound.getFirst());
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

    private static Pair<ResourceLocation, Float> getRandomSound() {
        if (!ClientCfg.isLoaded()) {
            LOGGER.warn("[Odds And Ends] Failed to play startup sound: Client Config has not been loaded!");
            return null;
        }

        List<? extends String> entries = ClientCfg.STARTUP_SOUNDS.get();
        if (entries.isEmpty()) {
            return null;
        }

        String entry = entries.get(RANDOM.nextInt(entries.size()));
        String[] values = entry.split(" ", 2);
        if (values.length != 2) {
            LOGGER.warn("[Odds And Ends] Failed to parse startup sound: {}", entry);
            return null;
        }

        ResourceLocation resource = ResourceLocation.tryParse(values[0]);
        float volume = Float.parseFloat(values[1]);
        if (resource == null || volume <= 0.05f) {
            LOGGER.warn("[Odds And Ends] Failed to parse startup sound: {}", entry);
            return null;
        }

        return Pair.of(resource, volume);
    }
}
