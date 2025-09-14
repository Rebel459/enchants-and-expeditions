package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow public abstract ItemStack getWeaponItem();

    @ModifyVariable(method = "shoot", at = @At(value = "HEAD"), index = 3, argsOnly = true)
    private double velocity(double value) {
        ItemStack stack = this.getWeaponItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.VELOCITY)) {
            double multiplier = 1 + 0.1 * EnchantingHelper.getLevel(stack, EaEEnchantments.VELOCITY);
            return value * multiplier;
        }
        else return value;
    }
}