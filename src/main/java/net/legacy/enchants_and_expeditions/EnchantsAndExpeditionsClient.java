package net.legacy.enchants_and_expeditions;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class EnchantsAndExpeditionsClient implements ClientModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static EnchantingAttributes.Attributes clientEnchantingAttributes;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                EnchantingAttributes.Attributes.ID,
                (payload, context) -> context.client().execute(() -> {
                    clientEnchantingAttributes = payload;
                    LOGGER.info("[EaE] S2C Attributes received on client thread: {}", payload);
                })
        );
    }

    public static EnchantingAttributes.Attributes getClientEnchantingAttributes() {
        return clientEnchantingAttributes;
    }
}