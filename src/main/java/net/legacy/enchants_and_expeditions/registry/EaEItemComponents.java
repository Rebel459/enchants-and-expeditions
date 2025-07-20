package net.legacy.enchants_and_expeditions.registry;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Repairable;

public class EaEItemComponents {
    public static void init(){
        DefaultItemComponentEvents.MODIFY.register(context -> {
            context.modify(Items.BOW, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.BOW_REPAIR_MATERIALS)));
            });
            context.modify(Items.CROSSBOW, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.CROSSBOW_REPAIR_MATERIALS)));
            });
            if (!EnchantsAndExpeditions.isLegaciesAndLegendsLoaded) {
                context.modify(Items.TRIDENT, builder -> {
                    builder.set(DataComponents.ATTRIBUTE_MODIFIERS, TridentItem.createAttributes());
                    HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                    builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.TRIDENT_REPAIR_MATERIALS)));
                });
            }
            context.modify(Items.ELYTRA, builder -> {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
            context.modify(Items.SHIELD, builder -> {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });

            context.modify(Items.FISHING_ROD, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.FISHING_ROD_REPAIR_MATERIALS)));
            });
            context.modify(Items.BRUSH, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.BRUSH_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
            context.modify(Items.SHEARS, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.SHEARS_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
            context.modify(Items.FLINT_AND_STEEL, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.FLINT_AND_STEEL_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
            context.modify(Items.CARROT_ON_A_STICK, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.CARROT_ON_A_STICK_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
            context.modify(Items.WARPED_FUNGUS_ON_A_STICK, builder -> {
                HolderGetter<Item> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
                builder.set(DataComponents.REPAIRABLE, new Repairable(holderGetter.getOrThrow(EaEItemTags.WARPED_FUNGUS_A_STICK_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            });
        });
    }
}