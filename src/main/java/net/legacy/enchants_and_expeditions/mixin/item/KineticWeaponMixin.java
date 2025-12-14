package net.legacy.enchants_and_expeditions.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.KineticWeapon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(KineticWeapon.class)
public abstract class KineticWeaponMixin {

    @ModifyVariable(
            method = "damageEntities",
            at = @At(
                    value = "LOAD",
                    target = "Lnet/minecraft/item/component/KineticWeapon;damageMultiplier:F"
            ),
            ordinal = 1
    )
    private float EaE$boostDamageMultiplierIfSharpness(float original, @Local(argsOnly = true) ItemStack stack) {
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.CHARGE)) return original;
        int level = EnchantingHelper.getLevel(stack, EaEEnchantments.CHARGE);
        return (int) (original * (1 + level * 0.1F));
    }
}