package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Shadow public abstract ItemStack getWeaponItem();

    @ModifyVariable(method = "shoot", at = @At(value = "HEAD"), index = 7, argsOnly = true)
    private float velocityVelocity(float value) {
        ItemStack stack = this.getWeaponItem();
        if (this.getWeaponItem() == null) return value;
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.VELOCITY)) {
            float multiplier = 1 + 0.15F * EnchantingHelper.getLevel(stack, EaEEnchantments.VELOCITY);
            return value * multiplier;
        }
        else return value;
    }

    @ModifyVariable(method = "shoot", at = @At(value = "HEAD"), index = 8, argsOnly = true)
    private float velocityInaccuracy(float value) {
        ItemStack stack = this.getWeaponItem();
        if (this.getWeaponItem() == null) return value;
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.VELOCITY)) {
            float multiplier = 1 - 0.1F * EnchantingHelper.getLevel(stack, EaEEnchantments.VELOCITY);
            return value * multiplier;
        }
        else return value;
    }

    @ModifyVariable(method = "shoot", at = @At(value = "HEAD"), index = 8, argsOnly = true)
    private float falteringCurse(float value) {
        ItemStack stack = this.getWeaponItem();
        if (this.getWeaponItem() == null) return value;
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FALTERING_CURSE)) {
            return value * 1.5F;
        }
        else return value;
    }
}