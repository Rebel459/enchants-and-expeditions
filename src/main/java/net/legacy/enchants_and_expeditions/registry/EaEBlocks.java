package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.block.AltarBlock;
import net.legacy.enchants_and_expeditions.sound.EaEBlockSounds;
import net.legacy.enchants_and_expeditions.util.CreativeTabs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class EaEBlocks {
    public static final Block ARCANE_BOOKSHELF = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.ARCANE_BOOKSHELF)
    );
    public static final Block GLACIAL_BOOKSHELF = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.ICE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.GLACIAL_BOOKSHELF)
                    .friction(0.98F)
                    .ignitedByLava()
    );
    public static final Block INFERNAL_BOOKSHELF = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.INFERNAL_BOOKSHELF)
    );

    public static final AltarBlock ALTAR = new AltarBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3.0F)
                    .sound(SoundType.DEEPSLATE_TILES)
                    .noOcclusion()
                    .isViewBlocking(Blocks::never)
    );

    public static void init() {
        registerBlockAfter(Blocks.BOOKSHELF, "arcane_bookshelf", ARCANE_BOOKSHELF, CreativeModeTabs.FUNCTIONAL_BLOCKS);
        registerBlockAfter(EaEBlocks.ARCANE_BOOKSHELF, "glacial_bookshelf", GLACIAL_BOOKSHELF, CreativeModeTabs.FUNCTIONAL_BLOCKS);
        registerBlockAfter(EaEBlocks.GLACIAL_BOOKSHELF, "infernal_bookshelf", INFERNAL_BOOKSHELF, CreativeModeTabs.FUNCTIONAL_BLOCKS);
        registerBlockAfter(Blocks.ENCHANTING_TABLE, "altar", ALTAR, CreativeModeTabs.FUNCTIONAL_BLOCKS);
    }

    @SafeVarargs
    private static void registerBlockAfter(ItemLike comparedItem, String path, Block block, ResourceKey<CreativeModeTab>... tabs) {
        registerBlockItemAfter(comparedItem, path, block, tabs);
        actualRegisterBlock(path, block);
    }

    @SafeVarargs
    private static void registerBlockItemAfter(ItemLike comparedItem, String name, Block block, ResourceKey<CreativeModeTab>... tabs) {
        registerBlockItemAfter(comparedItem, name, block, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    @SafeVarargs
    private static void registerBlockItemAfter(ItemLike comparedItem, String path, Block block, CreativeModeTab.TabVisibility visibility, ResourceKey<CreativeModeTab>... tabs) {
        actualRegisterBlockItem(path, block);
        CreativeTabs.addAfter(comparedItem, block, visibility, tabs);
    }

    private static void actualRegisterBlock(String path, Block block) {
        if (BuiltInRegistries.BLOCK.getOptional(EnchantsAndExpeditions.id(path)).isEmpty()) {
            Registry.register(BuiltInRegistries.BLOCK, EnchantsAndExpeditions.id(path), block);
        }
    }

    private static void actualRegisterBlockItem(String path, Block block) {
        if (BuiltInRegistries.ITEM.getOptional(EnchantsAndExpeditions.id(path)).isEmpty()) {
            Registry.register(BuiltInRegistries.ITEM, EnchantsAndExpeditions.id(path), new BlockItem(block, new Item.Properties()));
        }
    }
}