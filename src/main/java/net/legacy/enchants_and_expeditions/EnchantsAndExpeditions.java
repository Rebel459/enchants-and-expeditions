package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.enchantment.EaEEnchantmentEffects;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.registry.EaEItemComponents;
import net.legacy.enchants_and_expeditions.registry.EaELootTables;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class EnchantsAndExpeditions implements ModInitializer {

	public static boolean isLegaciesAndLegendsLoaded = false;
	public static boolean isProgressionRebornLoaded = false;
	public static boolean isTrailierTalesLoaded = false;
	public static boolean isEnderscapeLoaded = false;

	@Override
	public void onInitialize() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("enchants_and_expeditions");

		EaELootTables.init();
		EaEEnchantments.init();
		EaEItemComponents.init();
		EaEEnchantmentEffects.register();
		EaEConfig.initClient();

		ResourceManagerHelper.registerBuiltinResourcePack(
				ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "rebalanced_vanilla_enchants"), (ModContainer)modContainer.get(),
				Component.translatable("pack.enchants_and_expeditions.rebalanced_vanilla_enchants"),
				ResourcePackActivationType.ALWAYS_ENABLED
		);

		if (FabricLoader.getInstance().isModLoaded("legacies_and_legends") && EaEConfig.get.integrations.legacies_and_legends_integration) {
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
		if (FabricLoader.getInstance().isModLoaded("trailiertales") && EaEConfig.get.integrations.trailier_tales_integration) {
			isTrailierTalesLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "trailier_tales_integration"), (ModContainer)modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.trailier_tales_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}
		if (FabricLoader.getInstance().isModLoaded("enderscape") && EaEConfig.get.integrations.enderscape_integration) {
			isEnderscapeLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "enderscape_integration"), (ModContainer)modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.enderscape_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static final String MOD_ID = "enchants_and_expeditions";
}