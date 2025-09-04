package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    DamageSource damageSource;

    @Inject(method = "hurtServer", at = @At(value = "HEAD"))
    private void getDamageSource(ServerLevel level, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.damageSource = damageSource;
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
}