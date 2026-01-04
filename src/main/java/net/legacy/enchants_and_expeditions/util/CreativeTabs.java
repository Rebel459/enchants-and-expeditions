package net.legacy.enchants_and_expeditions.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreativeTabs {

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
}
