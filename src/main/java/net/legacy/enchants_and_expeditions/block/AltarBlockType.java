package net.legacy.enchants_and_expeditions.block;

import net.minecraft.util.StringRepresentable;

public enum AltarBlockType implements StringRepresentable {
	EMPTY("empty"),
	MANA_TOME("mana"),
	FROST_TOME("frost"),
	SCORCH_TOME("scorch"),
	FLOW_TOME("flow"),
	CHAOS_TOME("chaos"),
	GREED_TOME("greed"),
	MIGHT_TOME("might"),
	STABILITY_TOME("stability"),
	POWER_TOME("power");

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