package net.legacy.enchants_and_expeditions.block;

import net.minecraft.util.StringRepresentable;

public enum AltarBlockType implements StringRepresentable {
	EMPTY("empty"),
	MANA_TOME("mana_tome"),
	FROST_TOME("frost_tome"),
	SCORCH_TOME("scorch_tome"),
	FLOW_TOME("flow_tome"),
	CHAOS_TOME("chaos_tome"),
	GREED_TOME("greed_tome"),
	MIGHT_TOME("might_tome"),
	STABILITY_TOME("stability_tome"),
	POWER_TOME("power_tome");

	private final String name;

	private AltarBlockType(final String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}