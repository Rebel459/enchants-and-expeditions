package net.rebel459.enchants_and_expeditions.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.item_tooltips.util.ScreenHelper;

import java.util.Objects;

public class EnchantingAttributesHelper {
    public static final FontDescription ENCHANTING_ATTRIBUTE_FONT = new FontDescription.Resource(EnchantsAndExpeditions.id("enchanting_attributes"));

    public static final int ORANGE = 14639398;

    public static boolean shouldShowAttributeSymbols() {
        EaEConfig.GeneralConfig general = EaEConfig.get().general;
        return general.enchanting_attribute_tooltip != EaEConfig.EnchantingAttributeTooltip.NEVER && (general.enchanting_attribute_tooltip != EaEConfig.EnchantingAttributeTooltip.HOLD_KEY || ScreenHelper.hasKeyDown());
    }

    public static MutableComponent addAttributeSymbol(String attribute) {
        return addAttributeSymbol(Component.literal(""), attribute, false);
    }
    public static MutableComponent addAttributeSymbol(MutableComponent component, String attribute) {
        return addAttributeSymbol(component, attribute, false);
    }
    public static MutableComponent addAttributeSymbol(String attribute, boolean alwaysShow) {
        return addAttributeSymbol(Component.literal(""), attribute, alwaysShow);
    }
    public static MutableComponent addAttributeSymbol(MutableComponent component, String attribute, boolean alwaysShow) {
        if (!shouldShowAttributeSymbols() && !alwaysShow) return component;
        if (Objects.equals(attribute, "powerful")) {
            return component.append(Component.literal(" +").withStyle(ChatFormatting.BLUE));
        }
        if (Objects.equals(attribute, "generic")) {
            return component.append(Component.literal(" ◇").withStyle(ChatFormatting.GRAY));
        }
        int id = switch (attribute) {
            case "mana" -> 0;
            case "frost" -> 1;
            case "scorch" -> 2;
            case "flow" -> 3;
            case "chaos" -> 4;
            case "greed" -> 5;
            case "might" -> 6;
            case "corruption" -> 7;
            case "divinity" -> 8;
            case "treasure" -> 9;
            default -> -1;
        };
        if (id == -1) return component;

        component.append(Component.literal(String.valueOf(id)).withStyle(style -> style.withFont(ENCHANTING_ATTRIBUTE_FONT).withColor(0xFFFFFF)));
        return component;
    }
}
