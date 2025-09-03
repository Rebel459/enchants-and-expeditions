package net.legacy.enchants_and_expeditions.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

public class EnchantingHelper {
    public static int enchantmentScore(ItemStack stack) {
        return stack.getEnchantments().size() - getBlessings(stack) - getCurses(stack);
    }

    public static List<EnchantmentInstance> evaluateEnchantments(ItemStack stack, List<EnchantmentInstance> list) {
        if (getBlessings(stack) >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment.is(EaEEnchantmentTags.BLESSING);
            });
        }
        if (getCurses(stack) >= 1) {
            list.removeIf(enchantmentInstance -> {
                return enchantmentInstance.enchantment.is(EnchantmentTags.CURSE);
            });
        }
        list.removeIf(enchantmentInstance -> {
            return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantmentInstance.enchantment) >= enchantmentInstance.level;
        });
        list.removeIf(enchantmentInstance -> {
            for (int x = 0; x < enchantmentInstance.enchantment.value().exclusiveSet().size(); x++) {
                if (enchantmentInstance.enchantment.value().exclusiveSet().get(x) == stack.getEnchantments()) return true;
            }
            return false;
        });
        return list;
    }

    public static int getBlessings(ItemStack stack) {
        var enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.isEmpty()) {
            return 0;
        }

        int blessingCount = 0;
        for (var enchantment : enchantments.entrySet()) {
            if (enchantment.getKey().is(EaEEnchantmentTags.BLESSING)) {
                blessingCount++;
            }
        }
        return blessingCount;
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return BooleanUtils.toBoolean(getEnchantmentLevel(stack, enchantment));
    }

    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        if (hasEnchantment(stack, enchantment)) return EnchantmentHelper.getItemEnchantmentLevel(getEnchantment(stack, enchantment), stack);
        else return 0;
    }

    public static Holder<Enchantment> getEnchantment(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        ItemEnchantments itemEnchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (holder.is(enchantment)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static int getCurses(ItemStack stack) {
        var enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (enchantments.isEmpty()) {
            return 0;
        }

        int curseCount = 0;
        for (var enchantment : enchantments.entrySet()) {
            if (enchantment.getKey().is(EnchantmentTags.CURSE)) {
                curseCount++;
            }
        }
        return curseCount;
    }
}
