package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantments {
	public static final ResourceKey<Enchantment> ARCANE_PROTECTION = key("arcane_protection");
	public static final ResourceKey<Enchantment> ELEMENTAL_PROTECTION = key("elemental_protection");
	public static final ResourceKey<Enchantment> PHYSICAL_PROTECTION = key("physical_protection");
	public static final ResourceKey<Enchantment> CHILLED = key("chilled");
	public static final ResourceKey<Enchantment> SMITING = key("smiting");

	public static final ResourceKey<Enchantment> TANGLED = keyLegaciesAndLegends("tangled");

	public static final ResourceKey<Enchantment> REFORGE = keyProgressionReborn("reforge");

	public static void init() {
	}

	private static @NotNull ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, path));
	}

	private static @NotNull ResourceKey<Enchantment> keyLegaciesAndLegends(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("legacies_and_legends", path));
	}

	private static @NotNull ResourceKey<Enchantment> keyProgressionReborn(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("progression_reborn", path));
	}
}
