package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

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