package net.rebel459.enchants_and_expeditions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.network.EnchantingAttributes;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.util.EnchantmentSlots;
import net.rebel459.item_tooltips.util.ScreenHelper;
import net.rebel459.unified.api.client.core.UnifiedClientEvents;
import net.rebel459.unified.api.client.core.UnifiedClientHelpers;
import net.rebel459.unified.api.event.EventTiming;

public final class EnchantsAndExpeditionsClient {

    public static void init() {
        if (EaEConfig.get().integrations.item_tooltips_overrides) {
            UnifiedClientHelpers.RESOURCE_PACKS.addRequired(EnchantsAndExpeditions.id("item_tooltips_overrides"));
        }

        UnifiedClientEvents.ItemTooltips.addAttributes(EventTiming.POST, ((stack, consumer, _, _) -> {
            EaEConfig.TooltipConfig tooltips = EaEConfig.get().tooltips;
            if (tooltips.slot_tooltip == EaEConfig.TooltipType.NEVER || (tooltips.slot_tooltip == EaEConfig.TooltipType.HOLD_KEY && !ScreenHelper.hasKeyDown())) return;
            if (EnchantingHelper.hasSlots(stack) && !stack.has(DataComponents.STORED_ENCHANTMENTS) && stack.getItem() != Items.BOOK) {
                EnchantmentSlots slots = EnchantingHelper.getImmutableSlots(stack);
                if (slots.getTotal() == 0) return;
                ChatFormatting formatting = ChatFormatting.GRAY;
                if (slots.modifier() > 0) formatting = ChatFormatting.BLUE;
                else if (slots.modifier() < 0) formatting = ChatFormatting.RED;
                consumer.accept(
                        Component.literal("")
                                .append(Component.translatable("tooltip.enchants_and_expeditions.slots_used").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal(": " + (slots.getTotal() - slots.getRemaining(stack)) + " / ").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal(String.valueOf(slots.getTotal())).withStyle(formatting))
                );
            }
        }));
    }

    public static EnchantingAttributes.Attributes getClientEnchantingAttributes() {
        return EnchantsAndExpeditions.clientEnchantingAttributes;
    }
}