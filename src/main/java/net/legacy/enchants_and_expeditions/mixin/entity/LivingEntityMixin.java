package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected float lastHurt;
    @Shadow private int lastHurtByMobTimestamp;

    @Shadow public abstract boolean equipmentHasChanged(ItemStack oldItem, ItemStack newItem);

    @Unique
    DamageSource damageSource;

    @Inject(method = "hurtServer", at = @At(value = "HEAD"))
    private void getDamageSource(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.damageSource = damageSource;
    }

    @Inject(method = "getFrictionInfluencedSpeed", at = @At(value = "TAIL"), cancellable = true)
    private void icebound(float friction, CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = LivingEntity.class.cast(this);
        ItemStack stack;
        if (entity instanceof Animal) stack = entity.getItemBySlot(EquipmentSlot.BODY);
        else stack = entity.getItemBySlot(EquipmentSlot.FEET);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.ICEBOUND) && entity.getBlockStateOn().is(Blocks.ICE)) {
            int multiplier = 1 + EnchantingHelper.getLevel(stack, EaEEnchantments.ICEBOUND) / 10;
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }

    @Inject(method = "getSpeed", at = @At(value = "TAIL"), cancellable = true)
    private void blazingSpeed(CallbackInfoReturnable<Float> cir) {
        LivingEntity entity = LivingEntity.class.cast(this);
        ItemStack stack;
        if (entity instanceof Animal) stack = entity.getItemBySlot(EquipmentSlot.BODY);
        else stack = entity.getItemBySlot(EquipmentSlot.FEET);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BLAZING) && entity.getInBlockState().is(Blocks.FIRE)) {
            int multiplier = 1 + EnchantingHelper.getLevel(stack, EaEEnchantments.BLAZING) / 10;
            cir.setReturnValue(cir.getReturnValue() * multiplier);
        }
    }

    @Inject(method = "hurtServer", at = @At(value = "TAIL"))
    private void infernoBlessingExtendFire(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.INFERNO_BLESSING) && entity.isOnFire()) {
                int setFireTicks = (int) (entity.getRemainingFireTicks() + amount * 10);
                if (setFireTicks > 200) setFireTicks = 200;
                entity.setRemainingFireTicks(setFireTicks);
            }
        }
    }
    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float infernoBlessingDamage(float value) {
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.INFERNO_BLESSING)) {
                value += 2;
            }
        }
        return value;
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float entropy(float value) {
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.ENTROPY)) {
                int entropy = EnchantingHelper.getLevel(attackerStack, EaEEnchantments.ENTROPY);
                value += new Random().nextInt(-1, 3 + entropy);
            }
        }
        return value;
    }

    @Inject(method = "dropAllDeathLoot", at = @At(value = "HEAD"))
    private void getDamageSource(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        this.damageSource = damageSource;
    }
}