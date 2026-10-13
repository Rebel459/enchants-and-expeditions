package net.rebel459.enchants_and_expeditions;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rebel459.unified.neoforge.NeoForgeUnifiedBus;

@Mod(EnchantsAndExpeditions.MOD_ID)
public class EnchantsAndExpeditionsNeoForge {

    public EnchantsAndExpeditionsNeoForge(IEventBus modEventBus) {
        NeoForgeUnifiedBus.register(EnchantsAndExpeditions.MOD_ID, modEventBus);
        EnchantsAndExpeditions.initRegistries();
        modEventBus.addListener(EnchantsAndExpeditionsNeoForge::commonSetup);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        EnchantsAndExpeditions.init();
    }
}