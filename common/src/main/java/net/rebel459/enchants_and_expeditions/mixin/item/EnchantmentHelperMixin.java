package net.rebel459.enchants_and_expeditions.mixin.item;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.logging.LogUtils;
import net.minecraft.world.inventory.AnvilMenu;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.rebel459.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantmentSlots;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.selectEnchantment;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void EaE$enchantItem(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<ItemStack> cir) {
        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, possibleEnchantments);
        list = EnchantingHelper.evaluateEnchantments(stack, list, level);
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        for (EnchantmentInstance enchantmentInstance : list) {
            stack.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
        }

        cir.setReturnValue(stack);
    }

    @Inject(method = "getAvailableEnchantmentResults", at = @At(value = "HEAD"), cancellable = true)
    private static void EaE$getAvailableEnchantmentResults(int level, ItemStack stack, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        boolean bl; // allow enchanted book re-enchanting (disabled)
        if (EnchantingHelper.hasSlots(stack) && stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(stack) > 0) bl = stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
        else bl = stack.is(Items.BOOK);

        possibleEnchantments.filter(holder -> holder.value().isPrimaryItem(stack) || bl).forEach(holder -> {
            Enchantment enchantment = holder.value();

            for (int j = enchantment.getMaxLevel(); j >= enchantment.getMinLevel(); j--) {
                if (level >= enchantment.getMinCost(j) && (level <= enchantment.getMaxCost(j) || (j == enchantment.getMaxLevel() && !holder.is(EaEEnchantmentTags.ENFORCE_MAXIMUM_LEVEL)))  // override max level check
                        && !EnchantingHelper.configureEnchantments(holder)
                        && !(stack.is(ItemTags.AXES) && holder.is(EaEEnchantmentTags.NOT_ON_AXES)) // handle axe enchantments
                        && !(stack.is(EaEItemTags.ANIMAL_ARMOR) && holder.is(EaEEnchantmentTags.NOT_ON_ANIMAL_ARMOR))) {
                    list.add(new EnchantmentInstance(holder, j));
                    break;
                }
            }
        });
        cir.setReturnValue(list);
    }

    @Inject(method = "getAvailableEnchantmentResults", at = @At("TAIL"), cancellable = true)
    private static void EaE$filterIncompatibleEnchantments(int power, ItemStack stack, Stream<Enchantment> enchantments, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        List<EnchantmentInstance> originalResults = cir.getReturnValue();

        ItemEnchantments existingEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        if (existingEnchantments.isEmpty()) {
            return;
        }

        List<EnchantmentInstance> filteredResults = new ArrayList<>();
        for (EnchantmentInstance instance : originalResults) {
            boolean isCompatible = true;
            for (var entry : existingEnchantments.entrySet()) {
                Holder<Enchantment> existingEnchant = entry.getKey();
                if (existingEnchant.equals(instance.enchantment())) {
                    continue;
                }
                if (!Enchantment.areCompatible(existingEnchant, instance.enchantment())) {
                    isCompatible = false;
                    break;
                }
            }
            if (isCompatible) filteredResults.add(instance);
        }

        cir.setReturnValue(filteredResults);
    }

    @Inject(method = "updateEnchantments", at = @At("HEAD"))
    private static void EaE$captureArcana(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, CallbackInfoReturnable<ItemEnchantments> cir, @Share("hadArcana") LocalBooleanRef hadArcana) {
        hadArcana.set(EnchantingHelper.hasEnchantment(itemStack, EaEEnchantments.ARCANA_BLESSING));
    }

    @Inject(method = "updateEnchantments", at = @At("RETURN"))
    private static void EaE$arcanaBlessing(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, CallbackInfoReturnable<ItemEnchantments> cir, @Share("hadArcana") LocalBooleanRef hadArcana) {
        if (cir.getReturnValue().isEmpty()) return;
        boolean hasArcana = EnchantingHelper.hasEnchantment(itemStack, EaEEnchantments.ARCANA_BLESSING);
        if (EnchantingHelper.hasSlots(itemStack)) {
            EnchantmentSlots slots = itemStack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get());
            if (!hadArcana.get() && hasArcana) {
                slots = slots.setModifier(slots.modifier() + 1);
            } else if (hadArcana.get() && !hasArcana) {
                slots = slots.setModifier(slots.modifier() - 1);
            }
            itemStack.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), slots);
        }
    }
}
