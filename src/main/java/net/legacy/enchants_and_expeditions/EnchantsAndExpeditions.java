package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.enchantment.EaEEnchantmentEffects;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.registry.EaELootTables;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class EnchantsAndExpeditions implements ModInitializer {

	public static boolean isLegaciesAndLegendsLoaded = false;
	public static boolean isProgressionRebornLoaded = false;

	@Override
	public void onInitialize() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("enchants_and_expeditions");

		EaELootTables.init();
		EaEEnchantments.init();
		EaEEnchantmentEffects.register();
		EaEConfig.initClient();

		ResourceManagerHelper.registerBuiltinResourcePack(
				ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "rebalanced_vanilla_enchants"), (ModContainer)modContainer.get(),
				Component.translatable("pack.enchants_and_expeditions.rebalanced_vanilla_enchants"),
				ResourcePackActivationType.ALWAYS_ENABLED
		);

		if (FabricLoader.getInstance().isModLoaded("legacies_and_legends")) {
			isLegaciesAndLegendsLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "legacies_and_legends_integration"), (ModContainer)modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.legacies_and_legends_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}
		if (FabricLoader.getInstance().isModLoaded("progression_reborn")) {
			isProgressionRebornLoaded = true;
		}

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static final String MOD_ID = "enchants_and_expeditions";
}