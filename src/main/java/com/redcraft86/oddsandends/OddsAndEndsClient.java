package com.redcraft86.oddsandends;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = OddsAndEnds.MOD_ID, dist = Dist.CLIENT)
// @EventBusSubscriber(modid = OddsAndEnds.MOD_ID, value = Dist.CLIENT) // Add this if using @SubscribeEvent
public class OddsAndEndsClient {
    public OddsAndEndsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
