package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class EaEItemTags {
    public static final TagKey<Item> ENCHANTING_POWER_PROVIDER = register("enchanting_power_provider");
    public static final TagKey<Item> UNBOUNDABLE = register("unboundable");

    public static final TagKey<Item> ANIMAL_ARMOR = register("animal_armor");

    public static final TagKey<Item> VARIABLE_REPAIR_COST = register("variable_repair_cost");

    @NotNull
    private static TagKey<Item> register(@NotNull String path) {
        return TagKey.create(Registries.ITEM, EnchantsAndExpeditions.id(path));
    }
}