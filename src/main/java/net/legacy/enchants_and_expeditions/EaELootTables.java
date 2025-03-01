package net.legacy.enchants_and_expeditions;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class EaELootTables {
    public EaELootTables() {
    }

    public static void init() {
            LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
                LootPool.Builder pool;

                if (EaEConfig.get.loot_table_injects) {
                    if (BuiltInLootTables.DESERT_PYRAMID.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(16)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), UniformGenerator.between(1.0F, 3.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), UniformGenerator.between(1.0F, 3.0F)));
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

                    if (BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(8)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.RIPTIDE), UniformGenerator.between(1.0F, 3.0F)));
                        tableBuilder.withPool(pool);
                    }

                    if (BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(id)) {
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(15)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.RIPTIDE), UniformGenerator.between(1.0F, 3.0F)));
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
                        pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(EmptyLootItem.emptyItem().setWeight(10)).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), ConstantValue.exactly(1.0F))).add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(1)).apply((new SetEnchantmentsFunction.Builder()).withEnchantment(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MENDING), ConstantValue.exactly(1.0F)));
                        tableBuilder.withPool(pool);
                    }

                }

            });
    }
}