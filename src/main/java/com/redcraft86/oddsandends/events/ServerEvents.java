package com.redcraft86.oddsandends.events;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import com.redcraft86.oddsandends.FlagKeys;
import com.redcraft86.oddsandends.common.*;
import com.redcraft86.oddsandends.OddsAndEnds;
import com.redcraft86.oddsandends.OddsAndEndsRules;
import com.redcraft86.oddsandends.configs.CommonCfg;
import com.redcraft86.lanternlib.TransientFlags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.enchantment.Enchantments;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = OddsAndEnds.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
            if (CommonCfg.INFINITE_VILLAGERS.get() && target instanceof AbstractVillager villager) {
                for (MerchantOffer offer : villager.getOffers()) {
                    offer.maxUses = Integer.MAX_VALUE;
                    offer.resetUses();
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
    static void onLivingDamage(LivingDamageEvent e) {
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
        Player player = e.getEntity();
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
    static void trueInfinity(ArrowNockEvent e) {
        if (CommonCfg.TRUE_INFINITY.get()) {
            ItemStack bow = e.getBow();
            if (bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0) {
                e.getEntity().startUsingItem(e.getHand());
                e.setAction(InteractionResultHolder.success(bow));
            }
        }
    }
}
