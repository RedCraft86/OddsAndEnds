package com.redcraft86.oddsandends.mixin;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import org.objectweb.asm.tree.ClassNode;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class MixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> runningMods = new HashSet<>();

    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        if (mixinClass.contains("oddsandends.mixin.common.MixinPortalShape") && hasMod("betternether")) {
            LOGGER.info("[OddsAndEnds] Shapeless Portals force-disabled as BetterNether already implements them");
            return false;
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    private static boolean hasMod(String modId) {
        if (runningMods.contains(modId)) {
            return true;
        }

        if (FMLLoader.getLoadingModList().getModFileById(modId) != null) {
            runningMods.add(modId);
            return true;
        }

        return false;
    }
}
