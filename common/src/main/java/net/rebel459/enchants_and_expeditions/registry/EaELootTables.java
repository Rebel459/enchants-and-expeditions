package net.rebel459.enchants_and_expeditions.registry;

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
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.rebel459.unified.platform.UnifiedEvents;
import org.jetbrains.annotations.NotNull;

public final class EaELootTables {
    public EaELootTables() {}

    public static final ResourceKey<LootTable> BIRCH_RUINS = registerLaL("chests/forest_ruins/birch");
    public static final ResourceKey<LootTable> CHERRY_RUINS = registerLaL("chests/forest_ruins/cherry");
    public static final ResourceKey<LootTable> MAPLE_RUINS = registerLaL("chests/forest_ruins/maple");
    public static final ResourceKey<LootTable> DEEP_RUINS = registerLaL("chests/deep_ruins/deep");
    public static final ResourceKey<LootTable> SCULK_RUINS = registerLaL("chests/deep_ruins/sculk");
    public static final ResourceKey<LootTable> PALE_CABIN = registerLaL("chests/pale_cabin/chest");
    public static final ResourceKey<LootTable> PALE_CABIN_SECRET = registerLaL("chests/pale_cabin/secret");
    public static final ResourceKey<LootTable> RUINED_AETHER_PORTAL = registerLaL("chests/ruined_aether_portal");
    public static final ResourceKey<LootTable> RUINED_LIBRARY = registerLaL("chests/ruined_library");
    public static final ResourceKey<LootTable> END_RUINS = registerLaL("chests/end_ruins");
    public static final ResourceKey<LootTable> SWAMP_HUT = registerLaL("chests/swamp_hut");
    public static final ResourceKey<LootTable> RUINS = registerLaL("chests/ruins");
    public static final ResourceKey<LootTable> UNDERGROUND_CABIN = registerLaL("chests/cabin/underground");
    public static final ResourceKey<LootTable> DEEP_CABIN = registerLaL("chests/cabin/deep");
    public static final ResourceKey<LootTable> SPIRE = registerLaL("chests/spire");
    public static final ResourceKey<LootTable> SPIRE_BASE = registerLaL("chests/spire_base");

