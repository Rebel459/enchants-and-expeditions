package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class EaEItemTags {

    public static final TagKey<Item> BOW_REPAIR_MATERIALS = bind("bow_repair_materials");
    public static final TagKey<Item> CROSSBOW_REPAIR_MATERIALS = bind("crossbow_repair_materials");
    public static final TagKey<Item> TRIDENT_REPAIR_MATERIALS = bind("trident_repair_materials");

    public static final TagKey<Item> BRUSH_REPAIR_MATERIALS = bind("brush_repair_materials");
    public static final TagKey<Item> SHEARS_REPAIR_MATERIALS = bind("shears_repair_materials");
    public static final TagKey<Item> FLINT_AND_STEEL_REPAIR_MATERIALS = bind("flint_and_steel_repair_materials");
    public static final TagKey<Item> CARROT_ON_A_STICK_REPAIR_MATERIALS = bind("carrot_on_a_stick_repair_materials");
    public static final TagKey<Item> WARPED_FUNGUS_A_STICK_REPAIR_MATERIALS = bind("warped_fungus_repair_materials");

    @NotNull
    private static TagKey<Item> bind(@NotNull String path) {
        return TagKey.create(Registries.ITEM, EnchantsAndExpeditions.id(path));
    }

}