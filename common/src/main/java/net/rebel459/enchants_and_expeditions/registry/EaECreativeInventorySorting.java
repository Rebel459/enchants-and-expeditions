package net.rebel459.enchants_and_expeditions.registry;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.unified.platform.UnifiedHelpers;
import net.rebel459.unified.util.CreativeModeTabs;

public class EaECreativeInventorySorting {

	public static void init() {
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.INGREDIENTS, Items.HEART_OF_THE_SEA, EaEItems.ICE_SHARD);

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(
                CreativeModeTabs.INGREDIENTS,
                Items.OMINOUS_TRIAL_KEY,
                EaEItems.TOME_OF_MANA,
                EaEItems.TOME_OF_FROST,
                EaEItems.TOME_OF_SCORCH,
                EaEItems.TOME_OF_FLOW,
                EaEItems.TOME_OF_CHAOS,
                EaEItems.TOME_OF_GREED,
                EaEItems.TOME_OF_MIGHT,
                EaEItems.TOME_OF_STABILITY,
                EaEItems.TOME_OF_POWER
        );

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.ENCHANTING_TABLE, EaEBlocks.ALTAR);

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.BOOKSHELF, EaEBlocks.ARCANE_BOOKSHELF, EaEBlocks.GLACIAL_BOOKSHELF, EaEBlocks.INFERNAL_BOOKSHELF);
	}
}