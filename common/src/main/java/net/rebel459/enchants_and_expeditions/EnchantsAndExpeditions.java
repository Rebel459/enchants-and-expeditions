package net.rebel459.enchants_and_expeditions;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.network.EnchantingAttributes;
import net.rebel459.enchants_and_expeditions.registry.*;
import net.rebel459.enchants_and_expeditions.sound.EaEBlockSounds;
import net.rebel459.enchants_and_expeditions.sound.EaESounds;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.unified.platform.UnifiedHelpers;
import net.rebel459.unified.platform.UnifiedPlatform;
import net.rebel459.unified.util.PackType;
import org.slf4j.Logger;

public class EnchantsAndExpeditions {

	public static final String MOD_ID = "enchants_and_expeditions";
	private static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

	public static boolean debug = false;

    public static boolean isLegaciesAndLegendsLoaded = false;
	public static boolean isProgressionRebornLoaded = false;
    public static boolean isCombatRebornLoaded = false;
	public static boolean isTrailierTalesLoaded = false;
	public static boolean isEnderscapeLoaded = false;

	static EnchantingAttributes.Attributes clientEnchantingAttributes;

	public static void initRegistries() {
		EaEBlocks.init();
		EaEItems.init();
		EaEMobEffects.init();
		EaESounds.init();
		EaEDataComponents.init();
		EnchantingHelper.init();
	}

	public static void init() {
		UnifiedHelpers.NETWORKING.registerPlayToServer(EnchantingAttributes.Request.ID, EnchantingAttributes.Request.CODEC, ((request, player) -> {
			String who = (player != null) ? player.getGameProfile().name() : "<null player>";
			if (debug) LOGGER.info("[EaE] C2S Request received from {}", who);

			if (player == null) {
				if (debug) LOGGER.warn("[EaE] Aborting: player is null");
				return;
			}
			if (!(player.containerMenu instanceof EnchantmentMenu menu)) {
				if (debug) LOGGER.warn("[EaE] Aborting: player {} is not in an EnchantmentMenu (got {})", who,
                        player.containerMenu.getClass().getName());
				return;
			}
			if (!(menu instanceof EnchantingAttributes duck)) {
				if (debug) LOGGER.warn("[EaE] Aborting: EnchantmentMenu does not implement EnchantingAttributes duck");
				return;
			}

			EnchantingAttributes.Attributes a = duck.calculateAttributes();
			if (debug) LOGGER.info("[EaE] Computed attributes for {} -> {}", who, a);
			UnifiedHelpers.NETWORKING.send(a, player);
			if (debug) LOGGER.info("[EaE] S2C Attributes sent to {}: {}", who, a);
		}));
		UnifiedHelpers.NETWORKING.registerPlayToClient(EnchantingAttributes.Attributes.ID, EnchantingAttributes.Attributes.CODEC, (attributes, player) -> {
			clientEnchantingAttributes = attributes;
			if (EnchantsAndExpeditions.debug) LOGGER.info("[EaE] S2C Attributes received on client thread: {}", attributes);
		});

        loadResources();
		
		EaECreativeInventorySorting.init();
		EaEBlockSounds.init();
		EaELootTables.init();
		EaEEnchantments.init();
	}

    public static void loadResources() {
        UnifiedHelpers.PACKS.add(EnchantsAndExpeditions.id("vanilla_tag_replacements"), PackType.REQUIRED_DATA);

        if (UnifiedPlatform.get().isModLoaded("combat_reborn") && EaEConfig.get().integrations.combat_reborn) {
			UnifiedHelpers.PACKS.add(EnchantsAndExpeditions.id("combat_reborn_integration"), PackType.REQUIRED_DATA);
        }

        if (UnifiedPlatform.get().isModLoaded("legacies_and_legends") && EaEConfig.get().integrations.legacies_and_legends) {
            isLegaciesAndLegendsLoaded = true;
			UnifiedHelpers.PACKS.add(EnchantsAndExpeditions.id("legacies_and_legends_integration"), PackType.REQUIRED_DATA);
        }
        if (UnifiedPlatform.get().isModLoaded("progression_reborn")) {
            isProgressionRebornLoaded = true;
        }
        if (UnifiedPlatform.get().isModLoaded("combat_reborn")) {
            isCombatRebornLoaded = true;
        }
        if (UnifiedPlatform.get().isModLoaded("trailiertales") && EaEConfig.get().integrations.trailier_tales) {
            isTrailierTalesLoaded = true;
			UnifiedHelpers.PACKS.add(EnchantsAndExpeditions.id("trailier_tales_integration"), PackType.REQUIRED_DATA);
        }
        if (UnifiedPlatform.get().isModLoaded("enderscape") && EaEConfig.get().integrations.enderscape) {
            isEnderscapeLoaded = true;
        }
    }
}