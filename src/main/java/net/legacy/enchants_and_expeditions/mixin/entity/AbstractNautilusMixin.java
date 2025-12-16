package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractNautilus.class)
public abstract class AbstractNautilusMixin {

    @Inject(
            method = "executeRidersJump",
            at = @At(value = "TAIL")
    )
    private void slipstreamHeal(float f, Player player, CallbackInfo ci) {
        AbstractNautilus nautilus = AbstractNautilus.class.cast(this);
        ItemStack stack = nautilus.getBodyArmorItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SLIPSTREAM)) {
            nautilus.heal(1);
        }
    }
}