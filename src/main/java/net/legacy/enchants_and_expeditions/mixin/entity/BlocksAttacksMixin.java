package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.Blocks;
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

@Mixin(BlocksAttacks.class)
public abstract class BlocksAttacksMixin {

    @Inject(method = "disable", at = @At(value = "HEAD"), cancellable = true)
    private void parry(ServerLevel level, LivingEntity entity, float duration, ItemStack stack, CallbackInfo ci) {
        ItemStack itemStack = entity.getItemBlockingWith();
        if (itemStack == null) return;
        if (EnchantingHelper.hasEnchantment(itemStack, EaEEnchantments.PARRY)) {
            if (new Random().nextInt(1, 6) <= EnchantingHelper.getLevel(itemStack, EaEEnchantments.PARRY)) {
                ci.cancel();
            }
        }
    }
}