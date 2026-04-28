package net.rebel459.enchants_and_expeditions.network;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.unified.platform.UnifiedEvents;

import java.util.*;

public final class EnchantmentSlotCaps {
    private EnchantmentSlotCaps() {}

    public record Sync(Map<Identifier, Integer> slotCaps) implements CustomPacketPayload {
        public static final Type<Sync> ID = new Type<>(EnchantsAndExpeditions.id("enchantment_slot_caps"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Sync> CODEC = new StreamCodec<>() {
            @Override
            public Sync decode(RegistryFriendlyByteBuf buf) {
                int size = buf.readVarInt();
                Map<Identifier, Integer> slotCaps = new LinkedHashMap<>(size);
                for (int i = 0; i < size; i++) {
                    slotCaps.put(Identifier.STREAM_CODEC.decode(buf), buf.readVarInt());
                }
                return new Sync(slotCaps);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, Sync value) {
                buf.writeVarInt(value.slotCaps.size());
                value.slotCaps.forEach((id, slots) -> {
                    Identifier.STREAM_CODEC.encode(buf, id);
                    buf.writeVarInt(slots);
                });
            }
        };

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public static Map<Identifier, Integer> getSlotCapsForSync() {
        Map<Identifier, Integer> caps = new LinkedHashMap<>();
        SLOT_CAPS.forEach((item, value) -> caps.put(BuiltInRegistries.ITEM.getKey(item), value));
        return caps;
    }

    public static void applySyncedSlotCaps(Map<Identifier, Integer> caps) {
        SLOT_CAPS.clear();
        caps.forEach((id, value) -> BuiltInRegistries.ITEM.getOptional(id).ifPresent(item -> SLOT_CAPS.put(item, value)));
    }

    public static final Map<Item, Integer> SLOT_CAPS = new HashMap<>();

    public static void init() {
        UnifiedEvents.Server.onStart(server -> {
            rebuildSlotCaps(server.registryAccess());
        });

        UnifiedEvents.Server.onDatapackLoad(server -> {
            rebuildSlotCaps(server.registryAccess());
        });
    }

    private static void rebuildSlotCaps(RegistryAccess.Frozen provider) {
        SLOT_CAPS.clear();

        var enchantmentLookup = provider.lookup(Registries.ENCHANTMENT).get();
        Set<Identifier> enchantmentIds = enchantmentLookup.keySet();
        List<Holder<Enchantment>> enchantments = new ArrayList<>();
        List<Item> items = provider.lookup(Registries.ITEM).get().stream().toList();
        for (Identifier id : enchantmentIds) {
            enchantmentLookup.get(id).ifPresent(enchantment -> {
                if (!EnchantingHelper.disableEnchantment(enchantment, null)) enchantments.add(enchantment);
            });
        }
        for (Item item : items) {
            ItemStack stack = item.getDefaultInstance();
            if (!stack.isEnchantable()) continue;
            int validSlots = 0;
            for (Holder<Enchantment> holder : enchantments) {
                Enchantment enchantment = holder.value();
                if (enchantment.isSupportedItem(stack)) {
                    if (EnchantingHelper.isPowerful(holder)) validSlots += 2;
                    else if (EnchantingHelper.isEnchantment(holder)) validSlots += 1;
                }
            }
            if (EnchantingHelper.hasSlots(stack) && stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).slots() > validSlots) SLOT_CAPS.put(item, validSlots);
        }
    }
}
