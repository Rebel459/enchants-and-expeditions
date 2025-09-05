package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.lib.CreativeTabs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

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
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.NATURAL_BLOCKS);
	}

	private static void addBeforeInBuildingBlocks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}

	private static void addAfterInBuildingBlocks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.BUILDING_BLOCKS);
	}

	private static void addAfterInRedstone(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addAfterInFunctionalBlocks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.FUNCTIONAL_BLOCKS);
	}

	private static void addBeforeInRedstoneBlocks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.REDSTONE_BLOCKS);
	}

	private static void addInToolsAndUtilities(ItemLike item) {
		CreativeTabs.add(item, CreativeModeTabs.TOOLS_AND_UTILITIES);
	}

	private static void addAfterInToolsAndUtilities(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.TOOLS_AND_UTILITIES);
	}

	private static void addBeforeInIngredients(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.INGREDIENTS);
	}

	private static void addAfterInIngredients(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.INGREDIENTS);
	}

	private static void addBeforeInFoodAndDrinks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.FOOD_AND_DRINKS);
	}

	private static void addAfterInFoodAndDrinks(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.FOOD_AND_DRINKS);
	}

	private static void addAfterInCombat(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.COMBAT);
	}

	private static void addBeforeInCombat(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.COMBAT);
	}

	private static void addBeforeInSpawnEggs(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addBefore(comparedItem, item, CreativeModeTabs.SPAWN_EGGS);
	}

	private static void addAfterInSpawnEggs(ItemLike comparedItem, ItemLike item) {
		CreativeTabs.addAfter(comparedItem, item, CreativeModeTabs.SPAWN_EGGS);
	}
}