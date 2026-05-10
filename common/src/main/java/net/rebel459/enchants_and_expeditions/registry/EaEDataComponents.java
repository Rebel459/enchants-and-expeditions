package net.rebel459.enchants_and_expeditions.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Repairable;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.tag.EaEItemTags;
import net.rebel459.enchants_and_expeditions.util.EnchantingRerolls;
import net.rebel459.enchants_and_expeditions.util.EnchantmentSlots;
import net.rebel459.unified.platform.UnifiedEvents;
import net.rebel459.unified.platform.UnifiedPlatform;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EaEDataComponents {

    public static void init(){
        UnifiedEvents.DefaultDataComponents.modify((item, builder, provider) -> {

            ItemStack stack = item.getDefaultInstance();

            if (item == Items.BOW) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.BOW_REPAIR_MATERIALS)));
            }
            if (item == Items.CRAFTING_TABLE) {
                builder.set(DataComponents.REPAIRABLE, new Repairable(provider.getOrThrow(EaEItemTags.CROSSBOW_REPAIR_MATERIALS)));
            }
            if (!EnchantsAndExpeditions.isLegaciesAndLegendsLoaded() && !EnchantsAndExpeditions.isCombatRebornLoaded()) {
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
            }

            if (EaEConfig.get().general.craftable_experience_bottles) {
                if ((stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) && stack.getMaxStackSize() == 1) {
                    if (stack.has(DataComponents.POTION_CONTENTS) && stack.get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
                        builder.set(DataComponents.MAX_STACK_SIZE, 16);
                    }
                }
            }

            Map<String, Integer> enchantmentSlots = new HashMap<>();
            for (EaEConfig.ItemEnchantmentSlots entry : EaEConfig.get().item_enchantment_slots) {
                enchantmentSlots.put(entry.key, entry.slots);
            }
            HashMap<TagKey<Item>, Integer> itemTags = new HashMap<>();
            HashMap<Item, Integer> items = new HashMap<>();
            HashMap<Item, Integer> phrasedItems = new HashMap<>();
            var itemLookup = provider.lookup(Registries.ITEM).get();

            enchantmentSlots.forEach((key, value) -> {
                if (enchantmentSlots.containsKey("*end_reborn:netherite") && UnifiedPlatform.isModLoaded("end_reborn")) {
                    if (Objects.equals(key, "*netherite")) {
                        return;
                    }
                    else if (Objects.equals(key, "*end_reborn:netherite")) {
                        key = "*netherite";
                    }
                }
                if (key.startsWith("#")) {
                    key = key.substring(1);
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, getId(key));
                    if (provider.get(tag).isPresent()) itemTags.put(tag, value);
                }
                else if (key.startsWith("*")) {
                    key = key.substring(1);
                    boolean checkNamespace = key.contains(":");
                    Identifier id = getId(key);
                    String itemName = item.builtInRegistryHolder().getRegisteredName();
                    if (stack.isEnchantable() && ((!checkNamespace && itemName.contains(key)) || (checkNamespace && itemName.contains(id.getNamespace()) && itemName.contains(id.getPath())))) {
                        phrasedItems.put(item, value);
                    }
                }
                else {
                    Optional<Holder.Reference<Item>> optional = itemLookup.get(ResourceKey.create(Registries.ITEM, getId(key)));
                    optional.ifPresent(reference -> items.put(reference.value(), value));
                }
            });

            itemTags.forEach((key, value) -> {
                if (stack.is(key)) {
                    builder.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), EnchantmentSlots.create(value));
                }
            });
            items.forEach((key, value) -> {
                phrasedItems.remove(key);
                if (item == key) {
                    builder.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), EnchantmentSlots.create(value));
                }
            });
            phrasedItems.forEach((key, value) -> {
                if (item == key) {
                    builder.set(EaEDataComponents.ENCHANTMENT_SLOTS.get(), EnchantmentSlots.create(value));
                }
            });
        });
    }

    private static Identifier getId(String key) {
        if (!key.contains(":")) {
            return Identifier.withDefaultNamespace(key);
        }
        else {
            return Identifier.parse(key);
        }
    }

    static UnifiedRegistries.DataComponentTypes COMPONENTS = UnifiedRegistries.DataComponentTypes.create(EnchantsAndExpeditions.MOD_ID);

    public static final Supplied<DataComponentType<EnchantmentSlots>> ENCHANTMENT_SLOTS = COMPONENTS.register("item_slots", (b) -> b.persistent(EnchantmentSlots.CODEC).networkSynchronized(EnchantmentSlots.STREAM_CODEC).cacheEncoding());
    public static final Supplied<DataComponentType<EnchantingRerolls>> ENCHANTING_REROLLS = COMPONENTS.register("enchanting_rerolls", (b) -> b.persistent(EnchantingRerolls.CODEC).networkSynchronized(EnchantingRerolls.STREAM_CODEC).cacheEncoding());
}