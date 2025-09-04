package net.legacy.enchants_and_expeditions.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.legacy.enchants_and_expeditions.registry.EaEItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import org.jetbrains.annotations.NotNull;

public final class EaEModelProvider extends FabricModelProvider {

	public EaEModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(@NotNull BlockModelGenerators generator) {
		generator.createTrivialCube(EaEBlocks.ARCANE_BOOKSHELF);
		generator.createTrivialCube(EaEBlocks.GLACIAL_BOOKSHELF);
		generator.createTrivialCube(EaEBlocks.INFERNAL_BOOKSHELF);
	}

	@Override
	public void generateItemModels(@NotNull ItemModelGenerators generator) {
		generator.generateFlatItem(EaEItems.TOME_OF_MANA, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_FROST, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_SCORCH, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_FLOW, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_CHAOS, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_GREED, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_MIGHT, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_STABILITY, ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_POWER, ModelTemplates.FLAT_ITEM);
	}
}