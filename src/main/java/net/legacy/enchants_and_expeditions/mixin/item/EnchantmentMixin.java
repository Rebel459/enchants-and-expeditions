package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Shadow public abstract int getMaxLevel();

    @Shadow
    @Final
    private Enchantment.EnchantmentDefinition definition;

    @Shadow
    @Final
    private Component description;

    @Shadow
    public abstract Component description();

    @Inject(method = "getFullname", at = @At("TAIL"), cancellable = true)
    private static void EaE$addBlessings(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<MutableComponent> cir) {
        if (!enchantment.is(EaEEnchantmentTags.BLESSING)) return;
        MutableComponent mutableComponent = enchantment.value().description().copy();
        ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ChatFormatting.GOLD));
        cir.setReturnValue(mutableComponent);
    }

    @Inject(method = "getFullname", at = @At("TAIL"), cancellable = true)
    private static void EaE$showLevel(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<MutableComponent> cir) {
        if (enchantment.value().getMaxLevel() == 1) return;
        MutableComponent mutableComponent = enchantment.value().description().copy();
        ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(cir.getReturnValue().getStyle().getColor()));
        mutableComponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));
        cir.setReturnValue(mutableComponent);
    }
}