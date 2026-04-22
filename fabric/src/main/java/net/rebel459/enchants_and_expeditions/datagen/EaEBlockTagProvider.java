package net.rebel459.enchants_and_expeditions.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.rebel459.enchants_and_expeditions.registry.EaEBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class EaEBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public EaEBlockTagProvider(@NotNull FabricPackOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider arg) {
        this.valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(EaEBlocks.ARCANE_BOOKSHELF.get())
                .add(EaEBlocks.GLACIAL_BOOKSHELF.get())
                .add(EaEBlocks.INFERNAL_BOOKSHELF.get())
                .add(EaEBlocks.ALTAR.get());

        this.valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(EaEBlocks.ARCANE_BOOKSHELF.get())
                .add(EaEBlocks.GLACIAL_BOOKSHELF.get())
                .add(EaEBlocks.INFERNAL_BOOKSHELF.get());

        this.valueLookupBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .add(EaEBlocks.ALTAR.get())
                .add(EaEBlocks.ARCANE_BOOKSHELF.get())
                .add(EaEBlocks.GLACIAL_BOOKSHELF.get())
                .add(EaEBlocks.INFERNAL_BOOKSHELF.get());

        this.valueLookupBuilder(BlockTags.ICE)
                .add(EaEBlocks.GLACIAL_BOOKSHELF.get());
    }
}