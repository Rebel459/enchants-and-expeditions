package net.rebel459.enchants_and_expeditions.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.rebel459.enchants_and_expeditions.registry.EaEItems;

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

	AltarBlockType(final String name) {
		this.name = name;
	}

	private static Item getItem(AltarBlockType type) {
		return switch (type) {
			case EMPTY -> null;
			case MANA_TOME -> EaEItems.TOME_OF_MANA.get();
			case FROST_TOME -> EaEItems.TOME_OF_FROST.get();
			case SCORCH_TOME -> EaEItems.TOME_OF_SCORCH.get();
			case FLOW_TOME -> EaEItems.TOME_OF_FLOW.get();
			case CHAOS_TOME -> EaEItems.TOME_OF_CHAOS.get();
			case GREED_TOME -> EaEItems.TOME_OF_GREED.get();
			case MIGHT_TOME -> EaEItems.TOME_OF_MIGHT.get();
			case STABILITY_TOME -> EaEItems.TOME_OF_STABILITY.get();
			case POWER_TOME -> EaEItems.TOME_OF_POWER.get();
        };
	}

	public ItemStack getStack() {
		Item item = getItem(this);
		if (item == null) return ItemStack.EMPTY;
		return item.getDefaultInstance();
	}

	public static AltarBlockType getType(ItemStack stack) {
		AltarBlockType type = AltarBlockType.EMPTY;
		for (AltarBlockType value : AltarBlockType.values()) {
			if (stack.getItem() == getItem(value)) {
				type = value;
				break;
			}
		}
		return type;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}