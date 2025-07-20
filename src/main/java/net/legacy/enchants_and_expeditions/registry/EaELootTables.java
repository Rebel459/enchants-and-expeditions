package net.legacy.enchants_and_expeditions.registry;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

public final class EaELootTables {
    public EaELootTables() {
    }

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

    public static void init() {
            LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
                LootPool.Builder pool;

                // Vanilla

                if (EaEConfig.get.loot_table_injects) {
                    if (BuiltInLootTables.DESERT_PYRAMID.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(10)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.END_CITY_TREASURE.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(11)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FEATHER_FALLING), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.JUNGLE_TEMPLE.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(5)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.THORNS), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.NETHER_BRIDGE.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(16)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FLAME), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FIRE_ASPECT), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.RUINED_PORTAL.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(10)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FLAME), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FIRE_ASPECT), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.SHIPWRECK_MAP.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(11)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.DEPTH_STRIDER), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(8)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.RIPTIDE), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(15)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.RIPTIDE), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.WOODLAND_MANSION.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(5)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.CHANNELING), ConstantValue.exactly(1.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(4)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MENDING), ConstantValue.exactly(1.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.ANCIENT_CITY.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(14)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MENDING), ConstantValue.exactly(1.0F)));
                        tableBuilder.withPool(pool);
                    }

                    // Legacies and Legends

                    if (EaEConfig.get.legacies_and_legends_integration) {
                        if (EaELootTables.DUNGEON_CHEST_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.SMITING), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_ARID.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.SMITING), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ARCANE_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_DEEP.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ARCANE_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FROST_WALKER), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.CHILLED), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_FROZEN.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FROST_WALKER), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.CHILLED), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), ConstantValue.exactly(1.0F)));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_SIMPLE.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), ConstantValue.exactly(1.0F)));
                            tableBuilder.withPool(pool);
                        }

                        if (EaELootTables.DUNGEON_CHEST_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.THORNS), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.TANGLED), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }
                        if (EaELootTables.DUNGEON_LIBRARY_VERDANT.equals(id)) {
                            pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.THORNS), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.TANGLED), UniformGenerator.between(1.0F, 3.0F)));
                            tableBuilder.withPool(pool);
                        }

                        if (EnchantsAndExpeditions.isProgressionRebornLoaded && EaEConfig.get.progression_reborn_integration) {
                            if (EaELootTables.DUNGEON_CHEST_INFERNAL.equals(id)) {
                                pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(28)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.REFORGE), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ELEMENTAL_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                                tableBuilder.withPool(pool);
                            }
                            if (EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id)) {
                                pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(13)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.REFORGE), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ELEMENTAL_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                                tableBuilder.withPool(pool);
                            }
                        } else {
                            if (EaELootTables.DUNGEON_CHEST_INFERNAL.equals(id)) {
                                pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(29)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ELEMENTAL_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                                tableBuilder.withPool(pool);
                            }
                            if (EaELootTables.DUNGEON_LIBRARY_INFERNAL.equals(id)) {
                                pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(14)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EaEEnchantments.ELEMENTAL_PROTECTION), UniformGenerator.between(1.0F, 3.0F)));
                                tableBuilder.withPool(pool);
                            }
                        }
                    }

                }

            });
    }

    private static @NotNull ResourceKey<LootTable> registerLegaciesAndLegends(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("legacies_and_legends", path));
    }

}