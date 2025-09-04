package net.legacy.enchants_and_expeditions.lib;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// Credit: FrozenLib - https://github.com/FrozenBlock/FrozenLib/blob/master/src/main/java/net/frozenblock/lib/item/api/FrozenCreativeTabs.java

public class CreativeTabs {

    public static void add(ItemLike item, ResourceKey<CreativeModeTab> @NotNull ... tabs) {
        if (item == null) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                var stack = new ItemStack(item);
                stack.setCount(1);
                entries.accept(stack);
            });
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(ItemLike comparedItem, ItemLike item, ResourceKey<CreativeModeTab>... tabs) {
        addBefore(comparedItem, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(
            ItemLike comparedItem,
            ItemLike item,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> entries.addBefore(comparedItem, list, tabVisibility));
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addBefore(
            ItemLike comparedItem,
            ItemLike item,
            String path,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null ) return;
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> {
                entries.addBefore(comparedItem, list, tabVisibility);
            });
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            ResourceKey<CreativeModeTab>... tabs
    ) {
        addAfter(comparedItem, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) {
            return;
        } else {
            item.asItem();
        }
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> entries.addAfter(comparedItem, list, tabVisibility));
        }
    }

    /**
     * @param comparedItem	The item that the added item is compared to
     * @param item	The item that is going to be added
     */
    public static void addAfter(
            ItemLike comparedItem,
            ItemLike item,
            String path,
            CreativeModeTab.TabVisibility tabVisibility,
            ResourceKey<CreativeModeTab> @NotNull ... tabs
    ) {
        if (comparedItem == null || item == null) {
            return;
        } else {
            item.asItem();
        }
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            var stack = new ItemStack(item);
            stack.setCount(1);
            List<ItemStack> list = List.of(stack);
            ItemGroupEvents.modifyEntriesEvent(tab).register((entries) -> {
                entries.addAfter(comparedItem, list, tabVisibility);
            });
        }
    }
}