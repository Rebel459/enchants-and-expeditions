package net.legacy.enchants_and_expeditions.mixin.integration.combat_reborn;

import net.legacy.combat_reborn.util.ShieldHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.registry.EaEMobEffects;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShieldHelper.class)
public abstract class ShieldHelperMixin {

    @Inject(method = "onParry", at = @At("HEAD"))
    private static void onParryEnchantments(ServerLevel serverLevel, LivingEntity attacker, LivingEntity attacked, ItemStack stack, CallbackInfo ci) {
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FROSTBITE)) {
            EnchantingHelper.applyFreezing(serverLevel, attacker, attacked, 200);
        }
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SCORCH)) {
            attacker.setRemainingFireTicks(Math.max(attacker.getRemainingFireTicks(), 40));
        }
    }

    @Inject(method = "onDisable", at = @At("HEAD"))
    private static void onDisableEnchantments(ServerLevel serverLevel, LivingEntity attacked, LivingEntity attacker, float duration, ItemStack stack, boolean strengthDepleted, CallbackInfo ci) {
        if (strengthDepleted) {
            if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.JUSTICE_BLESSING)) {
                float multiplier = Math.max(Math.min(duration / 15F, 2F), 0.5F);
                int entityCount = EnchantingHelper.applyAreaKnockback(serverLevel, attacked, attacker, multiplier);
                if (entityCount >= 1) {
                    int absorptionOverheal = 0;
                    if (attacked.hasEffect(MobEffects.ABSORPTION)) {
                        absorptionOverheal += (1 + attacked.getEffect(MobEffects.ABSORPTION).getAmplifier()) * 4;
                    }
                    int max = (int) (attacked.getMaxHealth() / 2F);
                    int amountToAdd = (entityCount * 2) - 1;
                    attacked.addEffect(new MobEffectInstance(EaEMobEffects.OVERHEAL, (int) (300 * multiplier), Math.min(Math.min(amountToAdd, max), Math.max((int) attacked.getMaxHealth() - absorptionOverheal, max))));
                }
            }
        }
    }
}