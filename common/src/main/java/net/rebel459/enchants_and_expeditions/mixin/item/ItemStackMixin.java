package net.rebel459.enchants_and_expeditions.mixin.item;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract DataComponentMap getComponents();

    @Shadow public abstract Item getItem();

    @Shadow public abstract ItemEnchantments getEnchantments();

    @Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
    private void canEnchant(CallbackInfoReturnable<Boolean> cir) {
        if (!EaEConfig.get.general.repeat_table_enchanting) return;
        ItemStack stack = ItemStack.class.cast(this);
        if (!cir.getReturnValue() && stack.isEnchanted() && (EnchantingHelper.enchantmentScore(stack) < EaEConfig.get.general.enchantment_limit)) {
            cir.setReturnValue(true);
        }
    }
}