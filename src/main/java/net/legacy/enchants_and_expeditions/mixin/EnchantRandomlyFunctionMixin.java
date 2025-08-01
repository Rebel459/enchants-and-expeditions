package net.legacy.enchants_and_expeditions.mixin;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {

    @Inject(method = "run", at = @At(value = "TAIL"), cancellable = true)
    protected void enchantFallback(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        EnchantRandomlyFunction function = EnchantRandomlyFunction.class.cast(this);
        if (function.options.isPresent() || !EaEConfig.get.enchant_function_fallback) return;

        RandomSource randomSource = context.getRandom();
        Optional<HolderSet.Named<Enchantment>> enchantmentSet = Optional.of(context.getLevel().holderLookup(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));

        enchantmentSet.ifPresent(set -> {
            Optional<Holder<Enchantment>> selectedHolder = set.getRandomElement(randomSource);
            if (selectedHolder.isPresent())
                cir.setReturnValue(EnchantRandomlyFunction.enchantItem(stack, selectedHolder.get(), randomSource));
        });
    }
}