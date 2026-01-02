package net.legacy.enchants_and_expeditions.mixin.item;

import com.google.common.collect.Lists;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.selectEnchantment;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void EaE$enchantItem(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<ItemStack> cir) {
        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, possibleEnchantments);
        list = EnchantingHelper.evaluateEnchantments(stack, list);
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
        boolean bl; // allow enchanted book re-enchanting
        if (stack.getEnchantments().size() < EaEConfig.get.general.enchantment_limit) bl = stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
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
                Enchantment existingEnchant = entry.getKey().value();
                if (existingEnchant.exclusiveSet() == instance.enchantment.value().exclusiveSet()) {
                    isCompatible = false;
                    break;
                }
            }
            if (isCompatible) filteredResults.add(instance);
        }

        cir.setReturnValue(filteredResults);
    }
}