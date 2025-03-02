package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Optional;

public class EnchantsAndExpeditions implements ModInitializer {
	@Override
	public void onInitialize() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("enchants_and_expeditions");

		EaELootTables.init();
        EaEConfig.initClient();

		ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "rebalanced_vanilla_enchants"), (ModContainer)modContainer.get(), Component.translatable("pack.enchants_and_expeditions.rebalanced_vanilla_enchants"), ResourcePackActivationType.ALWAYS_ENABLED);

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static final String MOD_ID = "enchants_and_expeditions";
}