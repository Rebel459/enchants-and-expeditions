package net.legacy.enchants_and_expeditions;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.enchantment.EaEEnchantmentEffects;
import net.legacy.enchants_and_expeditions.registry.*;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;
import net.legacy.enchants_and_expeditions.sound.EaEBlockSounds;
import net.legacy.enchants_and_expeditions.sound.EaESounds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.slf4j.Logger;

import java.util.Optional;

public class EnchantsAndExpeditions implements ModInitializer {

	public static final String MOD_ID = "enchants_and_expeditions";
	private static final Logger LOGGER = LogUtils.getLogger();

	public static boolean debug = false;

	public static boolean isLegaciesAndLegendsLoaded = false;
	public static boolean isProgressionRebornLoaded = false;
	public static boolean isTrailierTalesLoaded = false;
	public static boolean isEnderscapeLoaded = false;

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playC2S().register(EnchantingAttributes.Request.ID, EnchantingAttributes.Request.CODEC);
		PayloadTypeRegistry.playS2C().register(EnchantingAttributes.Attributes.ID, EnchantingAttributes.Attributes.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(
				EnchantingAttributes.Request.ID,
				(payload, context) -> context.server().execute(() -> {
					ServerPlayer player = context.player();
					String who = (player != null) ? player.getGameProfile().getName() : "<null player>";
					if (debug) LOGGER.info("[EaE] C2S Request received from {}", who);

					if (player == null) {
						if (debug) LOGGER.warn("[EaE] Aborting: player is null");
						return;
					}
					if (!(player.containerMenu instanceof EnchantmentMenu menu)) {
						if (debug) LOGGER.warn("[EaE] Aborting: player {} is not in an EnchantmentMenu (got {})", who,
								(player.containerMenu != null ? player.containerMenu.getClass().getName() : "<null>"));
						return;
					}
					if (!(menu instanceof EnchantingAttributes duck)) {
						if (debug) LOGGER.warn("[EaE] Aborting: EnchantmentMenu does not implement EnchantingAttributes duck");
						return;
					}

					EnchantingAttributes.Attributes a = duck.calculateAttributes();
					if (debug) LOGGER.info("[EaE] Computed attributes for {} -> {}", who, a);
					ServerPlayNetworking.send(player, a);
					if (debug) LOGGER.info("[EaE] S2C Attributes sent to {}: {}", who, a);
				})
		);

		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("enchants_and_expeditions");

		EaEBlocks.init();
		EaEItems.init();
		EaECreativeInventorySorting.init();
		EaESounds.init();
		EaEBlockSounds.init();
		EaELootTables.init();
		EaEEnchantments.init();
		EaEDataComponents.init();
		EaEEnchantmentEffects.register();
		EaEConfig.initClient();

		ResourceManagerHelper.registerBuiltinResourcePack(
				ResourceLocation.fromNamespaceAndPath(MOD_ID, "vanilla_tag_replacements"),
				modContainer.get(),
				Component.translatable("pack.enchants_and_expeditions.vanilla_tag_replacements"),
				ResourcePackActivationType.ALWAYS_ENABLED
		);
		if (EaEConfig.get.integrations.item_tooltips_overrides) {
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_tooltips_overrides"),
					modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.item_tooltips_overrides"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}

		if (FabricLoader.getInstance().isModLoaded("legacies_and_legends") && EaEConfig.get.integrations.legacies_and_legends) {
			isLegaciesAndLegendsLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(MOD_ID, "legacies_and_legends_integration"),
					modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.legacies_and_legends_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}
		if (FabricLoader.getInstance().isModLoaded("progression_reborn")) {
			isProgressionRebornLoaded = true;
		}
		if (FabricLoader.getInstance().isModLoaded("trailiertales") && EaEConfig.get.integrations.trailier_tales) {
			isTrailierTalesLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(MOD_ID, "trailier_tales_integration"),
					modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.trailier_tales_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}
		if (FabricLoader.getInstance().isModLoaded("enderscape") && EaEConfig.get.integrations.enderscape) {
			isEnderscapeLoaded = true;
			ResourceManagerHelper.registerBuiltinResourcePack(
					ResourceLocation.fromNamespaceAndPath(MOD_ID, "enderscape_integration"),
					modContainer.get(),
					Component.translatable("pack.enchants_and_expeditions.enderscape_integration"),
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		}
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}