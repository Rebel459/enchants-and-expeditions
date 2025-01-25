package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class EnchantsAndExpeditions implements ModInitializer {
	@Override
	public void onInitialize() {
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath("anvil_rebalance", path);
	}
}