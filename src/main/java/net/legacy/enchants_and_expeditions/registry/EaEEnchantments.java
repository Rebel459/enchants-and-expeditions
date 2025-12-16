package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantments {

	// Core
	public static final ResourceKey<Enchantment> ARCANE_PROTECTION = key("arcane_protection");
	public static final ResourceKey<Enchantment> ELEMENTAL_PROTECTION = key("elemental_protection");
	public static final ResourceKey<Enchantment> PHYSICAL_PROTECTION = key("physical_protection");
	public static final ResourceKey<Enchantment> CHILLED = key("chilled");
	public static final ResourceKey<Enchantment> SMITING = key("smiting");

	// Blessing
	public static final ResourceKey<Enchantment> BOUNDING_BLESSING = key("bounding_blessing");
	public static final ResourceKey<Enchantment> INFERNO_BLESSING = key("inferno_blessing");
	public static final ResourceKey<Enchantment> VENGEANCE_BLESSING = key("vengeance_blessing");
	public static final ResourceKey<Enchantment> TEMPERING_BLESSING = key("tempering_blessing");
    public static final ResourceKey<Enchantment> FLUIDITY_BLESSING = key("fluidity_blessing");
    public static final ResourceKey<Enchantment> CONDUCTIVITY_BLESSING = key("conductivity_blessing");

	// Normal
	public static final ResourceKey<Enchantment> EXTRACTION = key("extraction");
	public static final ResourceKey<Enchantment> BLOODLUST = key("bloodlust");
	public static final ResourceKey<Enchantment> ENTROPY = key("entropy");
	public static final ResourceKey<Enchantment> EQUESTRIAN = key("equestrian");
	public static final ResourceKey<Enchantment> RECOVERY = key("recovery");
	public static final ResourceKey<Enchantment> ICEBOUND = key("icebound");
	public static final ResourceKey<Enchantment> BLAZING = key("blazing");
	public static final ResourceKey<Enchantment> FROSTBITE = key("frostbite");
	public static final ResourceKey<Enchantment> QUICKSTEP = key("quickstep");
	public static final ResourceKey<Enchantment> GALLOPING = key("galloping");
	public static final ResourceKey<Enchantment> LEAPING = key("leaping");
	public static final ResourceKey<Enchantment> FEROCITY = key("ferocity");
	public static final ResourceKey<Enchantment> VELOCITY = key("velocity");
    public static final ResourceKey<Enchantment> JOUSTING = key("jousting");
    public static final ResourceKey<Enchantment> SLIPSTREAM = key("slipstream");

	// Curse
	public static final ResourceKey<Enchantment> FRAGILITY_CURSE = key("fragility_curse");
	public static final ResourceKey<Enchantment> FALTERING_CURSE = key("faltering_curse");
	public static final ResourceKey<Enchantment> SHATTERING_CURSE = key("shattering_curse");
	public static final ResourceKey<Enchantment> DISPLACEMENT_CURSE = key("displacement_curse");
	public static final ResourceKey<Enchantment> SLIDING_CURSE = key("sliding_curse");

	public static void init() {
	}

	private static @NotNull ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, EnchantsAndExpeditions.id(path));
	}
}
