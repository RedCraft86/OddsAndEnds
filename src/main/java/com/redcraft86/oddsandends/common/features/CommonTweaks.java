package com.redcraft86.oddsandends.common.features;

import com.redcraft86.oddsandends.ModTags;
import com.redcraft86.oddsandends.configs.CommonCfg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

public class CommonTweaks {
    public static boolean handleBonemeal(Level level, Player player, ItemStack item, BlockPos pos) {
        if (!CommonCfg.BONEMEAL_DIRT_TO_GRASS.get() || !item.is(Items.BONE_MEAL)
                || !level.getBlockState(pos).is(Blocks.DIRT)) {
            return false;
        }

        level.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
        if (!player.isCreative()) {
            item.shrink(1);
        }
        return true;
    }

    public static void handleTrades(Entity target) {
        if (CommonCfg.INFINITE_TRADES.get() && target instanceof AbstractVillager villager) {
            for (MerchantOffer offer : villager.getOffers()) {
                offer.maxUses = Integer.MAX_VALUE;
                offer.resetUses();
            }
        }
    }

    public static boolean handlePlayerAttack(Player player, Entity target, ItemStack item) {
        if (CommonCfg.NO_FRIENDLY_FIRE.get() && target instanceof OwnableEntity petMob) {
            return petMob.getOwner() == player;
        }

        if (CommonCfg.NO_VILLAGER_HIT.get() && target instanceof AbstractVillager) {
            return item.isEmpty();
        }

        return false;
    }

    public static float handlePlayerFall(Level level, LivingEntity entity) {
        if (!CommonCfg.PLAYER_FALL_DAMPEN.get()) {
            return 1.0f;
        }
        BlockState blockBelow = level.getBlockState(entity.blockPosition().below());
        return (blockBelow.is(ModTags.Blocks.DAMPEN_FALL_DAMAGE) && entity instanceof Player) ? 0.2f : 1.0f;
    }

    public static void handleTrueInfinity(final LivingGetProjectileEvent event) {
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
