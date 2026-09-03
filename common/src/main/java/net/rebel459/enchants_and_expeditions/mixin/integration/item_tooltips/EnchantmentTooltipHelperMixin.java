package net.rebel459.enchants_and_expeditions.mixin.integration.item_tooltips;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.enchants_and_expeditions.client.EnchantingAttributesHelper;
import net.rebel459.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.item_tooltips.util.EnchantmentTooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnchantmentTooltipHelper.class, priority = 1001)
public abstract class EnchantmentTooltipHelperMixin {

    @Inject(method = "createName", at = @At(value = "TAIL"), cancellable = true)
    private static void enchantmentSymbols(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<MutableComponent> cir) {
        MutableComponent component = cir.getReturnValue();
        if (enchantment.is(EaEEnchantmentTags.BLESSING)) EnchantingAttributesHelper.addAttributeSymbol(component, "divinity");
        if (enchantment.is(EnchantmentTags.CURSE)) EnchantingAttributesHelper.addAttributeSymbol(component, "corruption");
        if (enchantment.is(EnchantmentTags.TREASURE)) EnchantingAttributesHelper.addAttributeSymbol(component, "treasure");
        if (EnchantingHelper.isPowerful(enchantment)) EnchantingAttributesHelper.addAttributeSymbol(component, "powerful");
        if (enchantment.is(EaEEnchantmentTags.GENERIC) || enchantment.is(EaEEnchantmentTags.GENERIC_BLESSING)) EnchantingAttributesHelper.addAttributeSymbol(component, "generic");
        if (!enchantment.is(EaEEnchantmentTags.GENERIC) && !enchantment.is(EaEEnchantmentTags.GENERIC_BLESSING) && !enchantment.is(EaEEnchantmentTags.GENERIC_TREASURE)) {
            if (enchantment.is(EaEEnchantmentTags.MANA) || enchantment.is(EaEEnchantmentTags.MANA_BLESSING) || enchantment.is(EaEEnchantmentTags.MANA_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "mana");
            if (enchantment.is(EaEEnchantmentTags.FROST) || enchantment.is(EaEEnchantmentTags.FROST_BLESSING) || enchantment.is(EaEEnchantmentTags.FROST_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "frost");
            if (enchantment.is(EaEEnchantmentTags.SCORCH) || enchantment.is(EaEEnchantmentTags.SCORCH_BLESSING) || enchantment.is(EaEEnchantmentTags.SCORCH_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "scorch");
            if (enchantment.is(EaEEnchantmentTags.FLOW) || enchantment.is(EaEEnchantmentTags.FLOW_BLESSING) || enchantment.is(EaEEnchantmentTags.FLOW_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "flow");
            if (enchantment.is(EaEEnchantmentTags.CHAOS) || enchantment.is(EaEEnchantmentTags.CHAOS_BLESSING) || enchantment.is(EaEEnchantmentTags.CHAOS_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "chaos");
            if (enchantment.is(EaEEnchantmentTags.GREED) || enchantment.is(EaEEnchantmentTags.GREED_BLESSING) || enchantment.is(EaEEnchantmentTags.GREED_TREASURE))
                EnchantingAttributesHelper.addAttributeSymbol(component, "greed");
            if (enchantment.is(EaEEnchantmentTags.MIGHT) || enchantment.is(EaEEnchantmentTags.MIGHT_BLESSING) || enchantment.is(EaEEnchantmentTags.MIGHT_TREASURE))
                EnchantingAttributesHelper. addAttributeSymbol(component, "might");
        }
        cir.setReturnValue(component);
    }
}
