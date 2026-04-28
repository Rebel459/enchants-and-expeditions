package net.rebel459.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(CreativeModeTabs.class)
public abstract class CreativeModeTabsMixin {

    @WrapOperation(method = "generateEnchantmentBookTypesOnlyMaxLevel", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    private static Stream<ItemStack> hideDisabledMaxEnchantmentBooks(Stream<Holder.Reference<Enchantment>> instance, Function<? super Holder.Reference<Enchantment>, ? extends ItemStack> function, Operation<Stream<ItemStack>> original) {
        List<Holder.Reference<Enchantment>> enchantments = new ArrayList<>(instance.toList());
        enchantments.removeIf(EnchantingHelper::configureEnchantments);
        return original.call(enchantments.stream(), function);
    }

    @WrapOperation(method = "generateEnchantmentBookTypesAllLevels", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;flatMap(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    private static Stream<ItemStack> hideDisabledEnchantmentBooks(Stream<Holder.Reference<Enchantment>> instance, Function<? super Holder.Reference<Enchantment>, ? extends Stream<? extends ItemStack>> function, Operation<Stream<ItemStack>> original) {        List<Holder.Reference<Enchantment>> enchantments = new ArrayList<>(instance.toList());
        enchantments.removeIf(EnchantingHelper::configureEnchantments);
        return original.call(enchantments.stream(), function);
    }
}