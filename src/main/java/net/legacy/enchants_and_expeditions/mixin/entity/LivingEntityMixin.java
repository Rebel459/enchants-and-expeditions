package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseFireBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @Shadow public abstract void makePoofParticles();

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

    @Unique
    DamageSource damageSource;

    @Unique
    int secondProgress;

    @Inject(method = "hurtServer", at = @At(value = "HEAD"))
    private void getDamageSource(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.damageSource = damageSource;
    }

    @Inject(method = "hurtServer", at = @At(value = "TAIL"))
    private void infernoBlessingExtendFire(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.INFERNO_BLESSING) && entity.isOnFire()) {
                int setFireTicks = entity.getRemainingFireTicks() + 50;
                if (setFireTicks > 200) setFireTicks = 200;
                entity.setRemainingFireTicks(setFireTicks);
            }
        }
    }
    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float infernoBlessingDamage(float value) {
        if (this.damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.INFERNO_BLESSING)) {
                value += 1;
            }
        }
        return value;
    }

    @Inject(method = "hurtServer", at = @At(value = "TAIL"))
    private void frostbite(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity attacked = LivingEntity.class.cast(this);
        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackedChestplate = attacked.getItemBySlot(EquipmentSlot.CHEST);
            if (EnchantingHelper.hasEnchantment(attackedChestplate, EaEEnchantments.FROSTBITE) && attacked.getHealth() > attacked.getMaxHealth() - 1) {
                int duration = 200;
                if (attacker.getTicksFrozen() < duration) attacker.setTicksFrozen(duration);
                level.sendParticles(ParticleTypes.SNOWFLAKE, attacker.getX(), attacker.getRandomY(), attacker.getZ(), 10, 0, -1, 0, 0.5);
            }
        }
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float entropy(float value) {
        if (this.damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.ENTROPY)) {
                int entropy = EnchantingHelper.getLevel(attackerStack, EaEEnchantments.ENTROPY);
                value += new Random().nextInt(-1, 3 + entropy);
            }
        }
        return value;
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float vengeanceBlessing(float value) {
        if (this.damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack attackerStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.VENGEANCE_BLESSING) && attacker.getHealth() < 7) {
                float amount = -attacker.getHealth() / 2 + 6;
                value += amount;
            }
        }
        return value;
    }

    @Inject(
            method = "getJumpPower()F",
            at = @At(value = "TAIL"),
            cancellable = true
    )
    private void leaping(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = LivingEntity.class.cast(this);
        if (!(livingEntity instanceof AbstractHorse horse)) return;
        ItemStack stack = horse.getBodyArmorItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.LEAPING)) {
            int level = EnchantingHelper.getLevel(stack, EaEEnchantments.LEAPING);
            cir.setReturnValue(cir.getReturnValue() * (1 + level * 0.1F));
        }
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float ferocity(float value) {
        if (this.damageSource.getEntity() instanceof Wolf wolf) {
            ItemStack attackerStack = wolf.getBodyArmorItem();
            if (EnchantingHelper.hasEnchantment(attackerStack, EaEEnchantments.FEROCITY)) {
                int ferocity = EnchantingHelper.getLevel(attackerStack, EaEEnchantments.FEROCITY);
                value += ferocity;
            }
        }
        return value;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void ticksToSeconds(CallbackInfo ci) {
        if (this.secondProgress < 20) {
            this.secondProgress++;
        }
        else {
            second();
            this.secondProgress = 0;
        }
    }

    @Unique
    private void second() {
        temperingBlessing();
        fluidityBlessing();
    }

    @Unique
    private void temperingBlessing() {
        Entity entity = Entity.class.cast(this);
        if (!(entity.getRemainingFireTicks() > 0)) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = this.getItemBySlot(slot);
                if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.TEMPERING_BLESSING) && stack.getDamageValue() >= 1) {
                    stack.setDamageValue(stack.getDamageValue() - 1);
                    if (stack.getDamageValue() < 0) stack.setDamageValue(0);
                }
            }
        }
    }

    @Unique
    private void fluidityBlessing() {
        Entity entity = Entity.class.cast(this);
        if (!entity.isInWater()) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = this.getItemBySlot(slot);
                if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FLUIDITY_BLESSING)) {
                    if (entity instanceof LivingEntity livingEntity) livingEntity.heal(0.5F);
                }
            }
        }
    }

    @Inject(method = "getSecondsToDisableBlocking", at = @At(value = "TAIL"), cancellable = true)
    private void cleaving(CallbackInfoReturnable<Float> cir) {
        float disableTime = cir.getReturnValue();
        if (disableTime <= 0) return;
        ItemStack stack = this.getWeaponItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.CLEAVING)) {
            disableTime = disableTime + EnchantingHelper.getLevel(stack, EaEEnchantments.CLEAVING);
            cir.setReturnValue(disableTime);
        }
    }
}