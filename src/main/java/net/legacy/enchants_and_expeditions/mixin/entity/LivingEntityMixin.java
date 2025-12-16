package net.legacy.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.registry.EaEItems;
import net.legacy.enchants_and_expeditions.registry.EaEMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
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

import java.util.List;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow
    public abstract @org.jspecify.annotations.Nullable LivingEntity asLivingEntity();

    @Shadow
    public abstract boolean addEffect(MobEffectInstance mobEffectInstance);

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
    private void displacementCurse(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = LivingEntity.class.cast(this);
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (damageSource.getEntity() instanceof LivingEntity attacker && EnchantingHelper.hasEnchantment(stack, EaEEnchantments.DISPLACEMENT_CURSE)) {
            for (MobEffectInstance instance : entity.getActiveEffectsMap().values()) {
                if (!entity.hasEffect(instance.getEffect())) return;

                Holder<MobEffect> effect = instance.getEffect();
                int duration = entity.getEffect(effect).getDuration();

                entity.removeEffect(effect);
                if (!attacker.hasEffect(effect)) attacker.addEffect(new MobEffectInstance(effect, duration / 2));
                entity.addEffect(new MobEffectInstance(effect, duration / 2));
            }
        }
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

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float jousting(float value) {
        if (this.damageSource.getEntity() instanceof LivingEntity entity && entity.getControlledVehicle() != null && entity.getControlledVehicle() instanceof LivingEntity riddenEntity) {
            ItemStack stack = entity.getWeaponItem();
            if (stack.is(ItemTags.SPEARS)) {
                float ignoredDamage = 0;
                if (stack.isEnchanted()) {
                    ignoredDamage = EnchantmentHelper.modifyDamage((ServerLevel) entity.level(), stack, this.asLivingEntity(), damageSource, 0F);
                }
                float baseDamage = 1;
                var modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers();
                for (ItemAttributeModifiers.Entry modifier : modifiers) {
                    if (modifier.attribute() == Attributes.ATTACK_DAMAGE) {
                        baseDamage += (float) modifier.modifier().amount();
                        break;
                    }
                }
                if (value - ignoredDamage >= baseDamage * 1.5F && EnchantingHelper.hasEnchantment(stack, EaEEnchantments.JOUSTING)) {
                    riddenEntity.heal(1F);
                    riddenEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, 100));
                    riddenEntity.level().getServer().getLevel(riddenEntity.level().dimension()).playSound(riddenEntity, riddenEntity.blockPosition(), SoundEvents.HORSE_EAT, entity.getSoundSource());
                }
            }
        }
        return value;
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private float conductivityBlessing(float value) {
        LogUtils.getLogger().info("Info: " + this.damageSource);
        LivingEntity attacked = LivingEntity.class.cast(this);
        if (attacked.hasEffect(EaEMobEffects.LIGHTNING_IMMUNE) && this.damageSource.is(DamageTypes.LIGHTNING_BOLT)) {
            LogUtils.getLogger().info("Tried");
            return 0F;
        }
        if (this.damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack stack = attacker.getWeaponItem();
            if (stack.is(ItemTags.SPEARS)) {
                float ignoredDamage = 0;
                if (stack.isEnchanted()) {
                    ignoredDamage = EnchantmentHelper.modifyDamage((ServerLevel) attacker.level(), stack, this.asLivingEntity(), damageSource, 0F);
                }
                float baseDamage = 1;
                var modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers();
                for (ItemAttributeModifiers.Entry modifier : modifiers) {
                    if (modifier.attribute() == Attributes.ATTACK_DAMAGE) {
                        baseDamage += (float) modifier.modifier().amount();
                        break;
                    }
                }
                if (value - ignoredDamage >= baseDamage * 2F && EnchantingHelper.hasEnchantment(stack, EaEEnchantments.CONDUCTIVITY_BLESSING)) {
                    ServerLevel level = attacked.level().getServer().getLevel(attacked.level().dimension());
                    if (attacked.level().isRainingAt(attacked.blockPosition())) {
                        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        lightning.setPos(attacked.position());
                        lightning.setVisualOnly(false);
                        level.addFreshEntity(lightning);
                        attacker.addEffect(new MobEffectInstance(EaEMobEffects.LIGHTNING_IMMUNE, 20, 0, true, false, false));
                        attacker.clearFire();
                        if (attacker.getControlledVehicle() instanceof LivingEntity attackerMount) {
                            attackerMount.addEffect(new MobEffectInstance(EaEMobEffects.LIGHTNING_IMMUNE, 20, 0, true, false, false));
                            attackerMount.clearFire();
                        }
                        stack.hurtAndBreak(1, attacker, attacker.getEquipmentSlotForItem(stack));
                        if (attacker instanceof Player player && stack.has(DataComponents.USE_COOLDOWN)) {
                            player.getCooldowns().addCooldown(stack, 200);
                            player.stopUsingItem();
                        }
                    }
                }
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

    @WrapOperation(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float slidingCurse(Block block, Operation<Float> original) {
        ItemStack stack = this.getItemBySlot(EquipmentSlot.FEET);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SLIDING_CURSE) && original.call(block) >= 0.6F && original.call(block) < 0.98F) return 0.98F;
        else return original.call(block);
    }

    @Inject(method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V", at = @At("TAIL"))
    public void elderGuardianLootInject(ServerLevel level, DamageSource damageSource, boolean playerKill, CallbackInfo ci) {
        LivingEntity entity = LivingEntity.class.cast(this);
        if (entity.getType() == EntityType.ELDER_GUARDIAN && new Random().nextInt(3) == 2 && EaEConfig.get.misc.loot_table_injects) entity.spawnAtLocation(level, EaEItems.TOME_OF_FLOW);
    }
}