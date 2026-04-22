package net.rebel459.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;

public class EnchantsAndExpeditionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EnchantsAndExpeditions.initRegistries();
        EnchantsAndExpeditions.init();
    }
}
