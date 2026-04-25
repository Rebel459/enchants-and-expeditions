package net.rebel459.enchants_and_expeditions.tag;

import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantmentTags {

    public static final TagKey<Enchantment> BLESSING = register("blessing");
    public static final TagKey<Enchantment> POWERFUL = register("powerful");
    public static final TagKey<Enchantment> DISABLED_ENCHANTMENTS = register("disabled_enchantments");
    public static final TagKey<Enchantment> ENCHANTMENT_ORDER = register("enchantment_order");
    public static final TagKey<Enchantment> ENFORCE_MAXIMUM_LEVEL = register("enforce_maximum_level");

    public static final TagKey<Enchantment> NOT_ON_AXES = registerCombatReborn("not_on_axes");
    public static final TagKey<Enchantment> NOT_ON_ANIMAL_ARMOR = register("not_on_animal_armor");

    public static final TagKey<Enchantment> GENERIC = register("enchanting_table/all");
    public static final TagKey<Enchantment> MANA = register("enchanting_table/mana");
    public static final TagKey<Enchantment> FROST = register("enchanting_table/frost");
    public static final TagKey<Enchantment> SCORCH = register("enchanting_table/scorch");
    public static final TagKey<Enchantment> FLOW = register("enchanting_table/flow"); // Frost + Mana
    public static final TagKey<Enchantment> CHAOS = register("enchanting_table/chaos"); // Frost + Scorch
    public static final TagKey<Enchantment> GREED = register("enchanting_table/greed"); // Scorch + Mana
    public static final TagKey<Enchantment> MIGHT = register("enchanting_table/might"); // Mana + Frost + Scorch

    public static final TagKey<Enchantment> GENERIC_TREASURE = register("enchanting_table/treasure/all");
    public static final TagKey<Enchantment> MANA_TREASURE = register("enchanting_table/treasure/mana");
    public static final TagKey<Enchantment> FROST_TREASURE = register("enchanting_table/treasure/frost");
    public static final TagKey<Enchantment> SCORCH_TREASURE = register("enchanting_table/treasure/scorch");
    public static final TagKey<Enchantment> FLOW_TREASURE = register("enchanting_table/treasure/flow");
    public static final TagKey<Enchantment> CHAOS_TREASURE = register("enchanting_table/treasure/chaos");
    public static final TagKey<Enchantment> GREED_TREASURE = register("enchanting_table/treasure/greed");
    public static final TagKey<Enchantment> MIGHT_TREASURE = register("enchanting_table/treasure/might");

    public static final TagKey<Enchantment> GENERIC_BLESSING = register("enchanting_table/blessing/all");
    public static final TagKey<Enchantment> MANA_BLESSING = register("enchanting_table/blessing/mana");
    public static final TagKey<Enchantment> FROST_BLESSING = register("enchanting_table/blessing/frost");
    public static final TagKey<Enchantment> SCORCH_BLESSING = register("enchanting_table/blessing/scorch");
    public static final TagKey<Enchantment> FLOW_BLESSING = register("enchanting_table/blessing/flow");
    public static final TagKey<Enchantment> CHAOS_BLESSING = register("enchanting_table/blessing/chaos");
    public static final TagKey<Enchantment> GREED_BLESSING = register("enchanting_table/blessing/greed");
    public static final TagKey<Enchantment> MIGHT_BLESSING = register("enchanting_table/blessing/might");

    @NotNull
    private static TagKey<Enchantment> register(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, EnchantsAndExpeditions.id(path));
    }

    @NotNull
    private static TagKey<Enchantment> registerCombatReborn(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("combat_reborn", path));
    }
}