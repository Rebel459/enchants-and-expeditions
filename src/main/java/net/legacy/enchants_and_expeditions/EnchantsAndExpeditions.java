package net.legacy.enchants_and_expeditions;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Optional;

public class EnchantsAndExpeditions implements ModInitializer {
	@Override
	public void onInitialize() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("enchants_and_expeditions");

		try {
			EaEConfig.main();
		} catch (IOException var3) {
			IOException e = var3;
			throw new RuntimeException(e);
		}

		EaELootTables.init();
        EaEEnchantmentEffects.register();

		if (FabricLoader.getInstance().isModLoaded("legacies_and_legends") && EaEConfig.mod_integration_datapacks) {
			ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.fromNamespaceAndPath("enchants_and_expeditions", "enchants_and_expeditions_legacies_and_legends_integration"), (ModContainer)modContainer.get(), Component.translatable("pack.enchants_and_expeditions.legacies_and_legends_integration"), ResourcePackActivationType.ALWAYS_ENABLED);
		}

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static final String MOD_ID = "enchants_and_expeditions";
}