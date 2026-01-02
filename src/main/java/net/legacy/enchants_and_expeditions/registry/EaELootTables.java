package net.legacy.enchants_and_expeditions.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

public final class EaELootTables {
    public EaELootTables() {}

    public static final ResourceKey<LootTable> BIRCH_RUINS = registerLegaciesAndLegends("chests/forest_ruins/birch");
    public static final ResourceKey<LootTable> CHERRY_RUINS = registerLegaciesAndLegends("chests/forest_ruins/cherry");
    public static final ResourceKey<LootTable> MAPLE_RUINS = registerLegaciesAndLegends("chests/forest_ruins/maple");
    public static final ResourceKey<LootTable> DEEP_RUINS = registerLegaciesAndLegends("chests/deep_ruins/deep");
    public static final ResourceKey<LootTable> SCULK_RUINS = registerLegaciesAndLegends("chests/deep_ruins/sculk");
    public static final ResourceKey<LootTable> PALE_CABIN = registerLegaciesAndLegends("chests/pale_cabin/chest");
    public static final ResourceKey<LootTable> PALE_CABIN_SECRET = registerLegaciesAndLegends("chests/pale_cabin/secret");
    public static final ResourceKey<LootTable> RUINED_AETHER_PORTAL = registerLegaciesAndLegends("chests/ruined_aether_portal");
    public static final ResourceKey<LootTable> RUINED_LIBRARY = registerLegaciesAndLegends("chests/ruined_library");
    public static final ResourceKey<LootTable> END_RUINS = registerLegaciesAndLegends("chests/end_ruins");
    public static final ResourceKey<LootTable> SWAMP_HUT = registerLegaciesAndLegends("chests/swamp_hut");
    public static final ResourceKey<LootTable> RUINS = registerLegaciesAndLegends("chests/ruins");
    public static final ResourceKey<LootTable> UNDERGROUND_CABIN = registerLegaciesAndLegends("chests/cabin/underground");
    public static final ResourceKey<LootTable> DEEP_CABIN = registerLegaciesAndLegends("chests/cabin/deep");
    public static final ResourceKey<LootTable> SPIRE = registerLegaciesAndLegends("chests/spire");
    public static final ResourceKey<LootTable> SPIRE_BASE = registerLegaciesAndLegends("chests/spire_base");

