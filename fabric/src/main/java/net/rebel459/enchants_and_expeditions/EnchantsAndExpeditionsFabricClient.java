package net.rebel459.enchants_and_expeditions;

import net.fabricmc.api.ClientModInitializer;

public class EnchantsAndExpeditionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EnchantsAndExpeditionsClient.init();
    }
}
