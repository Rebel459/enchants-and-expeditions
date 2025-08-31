package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "getFullname", at = @At("TAIL"), cancellable = true)
    private static void addBlessings(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<MutableComponent> cir) {
        MutableComponent mutableComponent = enchantment.value().description().copy();
        if (enchantment.is(EaEEnchantmentTags.BLESSING)) {
            ComponentUtils.mergeStyles(mutableComponent, Style.EMPTY.withColor(ChatFormatting.GOLD));
            cir.setReturnValue(mutableComponent);
        }
    }
}