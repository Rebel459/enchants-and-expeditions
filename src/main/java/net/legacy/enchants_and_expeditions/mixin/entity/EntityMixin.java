package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract SoundSource getSoundSource();

    @Inject(method = "killedEntity", at = @At(value = "HEAD"))
    private void bloodlust(ServerLevel level, LivingEntity killed, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = Entity.class.cast(this);
        if (!(entity instanceof LivingEntity livingEntity)) return;
        ItemStack stack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);

        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BLOODLUST)) {
            int amount = EnchantingHelper.getLevel(stack, EaEEnchantments.BLOODLUST);
            livingEntity.setHealth(livingEntity.getHealth() + amount);
            if (livingEntity.getHealth() > livingEntity.getMaxHealth())
                livingEntity.setHealth(livingEntity.getMaxHealth());
            level.playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.THORNS_HIT, this.getSoundSource(), 1F, 1F);
        }
    }

    @Inject(method = "killedEntity", at = @At(value = "HEAD"))
    private void quickstep(ServerLevel level, LivingEntity killed, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = Entity.class.cast(this);
        if (!(entity instanceof LivingEntity livingEntity)) return;
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
        if (livingEntity instanceof Wolf) stack = livingEntity.getItemBySlot(EquipmentSlot.BODY);

        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.QUICKSTEP)) {
            int amount = EnchantingHelper.getLevel(stack, EaEEnchantments.QUICKSTEP) * 2;
            if (!(livingEntity.hasEffect(MobEffects.SPEED) && livingEntity.getEffect(MobEffects.SPEED).getDuration() >= amount)) livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, amount));
        }
    }
}