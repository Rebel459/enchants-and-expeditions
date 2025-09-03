package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class EaEBlocks {
    public static final Block ARCANE_BOOKSHELF = register("arcane_bookshelf",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F).sound(SoundType.WOOD)
                    .ignitedByLava()
    );
    public static final Block GLACIAL_BOOKSHELF = register("glacial_bookshelf",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F).sound(SoundType.WOOD)
                    .ignitedByLava()
    );
    public static final Block INFERNAL_BOOKSHELF = register("infernal_bookshelf",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.5F).sound(SoundType.WOOD)
                    .ignitedByLava()
    );

    public static final Block MANA_ALTAR = register("mana_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block FROST_ALTAR = register("frost_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block SCORCH_ALTAR = register("scorch_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block FLOW_ALTAR = register("flow_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block CHAOS_ALTAR = register("chaos_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block GREED_ALTAR = register("greed_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block MIGHT_ALTAR = register("might_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block STABILITY_ALTAR = register("stability_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );
    public static final Block POWER_ALTAR = register("power_altar",
            Block::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0F).sound(SoundType.STONE)
                    .ignitedByLava()
    );

    public static void init() {
    }

    private static <T extends Block> @NotNull T registerWithoutItem(String path, Function<Properties, T> block, Properties properties) {
        ResourceLocation id = EnchantsAndExpeditions.id(path);
        return doRegister(id, makeBlock(block, properties, id));
    }

    private static <T extends Block> @NotNull T register(String path, Function<Properties, T> block, Properties properties) {
        T registered = registerWithoutItem(path, block, properties);
        Items.registerBlock(registered);
        return registered;
    }

    private static <T extends Block> @NotNull T doRegister(ResourceLocation id, T block) {
        if (BuiltInRegistries.BLOCK.getOptional(id).isEmpty()) {
            return Registry.register(BuiltInRegistries.BLOCK, id, block);
        }
        throw new IllegalArgumentException("Block with id " + id + " is already in the block registry.");
    }

    private static <T extends Block> T makeBlock(@NotNull Function<Properties, T> function, @NotNull Properties properties, ResourceLocation id) {
        return function.apply(properties.setId(ResourceKey.create(Registries.BLOCK, id)));
    }
}