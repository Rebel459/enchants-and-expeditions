package net.rebel459.enchants_and_expeditions.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Repairable;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.tag.EaEItemTags;
import net.rebel459.unified.platform.UnifiedEvents;

public class EaEDataComponents {

    public static void init(){
        UnifiedEvents.DefaultDataComponents.modify((item, builder, provider) -> {

            ItemStack stack = item.getDefaultInstance();

            if (item == Items.ENCHANTED_BOOK) {
                builder.set(DataComponents.MAX_DAMAGE, 4);
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(1));
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.BOOK_REPAIR_MATERIALS)));
            }

            if (item == Items.BOW) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.BOW_REPAIR_MATERIALS)));
            }
            if (item == Items.CRAFTING_TABLE) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.CROSSBOW_REPAIR_MATERIALS)));
            }
            if (!EnchantsAndExpeditions.isLegaciesAndLegendsLoaded) {
                if (item == Items.TRIDENT) {
                    builder.set(DataComponents.ATTRIBUTE_MODIFIERS, TridentItem.createAttributes());
                    builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.TRIDENT_REPAIR_MATERIALS)));
                }
            }
            if (item == Items.ELYTRA) {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }
            if (item == Items.SHIELD) {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }

            if (item == Items.COMPASS) {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(1));
            }
            if (item == Items.RECOVERY_COMPASS) {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(1));
            }

            if (item == Items.FISHING_ROD) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.FISHING_ROD_REPAIR_MATERIALS)));
            }
            if (item == Items.BRUSH) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.BRUSH_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }
            if (item == Items.SHEARS) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.SHEARS_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }
            if (item == Items.FLINT_AND_STEEL) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.FLINT_AND_STEEL_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }
            if (item == Items.CARROT_ON_A_STICK) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.CARROT_ON_A_STICK_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            }
            if (item == Items.WARPED_FUNGUS_ON_A_STICK) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.WARPED_FUNGUS_ON_A_STICK_REPAIR_MATERIALS)));
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(10));
            };

            if (EaEConfig.get.general.craftable_experience_bottles) {
                if ((stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) && stack.getMaxStackSize() == 1) {
                    if (stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
                        builder.set(DataComponents.MAX_STACK_SIZE, 16);
                    }
                }
            }
        });
    }
}