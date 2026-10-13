package net.rebel459.enchants_and_expeditions.registry;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.unified.api.core.UnifiedEvents;
import net.rebel459.unified.api.registry.CreativeModeTabIds;

public class EaECreativeInventorySorting {

	public static void init() {
        UnifiedEvents.CreativeEntries.modify((tab, context) -> {
            if (tab == CreativeModeTabIds.INGREDIENTS) {
                context.insertAfter(Items.HEART_OF_THE_SEA, EaEItems.ICE_SHARD);

                context.insertAfter(
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
            }

            if (tab == CreativeModeTabIds.FUNCTIONAL_BLOCKS) {
                context.insertAfter(Blocks.ENCHANTING_TABLE, EaEBlocks.ALTAR);

                context.insertAfter(Blocks.BOOKSHELF, EaEBlocks.ARCANE_BOOKSHELF, EaEBlocks.GLACIAL_BOOKSHELF, EaEBlocks.INFERNAL_BOOKSHELF);
            }
        });
    }
}