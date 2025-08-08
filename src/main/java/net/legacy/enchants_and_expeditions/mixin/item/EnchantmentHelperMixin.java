package net.legacy.enchants_and_expeditions.mixin.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

    @Inject(method = "selectEnchantment", at = @At("RETURN"), cancellable = true)
    private static void limitEnchantments(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        if (EaEConfig.get.enchanting.enchantment_limit != 0) {
            var selectedEnchantments = cir.getReturnValue();
            if (selectedEnchantments.size() > EaEConfig.get.enchanting.enchantment_limit) {
                var shuffled = new ArrayList<>(selectedEnchantments);
                Collections.shuffle(shuffled);
                var selected = shuffled.stream().limit(EaEConfig.get.enchanting.enchantment_limit);
                cir.setReturnValue(selected.toList());
            }
        }
    }
}