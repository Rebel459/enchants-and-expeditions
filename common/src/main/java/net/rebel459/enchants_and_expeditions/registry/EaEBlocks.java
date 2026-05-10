package net.rebel459.enchants_and_expeditions.registry;

import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.block.AltarBlock;
import net.rebel459.enchants_and_expeditions.sound.EaEBlockSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.SuppliedBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class EaEBlocks {

    static UnifiedRegistries.Blocks BLOCKS = UnifiedRegistries.Blocks.create(EnchantsAndExpeditions.MOD_ID);

    public static final SuppliedBlock ARCANE_BOOKSHELF = BLOCKS.register("arcane_bookshelf",
            Block::new,
            () -> Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.ARCANE_BOOKSHELF)
    );
    public static final SuppliedBlock GLACIAL_BOOKSHELF = BLOCKS.register("glacial_bookshelf",
            Block::new,
            () -> Properties.of()
                    .mapColor(MapColor.ICE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.GLACIAL_BOOKSHELF)
                    .friction(0.98F)
                    .ignitedByLava()
    );
    public static final SuppliedBlock INFERNAL_BOOKSHELF = BLOCKS.register("infernal_bookshelf",
            Block::new,
            () -> Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F)
                    .sound(EaEBlockSounds.INFERNAL_BOOKSHELF)
    );

    public static final SuppliedBlock ALTAR = BLOCKS.register("altar",
            AltarBlock::new,
            () -> Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(3.0F)
                    .sound(SoundType.DEEPSLATE_TILES)
                    .noOcclusion()
                    .isViewBlocking((_, _, _) -> false)
    );

    public static void init() {}
}