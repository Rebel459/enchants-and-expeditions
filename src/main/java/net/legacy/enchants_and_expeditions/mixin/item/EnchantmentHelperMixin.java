package net.legacy.enchants_and_expeditions.mixin.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "setEnchantments", at = @At("HEAD"), cancellable = true)
    private static void limitEnchantments(ItemStack stack, ItemEnchantments enchantments, CallbackInfo ci) {
        if (EaEConfig.get.enchanting.enchantment_limit == -1) return;
        if (enchantments.size() > EaEConfig.get.enchanting.enchantment_limit) {
            Map<Enchantment, Integer> limitedEnchantments = new java.util.LinkedHashMap<>();
            int count = 0;
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
                if (count < EaEConfig.get.enchanting.enchantment_limit) {
                    limitedEnchantments.put(entry.getKey().value(), entry.getValue());
                    count++;
                } else {
                    break;
                }
            }
            EnchantmentHelper.setEnchantments(stack, (ItemEnchantments) limitedEnchantments);
            ci.cancel();
        }
    }
}