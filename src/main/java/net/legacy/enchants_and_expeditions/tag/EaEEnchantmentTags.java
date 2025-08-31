package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantmentTags {

    public static final TagKey<Enchantment> IN_ENCHANTING_TABLE = register("in_enchanting_table");

    public static final TagKey<Enchantment> SCORCH = register("enchanting_table/scorch");
    public static final TagKey<Enchantment> FROST = register("enchanting_table/frost");
    public static final TagKey<Enchantment> MANA = register("enchanting_table/mana");

    public static final TagKey<Enchantment> FLOW = register("enchanting_table/flow"); // Frost + Mana
    public static final TagKey<Enchantment> ELEMENTUS = register("enchanting_table/elementus"); // Frost + Scorch
    public static final TagKey<Enchantment> MIGHT = register("enchanting_table/might"); // Scorch + Mana

    public static final TagKey<Enchantment> ARCHAIA = register("enchanting_table/archaia"); // Mana + Frost + Scorch

    @NotNull
    private static TagKey<Enchantment> register(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, EnchantsAndExpeditions.id(path));
    }

}