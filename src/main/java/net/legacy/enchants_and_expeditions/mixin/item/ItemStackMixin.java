package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ItemEnchantments getEnchantments();

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void limitEnchantments(CallbackInfoReturnable<Boolean> cir) {
        if ((this.getEnchantments().size() < EaEConfig.get.enchanting.enchantment_limit || EaEConfig.get.enchanting.enchantment_limit == 0) && EaEConfig.get.enchanting.allow_repeat_enchanting)
                cir.setReturnValue(true);
    }
}