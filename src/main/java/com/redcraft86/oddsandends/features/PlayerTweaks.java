package com.redcraft86.oddsandends.features;

import com.redcraft86.oddsandends.ModTags;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.server.level.ServerLevel;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public final class PlayerTweaks {
    @SubscribeEvent
    static void handlePlayerAttack(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Level level = target.level();
        if (level.isClientSide()) {
            return;
        }

        Player player = event.getEntity();
        if (CommonCfg.NO_FRIENDLY_FIRE.get() && target instanceof OwnableEntity petMob) {
            event.setCanceled(petMob.getOwner() == player);
        }

        ItemStack item = player.getMainHandItem();
        if (CommonCfg.NO_VILLAGER_HIT.get() && target instanceof AbstractVillager) {
            event.setCanceled(item.isEmpty());
        }
    }

    @SubscribeEvent
    static void handlePlayerFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) {
            return;
        }

        if (CommonCfg.PLAYER_FALL_DAMPEN.get() && entity instanceof Player) {
            BlockState belowState = level.getBlockState(entity.blockPosition().below());
            event.setDamageMultiplier(belowState.is(ModTags.Blocks.DAMPEN_FALL_DAMAGE) ? 0.2f : 1.0f);
        }
    }

    @SubscribeEvent
    static void handleTrueInfinity(final LivingGetProjectileEvent event) {
        if (!CommonCfg.TRUE_INFINITY.get()) {
            return;
        }

        ItemStack weaponStack = event.getProjectileWeaponItemStack();
        if (event.getEntity() instanceof Player player
                && player.level() instanceof ServerLevel level
                && weaponStack.getItem() instanceof ProjectileWeaponItem weapon
                && event.getProjectileItemStack().isEmpty()) {

            int ammoCount = 0;
            ItemStack ammo = weapon.getDefaultCreativeAmmo(player, weaponStack);
            if (!(ammo.getItem() instanceof ArrowItem arrow && arrow.isInfinite(ammo, weaponStack, player))) {
                ammoCount = EnchantmentHelper.processAmmoUse(level, weaponStack, ammo, 1);
            }

            if (ammoCount == 0) {
                event.setProjectileItemStack(ammo);
            }
        }
    }
}
