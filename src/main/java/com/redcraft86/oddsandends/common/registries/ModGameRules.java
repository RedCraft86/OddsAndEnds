package com.redcraft86.oddsandends.common.registries;

import net.minecraft.world.level.GameRules;

public class ModGameRules {
    public static GameRules.Key<GameRules.BooleanValue> NO_ATK_COOLDOWN;

    public static void registerRules() {
        NO_ATK_COOLDOWN = GameRules.register("noAttackCooldown",
                GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
    }
}
