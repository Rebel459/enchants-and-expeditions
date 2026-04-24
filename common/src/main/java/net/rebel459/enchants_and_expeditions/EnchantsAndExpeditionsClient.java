package net.rebel459.enchants_and_expeditions;

import net.minecraft.network.chat.Component;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.network.EnchantingAttributes;
import net.rebel459.unified.platform.UnifiedHelpers;
import net.rebel459.unified.util.PackType;

public final class EnchantsAndExpeditionsClient {

    public static void init() {
        if (EaEConfig.get().integrations.item_tooltips_overrides) {
            UnifiedHelpers.PACKS.add(EnchantsAndExpeditions.id("item_tooltips_overrides"), PackType.REQUIRED_RESOURCES);
        }
    }

    public static EnchantingAttributes.Attributes getClientEnchantingAttributes() {
        return EnchantsAndExpeditions.clientEnchantingAttributes;
    }
}