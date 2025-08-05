package net.legacy.enchants_and_expeditions.mixin.function;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EnchantWithLevelsFunction.class)
public class EnchantWithLevelsFunctionMixin {

    @Inject(method = "run", at = @At(value = "HEAD"), cancellable = true)
    protected void enchantFallback(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        EnchantWithLevelsFunction function = EnchantWithLevelsFunction.class.cast(this);
        if (function.options.isPresent() || !EaEConfig.get.misc.enchant_function_fallback) return;

        RandomSource randomSource = context.getRandom();
        RegistryAccess registryAccess = context.getLevel().registryAccess();
        cir.setReturnValue(EnchantmentHelper.enchantItem(randomSource, stack, function.levels.getInt(context), registryAccess, Optional.of(context.getLevel().holderLookup(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT))));
    }
}