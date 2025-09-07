package com.redcraft86.oddsandends.events;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.lanternlib.TransientFlags;
import com.redcraft86.oddsandends.FlagKeys;
import com.redcraft86.oddsandends.OddsAndEndsRules;
import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.common.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

@EventBusSubscriber(modid = OddsAndEnds.MOD_ID)
public class ServerEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    static void onServerStart(LevelEvent.Load e) {
        LevelAccessor level = e.getLevel();
        if (level.isClientSide()) {
            return;
        }

        SoulCampfireEffects.generateEffectList();
        ImprovedBoneMeal.generateFlowerList((Level)level);
    }

    @SubscribeEvent
    static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        Level level = e.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = e.getPos();
        Player player = e.getEntity();
        ItemStack item = e.getItemStack();

        if (e.getHand() == InteractionHand.MAIN_HAND) {
            e.setCanceled(ImprovedBoneMeal.handleInteract(level, player, item, pos));
        } // else {
        // }
    }

    @SubscribeEvent
    static void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        Level level = e.getLevel();
        if (level.isClientSide()) {
            return;
        }

        Player player = e.getEntity();
        Entity target = e.getTarget();
        ItemStack item = e.getItemStack();

        if (e.getHand() == InteractionHand.MAIN_HAND) {
            if (CommonCfg.INFINITE_VILLAGERS.get()) {
                if (target instanceof AbstractVillager villager) {
                    for (MerchantOffer offer : villager.getOffers()) {
                        offer.maxUses = Integer.MAX_VALUE;
                        offer.resetUses();
                    }
                }
            }
        } // else {
        // }
    }

    @SubscribeEvent
    static void onAttackEntity(AttackEntityEvent e) {
        Entity target = e.getTarget();
        Level level = target.level();
        if (level.isClientSide()) {
            return;
        }

        Player player = e.getEntity();
        ItemStack attackItem = player.getMainHandItem();

        if (CommonCfg.NO_PET_ATTACK.get() && target instanceof OwnableEntity petMob) {
            // Attacker must be the owner for the attack to fail
            e.setCanceled(petMob.getOwner() == player);
            return;
        }

        if (CommonCfg.NO_VILLAGER_ATTACK.get() && target instanceof AbstractVillager) {
            // Attacker must use a tool to attack otherwise it will fail
            e.setCanceled(!(attackItem.getItem() instanceof TieredItem));
            return;
        }
    }

    @SubscribeEvent
    static void onLivingDamage(LivingDamageEvent.Post e) {
        LivingEntity target = e.getEntity();
        Level level = target.level();
        if (level.isClientSide() || target.getLastDamageSource() == null) {
            return;
        }

        if (level.getGameRules().getBoolean(OddsAndEndsRules.NO_ATK_COOLDOWN)
                && target.getLastDamageSource().is(DamageTypes.PLAYER_ATTACK)) {
            target.invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    static void OnBonemeal(BonemealEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = e.getPos();
        Player player = e.getPlayer();
        ImprovedBoneMeal.handleBoneMeal(level, player, pos);
    }

    @SubscribeEvent(receiveCanceled = true)
    static void onWorldCreate(LevelEvent.CreateSpawnPosition e) {
        LevelAccessor levelAccessor = e.getLevel();
        if (levelAccessor.isClientSide()) {
            return;
        }

        TransientFlags.addFlag(FlagKeys.GENERATING_WORLD);
        if (StructureSpawnPoint.handle(levelAccessor)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void onMobGrief(EntityMobGriefingEvent e) {
        Entity entity = e.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        ResourceLocation resource = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        e.setCanGrief(!CommonCfg.GRIEF_BLACKLIST.get().contains(resource.toString()));
    }

    @SubscribeEvent
    static void trueInfinity(final LivingGetProjectileEvent e) {
        if (!CommonCfg.TRUE_INFINITY.get()) {
            return;
        }

        ItemStack weaponStack = e.getProjectileWeaponItemStack();
        if (e.getEntity() instanceof Player player
                && player.level() instanceof ServerLevel level
                && weaponStack.getItem() instanceof ProjectileWeaponItem weapon
                && e.getProjectileItemStack().isEmpty()) {

            int ammoCount = 0;
            ItemStack ammo = weapon.getDefaultCreativeAmmo(player, weaponStack);
            if (!(ammo.getItem() instanceof ArrowItem arrow && arrow.isInfinite(ammo, weaponStack, player))) {
                ammoCount = EnchantmentHelper.processAmmoUse(level, weaponStack, ammo, 1);
            }

            if (ammoCount == 0) {
                e.setProjectileItemStack(ammo);
            }
        }
    }
}
