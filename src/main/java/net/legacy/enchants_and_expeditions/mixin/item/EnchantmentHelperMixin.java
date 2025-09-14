package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Stream;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.selectEnchantment;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "enchantItem(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;ILjava/util/stream/Stream;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void EaE$enchantItem(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<ItemStack> cir) {
        if (EaEConfig.get.general.enchantment_limit != 0) return;

        Stream<Holder<Enchantment>> newEnchantments = possibleEnchantments.limit(EaEConfig.get.general.enchantment_limit);

        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, newEnchantments);
        list = EnchantingHelper.evaluateEnchantments(stack, list);
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
        if (EaEConfig.get.general.enchantment_limit != 0) return;

        possibleEnchantments = possibleEnchantments.filter(enchantment -> {
            return !EnchantingHelper.configureEnchantments(enchantment);
        });

        Stream<Holder<Enchantment>> newEnchantments = possibleEnchantments.limit(EaEConfig.get.general.enchantment_limit);

        List<EnchantmentInstance> list = selectEnchantment(random, stack, level, newEnchantments);
        list = EnchantingHelper.evaluateEnchantments(stack, list);
        if (stack.is(Items.BOOK)) {
            stack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        for (EnchantmentInstance enchantmentInstance : list) {
            stack.enchant(enchantmentInstance.enchantment(), enchantmentInstance.level());
        }

        cir.setReturnValue(stack);
    }

    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"), cancellable = true)
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
            if (isCompatible && !EnchantingHelper.configureEnchantments(instance.enchantment())) {
                filteredResults.add(instance);
            }
        }

        cir.setReturnValue(filteredResults);
    }

    @ModifyVariable(
            method = "getAvailableEnchantmentResults",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private static boolean EaE$modifyBookCheck(boolean original, int level, ItemStack stack, Stream<RegistryAccess.RegistryEntry<Enchantment>> possibleEnchantments) {
        if (stack.getEnchantments().size() < EaEConfig.get.general.enchantment_limit)
            return stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
        else
            return stack.is(Items.BOOK);
    }

    @Inject(method = "modifyDamage", at = @At("TAIL"), cancellable = true)
    private static void EaE$impotenceCurse(ServerLevel level, ItemStack tool, Entity entity, DamageSource damageSource, float damage, CallbackInfoReturnable<Float> cir) {
        if (!(EnchantingHelper.hasEnchantment(tool, EaEEnchantments.IMPOTENCE_CURSE) && damageSource.is(DamageTypeTags.IS_PROJECTILE))) return;
        float oldDamage = cir.getReturnValue();
        float newDamage = Math.max(1, oldDamage / 2 - 1);
        cir.setReturnValue(Math.min(oldDamage, newDamage));
    }
}