package net.legacy.enchants_and_expeditions.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EaECreativeInventorySorting {

	public static void init() {
		addAfterInIngredients(Items.HEART_OF_THE_SEA, EaEItems.ICE_SHARD);

		addAfterInIngredients(Items.OMINOUS_TRIAL_KEY, EaEItems.TOME_OF_MANA);
		addAfterInIngredients(EaEItems.TOME_OF_MANA, EaEItems.TOME_OF_FROST);
		addAfterInIngredients(EaEItems.TOME_OF_FROST, EaEItems.TOME_OF_SCORCH);
		addAfterInIngredients(EaEItems.TOME_OF_SCORCH, EaEItems.TOME_OF_FLOW);
		addAfterInIngredients(EaEItems.TOME_OF_FLOW, EaEItems.TOME_OF_CHAOS);
		addAfterInIngredients(EaEItems.TOME_OF_CHAOS, EaEItems.TOME_OF_GREED);
		addAfterInIngredients(EaEItems.TOME_OF_GREED, EaEItems.TOME_OF_MIGHT);
		addAfterInIngredients(EaEItems.TOME_OF_MIGHT, EaEItems.TOME_OF_STABILITY);
		addAfterInIngredients(EaEItems.TOME_OF_STABILITY, EaEItems.TOME_OF_POWER);

		addAfterInFunctionalBlocks(Blocks.ENCHANTING_TABLE, EaEBlocks.ALTAR);

		addAfterInFunctionalBlocks(Blocks.BOOKSHELF, EaEBlocks.ARCANE_BOOKSHELF);
		addAfterInFunctionalBlocks(EaEBlocks.ARCANE_BOOKSHELF, EaEBlocks.GLACIAL_BOOKSHELF);
		addAfterInFunctionalBlocks(EaEBlocks.GLACIAL_BOOKSHELF, EaEBlocks.INFERNAL_BOOKSHELF);
	}

	private static void addAfterInNaturalBlocks(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.NATURAL_BLOCKS);
	}

	private static void addBeforeInBuildingBlocks(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}

	private static void addAfterInBuildingBlocks(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}

	private static void addAfterInRedstone(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addAfterInFunctionalBlocks(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.FUNCTIONAL_BLOCKS);
	}

	private static void addBeforeInRedstoneBlocks(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addInToolsAndUtilities(ItemLike item) {
		add(item, CreativeModeTabs.TOOLS_AND_UTILITIES);
	}

	private static void addAfterInToolsAndUtilities(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.TOOLS_AND_UTILITIES);
	}

	private static void addBeforeInIngredients(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.INGREDIENTS);
	}

	private static void addAfterInIngredients(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.INGREDIENTS);
	}

	private static void addBeforeInFoodAndDrinks(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.FOOD_AND_DRINKS);
	}

	private static void addAfterInFoodAndDrinks(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.FOOD_AND_DRINKS);
	}

	private static void addAfterInCombat(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.COMBAT);
	}

	private static void addBeforeInCombat(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.COMBAT);
	}

	private static void addBeforeInSpawnEggs(ItemLike comparedItem, ItemLike item) {
		addBefore(comparedItem, item, CreativeModeTabs.SPAWN_EGGS);
	}

	private static void addAfterInSpawnEggs(ItemLike comparedItem, ItemLike item) {
		addAfter(comparedItem, item, CreativeModeTabs.SPAWN_EGGS);
	}

    public static void add(ItemLike item, ResourceKey<CreativeModeTab> @NotNull ... tabs) {
        if (item == null) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                var stack = new ItemStack(item);
                stack.setCount(1);
                entries.accept(stack);
            });
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(ItemLike comparedItem, ItemLike item, ResourceKey<CreativeModeTab>... tabs) {
        addBefore(comparedItem, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(
            ItemLike comparedItem,
            ItemLike item,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> entries.addBefore(comparedItem, list, tabVisibility));
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(
            ItemLike comparedItem,
            ItemLike item,
            String path,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null ) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> {
                entries.addBefore(comparedItem, list, tabVisibility);
            });
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            ResourceKey<CreativeModeTab>... tabs
    ) {
        addAfter(comparedItem, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) {
            return;
        } else {
            item.asItem();
        }
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> entries.addAfter(comparedItem, list, tabVisibility));
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            String path,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) {
            return;
        } else {
            item.asItem();
        }
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> {
                entries.addAfter(comparedItem, list, tabVisibility);
            });
        }
    }
}