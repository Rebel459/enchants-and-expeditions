package net.legacy.enchants_and_expeditions.mixin.item;

import com.google.common.collect.Lists;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.selectEnchantment;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void EaE$enchantItem(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<ItemStack> cir) {
        if (EaEConfig.get.loot.world_enchantment_limit != 0) return;

        Stream<Holder<Enchantment>> newEnchantments = possibleEnchantments.limit(EaEConfig.get.loot.world_enchantment_limit);

        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, newEnchantments);
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        for (EnchantmentInstance enchantmentInstance : list) {
            stack.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
        }

        cir.setReturnValue(stack);
    }

    @Inject(method = "selectEnchantment", at = @At("HEAD"), cancellable = true)
    private static void EaE$selectEnchantment(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<ItemStack> cir) {
        if (EaEConfig.get.loot.world_enchantment_limit != 0) return;

        Stream<Holder<Enchantment>> newEnchantments = possibleEnchantments.limit(EaEConfig.get.loot.world_enchantment_limit);

        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, newEnchantments);
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        for (EnchantmentInstance enchantmentInstance : list) {
            stack.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
        }

        cir.setReturnValue(stack);
    }
}