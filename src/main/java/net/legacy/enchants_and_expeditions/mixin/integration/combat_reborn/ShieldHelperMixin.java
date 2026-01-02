package net.legacy.enchants_and_expeditions.mixin.integration.combat_reborn;

import net.legacy.combat_reborn.util.ShieldHelper;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShieldHelper.class)
public abstract class ShieldHelperMixin {

    @Inject(method = "onParry", at = @At("HEAD"))
    private static void shieldEnchantments(ServerLevel serverLevel, LivingEntity attacker, LivingEntity attacked, ItemStack stack, CallbackInfo ci) {
        if (!EaEConfig.get.integrations.combat_reborn) return;
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FROSTBITE)) {
            EnchantingHelper.applyFreezing(serverLevel, attacker, attacked, 200);
        }
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SCORCH)) {
            attacker.setRemainingFireTicks(Math.max(attacker.getRemainingFireTicks(), 40));
        }
    }
}