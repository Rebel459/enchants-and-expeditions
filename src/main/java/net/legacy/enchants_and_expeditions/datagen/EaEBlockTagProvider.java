package net.legacy.enchants_and_expeditions.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class EaEBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public EaEBlockTagProvider(@NotNull FabricDataOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(EaEBlocks.ARCANE_BOOKSHELF)
                .add(EaEBlocks.GLACIAL_BOOKSHELF)
                .add(EaEBlocks.INFERNAL_BOOKSHELF)
                .add(EaEBlocks.ALTAR);

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(EaEBlocks.ARCANE_BOOKSHELF)
                .add(EaEBlocks.GLACIAL_BOOKSHELF)
                .add(EaEBlocks.INFERNAL_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .add(EaEBlocks.ALTAR)
                .add(EaEBlocks.ARCANE_BOOKSHELF)
                .add(EaEBlocks.GLACIAL_BOOKSHELF)
                .add(EaEBlocks.INFERNAL_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.ICE)
                .add(EaEBlocks.GLACIAL_BOOKSHELF);
    }
}