package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.util.CreativeTabs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public final class EaEItems {

    // Items
    public static final Item ICE_SHARD = new Item(
            new Item.Properties()
                    .stacksTo(64)
    );

    // Tomes
    public static final Item TOME_OF_MANA = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_FROST = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_SCORCH = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .stacksTo(1)
    );
    public static final Item TOME_OF_FLOW = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_CHAOS = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_GREED = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_MIGHT = new Item(
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_STABILITY = new Item(
            new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_POWER = new Item(
            new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );

    public static void init() {
        registerItemAfter(Items.HEART_OF_THE_SEA, ICE_SHARD, "ice_shard", CreativeModeTabs.INGREDIENTS);

        registerItemAfter(Items.OMINOUS_TRIAL_KEY, TOME_OF_MANA, "tome_of_mana", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_MANA, TOME_OF_FROST, "tome_of_frost", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_FROST, TOME_OF_SCORCH, "tome_of_scorch", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_SCORCH, TOME_OF_FLOW, "tome_of_flow", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_FLOW, TOME_OF_CHAOS, "tome_of_chaos", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_CHAOS, TOME_OF_GREED, "tome_of_greed", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_GREED, TOME_OF_MIGHT, "tome_of_might", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_MIGHT, TOME_OF_STABILITY, "tome_of_stability", CreativeModeTabs.INGREDIENTS);
        registerItemAfter(TOME_OF_STABILITY, TOME_OF_POWER, "tome_of_power", CreativeModeTabs.INGREDIENTS);
    }

    @SafeVarargs
    private static void registerItemAfter(@NotNull ItemLike comparedItem, @NotNull Item item, @NotNull String path, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        registerItemAfter(comparedItem, item, path, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    @SafeVarargs
    private static void registerItemAfter(@NotNull ItemLike comparedItem, @NotNull Item item, @NotNull String path, @NotNull CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        actualRegister(item, path);
        CreativeTabs.addAfter(comparedItem, item, tabVisibility, tabs);
    }

    private static void actualRegister(@NotNull Item item, @NotNull String path) {
        if (BuiltInRegistries.ITEM.getOptional(EnchantsAndExpeditions.id(path)).isEmpty()) {
            Registry.register(BuiltInRegistries.ITEM, EnchantsAndExpeditions.id(path), item);
        }
    }
}