package net.legacy.enchants_and_expeditions.lib;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;

public class EnchantingHelper {
    public static int enchantmentScore(ItemStack stack) {
        return stack.getEnchantments().size() - getBlessings(stack) - getCurses(stack);
    }

    public static List<EnchantmentInstance> evaluateEnchantments(ItemStack stack, List<EnchantmentInstance> list) {
        List<EnchantmentInstance> removeList = Lists.newArrayList();

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

        list.removeIf(enchantmentInstance -> {
            return configureEnchantments(enchantmentInstance.enchantment);
        });

        return list;
    }

    public static boolean configureEnchantments(Holder<Enchantment> enchantment) {
        if (!EaEConfig.get.enchantments.mending_blessing && enchantment.is(Enchantments.MENDING)) {
            return true;
        }
        else if (!EaEConfig.get.enchantments.infinity_blessing && enchantment.is(Enchantments.INFINITY)) {
            return true;
        }
        else if (!EaEConfig.get.enchantments.channeling_blessing && enchantment.is(Enchantments.CHANNELING)) {
            return true;
        }
        else if (!EaEConfig.get.enchantments.extraction && enchantment.is(EaEEnchantments.EXTRACTION)) {
            return true;
        }
        else if (!EaEConfig.get.enchantments.bloodlust && enchantment.is(EaEEnchantments.BLOODLUST)) {
            return true;
        }
        else if (!EaEConfig.get.enchantments.fragility_curse && enchantment.is(EaEEnchantments.FRAGILITY_CURSE)) {
            return true;
        }
        return false;
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
        return BooleanUtils.toBoolean(getLevel(stack, enchantment));
    }

    public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
        return EnchantmentHelper.getItemEnchantmentLevel(getEnchantment(stack, enchantment), stack);
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
