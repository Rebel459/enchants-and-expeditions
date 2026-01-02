package net.legacy.enchants_and_expeditions.mixin.function;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.stream.Stream;

@Mixin(EnchantWithLevelsFunction.class)
public class EnchantWithLevelsFunctionMixin {

    @Shadow @Final public NumberProvider levels;

    @Shadow @Final public Optional<HolderSet<Enchantment>> options;

    @Inject(method = "run", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$enchantFallback(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        EnchantWithLevelsFunction function = EnchantWithLevelsFunction.class.cast(this);
        if (function.options.isPresent() || !EaEConfig.get.misc.enchant_function_fallback) return;

        RandomSource randomSource = context.getRandom();
        RegistryAccess registryAccess = context.getLevel().registryAccess();

        Optional<HolderSet.Named<Enchantment>> optional = Optional.of(context.getLevel().holderLookup(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));

        cir.setReturnValue(enchantItem(randomSource, stack, function.levels.getInt(context), registryAccess, optional));
    }

    @Inject(method = "run", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$randomLootModification(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        RandomSource randomSource = context.getRandom();
        RegistryAccess registryAccess = context.getLevel().registryAccess();
        if (this.options.isPresent() && this.options.get() != Optional.of(context.getLevel().holderLookup(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT)).get()) return;
        cir.setReturnValue(enchantItem(randomSource, stack, this.levels.getInt(context), registryAccess, this.options));
    }

    @Unique
    private static ItemStack enchantItem(
            RandomSource random, ItemStack stack, int level, RegistryAccess registryAccess, Optional<? extends HolderSet<Enchantment>> possibleEnchantments
    ) {
        return enchantItem(
                random,
                stack,
                level,
                possibleEnchantments.map(HolderSet::stream)
                        .orElseGet(() -> registryAccess.lookupOrThrow(Registries.ENCHANTMENT).listElements().map(reference -> reference))
        );
    }

    @Unique
    private static ItemStack enchantItem(RandomSource random, ItemStack stack, int level, Stream<Holder<Enchantment>> possibleEnchantments) {
        possibleEnchantments = possibleEnchantments.filter(enchantment -> {
            return !EnchantingHelper.onRandomLoot(enchantment, random);
        });

        return EnchantmentHelper.enchantItem(random, stack, level, possibleEnchantments);
    }
}