    public static final ResourceKey<LootTable> DUNGEON_CHEST = registerLaL("chests/dungeon/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL = registerLaL("chests/dungeon/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY = registerLaL("chests/dungeon/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_SIMPLE = registerLaL("chests/dungeon/simple/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_SIMPLE = registerLaL("chests/dungeon/simple/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_SIMPLE = registerLaL("chests/dungeon/simple/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_DEEP = registerLaL("chests/dungeon/deep/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_DEEP = registerLaL("chests/dungeon/deep/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_DEEP = registerLaL("chests/dungeon/deep/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_ARID = registerLaL("chests/dungeon/arid/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_ARID = registerLaL("chests/dungeon/arid/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_ARID = registerLaL("chests/dungeon/arid/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_FROZEN = registerLaL("chests/dungeon/frozen/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_FROZEN = registerLaL("chests/dungeon/frozen/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_FROZEN = registerLaL("chests/dungeon/frozen/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_VERDANT = registerLaL("chests/dungeon/verdant/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_VERDANT = registerLaL("chests/dungeon/verdant/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_VERDANT = registerLaL("chests/dungeon/verdant/library");
    public static final ResourceKey<LootTable> DUNGEON_CHEST_INFERNAL = registerLaL("chests/dungeon/infernal/chest");
    public static final ResourceKey<LootTable> DUNGEON_BARREL_INFERNAL = registerLaL("chests/dungeon/infernal/barrel");
    public static final ResourceKey<LootTable> DUNGEON_LIBRARY_INFERNAL = registerLaL("chests/dungeon/infernal/library");

    public static final ResourceKey<LootTable> REMNANTS_LIBRARY = registerRemnants("chests/remnants/library_barrels");
    public static final ResourceKey<LootTable> REMNANTS_VAULT = registerRemnants("chests/remnants/vault");
    public static final ResourceKey<LootTable> REMNANTS_OMINOUS_VAULT = registerRemnants("chests/remnants/ominous_vault");

    public static final ResourceKey<LootTable> ENDERSCAPE_STRONGHOLD_LIBRARY = registerRemnants("stronghold/chest/library");

    public static void init() {
            UnifiedEvents.LootTables.modify((tableBuilder, id, registries) -> {
                LootPool.Builder pool;

                if (EaEConfig.get().misc.loot_table_injects) {

                    // Treasure
                    if (BuiltInLootTables.END_CITY_TREASURE.equals(id) && (!EnchantsAndExpeditions.isEnderscapeLoaded || !EaEConfig.get().integrations.enderscape)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(11))
                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING))));
                        tableBuilder.addPool(pool);
                    }

                    // Tomes
                    if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (ENDERSCAPE_STRONGHOLD_LIBRARY.equals(id) && EaEConfig.get().integrations.enderscape) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.RUINED_LIBRARY.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MANA).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    if (BuiltInLootTables.IGLOO_CHEST.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(2))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (BuiltInLootTables.SHIPWRECK_MAP.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FROST).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    if (BuiltInLootTables.NETHER_BRIDGE.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.SPIRE.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_SCORCH).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    // Elder Guardian - Tome of Flow
                    if (EaELootTables.REMNANTS_OMINOUS_VAULT.equals(id) && EaEConfig.get().integrations.remnants) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_FLOW).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    if (BuiltInLootTables.WOODLAND_MANSION.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_CHAOS).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_CHAOS).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    if (BuiltInLootTables.ANCIENT_CITY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(11))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_GREED).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_ARID.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_GREED).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(8))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MIGHT).setWeight(1));
                        tableBuilder.addPool(pool);
                    }
                    if (EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id) && EaEConfig.get().integrations.legacies_and_legends) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(5))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_MIGHT).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    // Librarian - Tome of Stability
                    if (BuiltInLootTables.ABANDONED_MINESHAFT.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(14))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_STABILITY).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    // Librarian - Tome of Power
                    if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(29))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_POWER).setWeight(1));
                        tableBuilder.addPool(pool);
                    }

                    // Remnants
                    if (EaELootTables.REMNANTS_LIBRARY.equals(id) && EaEConfig.get().integrations.remnants) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(EmptyLootItem.emptyItem().setWeight(16))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_STABILITY).setWeight(1))
                                .add(LootItem.lootTableItem(EaEItems.TOME_OF_POWER).setWeight(1));;
                        tableBuilder.addPool(pool);
                    }

                    // Enchantments
                    if (EaEConfig.get().integrations.legacies_and_legends) {
                        if (EaELootTables.DUNGEON_CHEST_SIMPLE.equals(id) || EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MANA))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MANA_BLESSING))));
                            tableBuilder.addPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_ARID.equals(id) || EaELootTables.DUNGEON_LIBRARY_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.GREED))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.GREED_BLESSING))));
                            tableBuilder.addPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_FROZEN.equals(id) || EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FROST))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FROST_BLESSING))));
                            tableBuilder.addPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_DEEP.equals(id) || EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MIGHT))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.MIGHT_BLESSING))));
                            tableBuilder.addPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_VERDANT.equals(id) || EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.CHAOS))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.CHAOS_BLESSING))));
                            tableBuilder.addPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_INFERNAL.equals(id) || EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.SCORCH))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(8))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.SCORCH_BLESSING))));
                            tableBuilder.addPool(pool);
                        }
                    }
                    if (EaEConfig.get().integrations.remnants) {
                        if (EaELootTables.REMNANTS_VAULT.equals(id) || EaELootTables.REMNANTS_LIBRARY.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FLOW))));
                            tableBuilder.addPool(pool);
                        }
                        if (EaELootTables.REMNANTS_LIBRARY.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(EmptyLootItem.emptyItem().setWeight(14))
                                    .add(LootItem.lootTableItem(Items.BOOK).setWeight(1).apply(EnchantRandomlyFunction.randomEnchantment().withOneOf(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantmentTags.FLOW_BLESSING))));
                            tableBuilder.addPool(pool);
                        }
                    }
                }
            });
    }

    private static @NotNull ResourceKey<LootTable> registerLaL(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("legacies_and_legends", path));
    }
    private static @NotNull ResourceKey<LootTable> registerRemnants(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("remnants", path));
    }
}