    public static final ResourceKey<LootTable> DUNGEON_CHEST = registerLegaciesAndLegends("chests/dungeon/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL = registerLegaciesAndLegends("chests/dungeon/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY = registerLegaciesAndLegends("chests/dungeon/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_SIMPLE = registerLegaciesAndLegends("chests/dungeon/simple/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_SIMPLE = registerLegaciesAndLegends("chests/dungeon/simple/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_SIMPLE = registerLegaciesAndLegends("chests/dungeon/simple/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_DEEP = registerLegaciesAndLegends("chests/dungeon/deep/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_DEEP = registerLegaciesAndLegends("chests/dungeon/deep/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_DEEP = registerLegaciesAndLegends("chests/dungeon/deep/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_ARID = registerLegaciesAndLegends("chests/dungeon/arid/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_ARID = registerLegaciesAndLegends("chests/dungeon/arid/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_ARID = registerLegaciesAndLegends("chests/dungeon/arid/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_FROZEN = registerLegaciesAndLegends("chests/dungeon/frozen/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_FROZEN = registerLegaciesAndLegends("chests/dungeon/frozen/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_FROZEN = registerLegaciesAndLegends("chests/dungeon/frozen/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_VERDANT = registerLegaciesAndLegends("chests/dungeon/verdant/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_VERDANT = registerLegaciesAndLegends("chests/dungeon/verdant/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_VERDANT = registerLegaciesAndLegends("chests/dungeon/verdant/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_INFERNAL = registerLegaciesAndLegends("chests/dungeon/infernal/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_INFERNAL = registerLegaciesAndLegends("chests/dungeon/infernal/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_INFERNAL = registerLegaciesAndLegends("chests/dungeon/infernal/library");

    public static final ResourceKey<LootTable> REMNANTS_LIBRARY = registerRemnants("chests/remnants/library_barrels");
    public static final ResourceKey<LootTable> REMNANTS_VAULT = registerRemnants("chests/remnants/vault");
    public static final ResourceKey<LootTable> REMNANTS_OMINOUS_VAULT = registerRemnants("chests/remnants/ominous_vault");

    public static final ResourceKey<LootTable> ENDERSCAPE_STRONGHOLD_LIBRARY = registerRemnants("stronghold/chest/library");

    public static void init() {
            LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
                LootPool.Builder pool;

                if (EaEConfig.get.misc.loot_table_injects) {

                    // Treasure
                    if (BuiltInLootTables.END_CITY_TREASURE.equals(id) && (!EnchantsAndExpeditions.isEnderscapeLoaded || !EaEConfig.get.integrations.enderscape)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(11))
                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING))));
                        tableBuilder.withPool(pool);
                    }

                    // Tomes
                    if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (ENDERSCAPE_STRONGHOLD_LIBRARY.equals(id) && EaEConfig.get.integrations.enderscape) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.RUINED_LIBRARY.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.IGLOO_CHEST.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (BuiltInLootTables.SHIPWRECK_MAP.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.NETHER_BRIDGE.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.SPIRE.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    // Elder Guardian - Tome of Flow
                    if (EaELootTables.REMNANTS_OMINOUS_VAULT.equals(id) && EaEConfig.get.integrations.remnants) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FLOW).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.WOODLAND_MANSION.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_CHAOS).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_CHAOS).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.ANCIENT_CITY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_GREED).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_ARID.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_GREED).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MIGHT).setWeight(1));
                        tableBuilder.withPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id) && EaEConfig.get.integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MIGHT).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    // Librarian - Tome of Stability
                    if (BuiltInLootTables.ABANDONED_MINESHAFT.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_STABILITY).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    // Librarian - Tome of Power
                    if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(29))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_POWER).setWeight(1));
                        tableBuilder.withPool(pool);
                    }

                    // Remnants
                    if (EaELootTables.REMNANTS_LIBRARY.equals(id) && EaEConfig.get.integrations.remnants) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(16))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_STABILITY).setWeight(1))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_POWER).setWeight(1));;
                        tableBuilder.withPool(pool);
                    }

                    // Enchantments
                    if (EaEConfig.get.integrations.legacies_and_legends) {
                        if (EaELootTables.DUNGEON_CHEST_SIMPLE.equals(id) || EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MANA))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MANA_BLESSING))));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_ARID.equals(id) || EaELootTables.DUNGEON_LIBRARY_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.GREED))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.GREED_BLESSING))));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_FROZEN.equals(id) || EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FROST))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FROST_BLESSING))));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_DEEP.equals(id) || EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MIGHT))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_CHEST_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MIGHT_BLESSING))));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_VERDANT.equals(id) || EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.CHAOS))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_CHEST_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.CHAOS_BLESSING))));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_INFERNAL.equals(id) || EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.SCORCH))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_CHEST_INFERNAL.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.SCORCH_BLESSING))));
                            tableBuilder.withPool(pool);
                        }
                    }
                    if (EaEConfig.get.integrations.remnants) {
                        if (EaELootTables.REMNANTS_VAULT.equals(id) || EaELootTables.REMNANTS_LIBRARY.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FLOW))));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.REMNANTS_LIBRARY.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FLOW_BLESSING))));
                            tableBuilder.withPool(pool);
                        }
                    }
                }
            });
    }

    private static @NotNull ResourceKey<LootTable> registerLegaciesAndLegends(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("legacies_and_legends", path));
    }
    private static @NotNull ResourceKey<LootTable> registerRemnants(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("remnants", path));
    }
    private static @NotNull ResourceKey<LootTable> registerEnderscape(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("enderscape", path));
    }
}