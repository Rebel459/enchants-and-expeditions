package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class EaEBlockTags {

    public static final TagKey<Block> MANA_PROVIDER = register("mana_provider");
    public static final TagKey<Block> SCORCH_PROVIDER = register("scorch_provider");
    public static final TagKey<Block> FROST_PROVIDER = register("frost_provider");

    @NotNull
    private static TagKey<Block> register(@NotNull String path) {
        return TagKey.create(Registries.BLOCK, EnchantsAndExpeditions.id(path));
    }

}