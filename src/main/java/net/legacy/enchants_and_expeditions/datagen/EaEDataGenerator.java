package net.legacy.enchants_and_expeditions.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.NotNull;

public final class EaEDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(@NotNull FabricDataGenerator dataGenerator) {
		final FabricDataGenerator.Pack pack = dataGenerator.createPack();

		pack.addProvider(EaEModelProvider::new);
		pack.addProvider(EaEBlockTagProvider::new);
	}
}