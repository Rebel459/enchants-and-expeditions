package net.rebel459.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.rebel459.unified.fabric.FabricUnifiedInitializer;

public class EnchantsAndExpeditionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EnchantsAndExpeditions.initRegistries();
        FabricUnifiedInitializer.register(EnchantsAndExpeditions::init);
    }
}
