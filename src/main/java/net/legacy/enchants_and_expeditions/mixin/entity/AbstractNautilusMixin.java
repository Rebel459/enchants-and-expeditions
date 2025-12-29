package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractNautilus.class)
public abstract class AbstractNautilusMixin {

    @ModifyArg(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/TamableAnimal;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 2
    )
    private float slipstream(float f) {
        AbstractNautilus nautilus = AbstractNautilus.class.cast(this);
        ItemStack stack = nautilus.getBodyArmorItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SLIPSTREAM) && nautilus.isInWater() && (nautilus.isDashing() || nautilus.getJumpCooldown() > 30)) {
            f = 0F;
        }
        return f;
    }
}