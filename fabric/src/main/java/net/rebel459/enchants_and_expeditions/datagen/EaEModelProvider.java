package net.rebel459.enchants_and_expeditions.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.rebel459.enchants_and_expeditions.registry.EaEItems;
import org.jetbrains.annotations.NotNull;

public final class EaEModelProvider extends FabricModelProvider {

	public EaEModelProvider(FabricPackOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(@NotNull BlockModelGenerators generator) {}

	@Override
	public void generateItemModels(@NotNull ItemModelGenerators generator) {
		generator.generateFlatItem(EaEItems.ICE_SHARD.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_MANA.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_FROST.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_SCORCH.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_FLOW.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_CHAOS.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_GREED.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_MIGHT.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_STABILITY.get(), ModelTemplates.FLAT_ITEM);
		generator.generateFlatItem(EaEItems.TOME_OF_POWER.get(), ModelTemplates.FLAT_ITEM);
	}
}