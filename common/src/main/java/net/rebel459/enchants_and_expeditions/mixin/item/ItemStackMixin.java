package net.rebel459.enchants_and_expeditions.mixin.item;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.util.EnchantmentSlots;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Shadow public abstract ItemEnchantments getEnchantments();

    @Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
    private void canEnchant(CallbackInfoReturnable<Boolean> cir) {
        if (!EaEConfig.get().general.repeat_table_enchanting) return;
        ItemStack stack = ItemStack.class.cast(this);
        if (!cir.getReturnValue() && stack.isEnchanted() && EnchantingHelper.hasSlots(stack) && (stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(stack) > 0) || EnchantingHelper.getBlessings(stack) == 0 || !EnchantingHelper.allMaxLevel(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;", at = @At("HEAD"))
    private <T> void EaE$arcanaBlessing(DataComponentType<T> type, @Nullable T value, CallbackInfoReturnable<T> cir) {
        if (type != DataComponents.ENCHANTMENTS || !(value instanceof ItemEnchantments newEnchantments)) return;
        ItemStack stack = ItemStack.class.cast(this);
        boolean hadArcana = EnchantingHelper.hasEnchantment(stack, EaEEnchantments.ARCANA_BLESSING);
        boolean hasArcana = false;
        for (Holder<Enchantment> enchantment : newEnchantments.keySet()) {
            if (enchantment.is(EaEEnchantments.ARCANA_BLESSING)) {
                hasArcana = true;
                break;
            }
        }
        if (EnchantingHelper.hasSlots(stack)) {
            EnchantmentSlots slots = stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get());
            if (!hadArcana && hasArcana) {
                slots = slots.setModifier(slots.modifier() + 1);
            } else if (hadArcana && !hasArcana) {
                slots = slots.setModifier(slots.modifier() - 1);
            }
            stack.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), slots);
        }
    }
}