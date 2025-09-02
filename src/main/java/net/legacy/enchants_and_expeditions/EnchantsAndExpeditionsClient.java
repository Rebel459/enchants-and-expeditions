package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;

@Environment(EnvType.CLIENT)
public final class EnchantsAndExpeditionsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(EnchantingAttributes.Attributes.ID,
                (payload, context) -> context.client().execute(() -> {
                    clientEnchantingAttributes = new EnchantingAttributes.Attributes(payload.mana(), payload.frost(), payload.scorch(), payload.flow(), payload.chaos(), payload.greed(), payload.might(), payload.stability(), payload.divinity());
                }));
    }

    public static EnchantingAttributes.Attributes getClientEnchantingAttributes() {
        return clientEnchantingAttributes;
    }

    private static EnchantingAttributes.Attributes clientEnchantingAttributes;
}