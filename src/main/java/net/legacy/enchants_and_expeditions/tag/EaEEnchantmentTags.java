package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantmentTags {

    public static final TagKey<Enchantment> BLESSING = register("blessing");

    public static final TagKey<Enchantment> ENCHANTING_TABLE = register("enchanting_table");

    public static final TagKey<Enchantment> ENCHANTING_TABLE_BLESSING = register("enchanting_table/blessing");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_MANA_BLESSING = register("enchanting_table/blessing/mana");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_FROST_BLESSING = register("enchanting_table/blessing/frost");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_SCORCH_BLESSING = register("enchanting_table/blessing/scorch");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_FLOW_BLESSING = register("enchanting_table/blessing/flow");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_CHAOS_BLESSING = register("enchanting_table/blessing/chaos");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_GREED_BLESSING = register("enchanting_table/blessing/greed");
    public static final TagKey<Enchantment> ENCHANTING_TABLE_MIGHT_BLESSING = register("enchanting_table/blessing/might");

    public static final TagKey<Enchantment> MANA = register("enchanting_table/mana");
    public static final TagKey<Enchantment> FROST = register("enchanting_table/frost");
    public static final TagKey<Enchantment> SCORCH = register("enchanting_table/scorch");
    public static final TagKey<Enchantment> FLOW = register("enchanting_table/flow"); // Frost + Mana
    public static final TagKey<Enchantment> CHAOS = register("enchanting_table/chaos"); // Frost + Scorch
    public static final TagKey<Enchantment> GREED = register("enchanting_table/greed"); // Scorch + Mana
    public static final TagKey<Enchantment> MIGHT = register("enchanting_table/might"); // Mana + Frost + Scorch

    @NotNull
    private static TagKey<Enchantment> register(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, EnchantsAndExpeditions.id(path));
    }

}