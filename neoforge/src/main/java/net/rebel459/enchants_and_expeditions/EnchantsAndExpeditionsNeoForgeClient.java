package net.rebel459.enchants_and_expeditions;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = EnchantsAndExpeditions.MOD_ID, dist = Dist.CLIENT)
public class EnchantsAndExpeditionsNeoForgeClient {

    public EnchantsAndExpeditionsNeoForgeClient(IEventBus modEventBus) {
        EnchantsAndExpeditionsClient.init();
    }
}