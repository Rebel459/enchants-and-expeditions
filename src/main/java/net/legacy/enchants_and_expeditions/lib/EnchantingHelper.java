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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantingHelper {
    public static int enchantmentScore(ItemStack stack) {
        return stack.getEnchantments().size() - getBlessings(stack) - getCurses(stack);
    }

    public static List<EnchantmentInstance> evaluateEnchantments(ItemStack stack, List<EnchantmentInstance> list) {
        list.removeIf(enchantmentInstance -> {
            return configureEnchantments(enchantmentInstance.enchantment);
        });
        list.removeIf(enchantmentInstance -> {
            return stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantmentInstance.enchantment) >= enchantmentInstance.level;
        });
        list.removeIf(enchantmentInstance -> {
            for (int x = 0; x < enchantmentInstance.enchantment.value().exclusiveSet().size(); x++) {
                if (enchantmentInstance.enchantment.value().exclusiveSet().get(x) == stack.getEnchantments()) return true;
            }
            return false;
        });

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

        List<EnchantmentInstance> enchantmentList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            if (isEnchantment(list.get(x).enchantment)) {
                enchantmentList.add(list.get(x));
            }
        }
        if (EaEConfig.get.general.enchantment_limit != -1) {
            list.removeAll(enchantmentList);
            while (enchantmentList.size() + stack.getEnchantments().size() > EaEConfig.get.general.enchantment_limit) {
                int x = new Random().nextInt(0, enchantmentList.size());
                enchantmentList.remove(x);
            }
            list.addAll(enchantmentList);
        }

        return list;
    }

    public static boolean configureEnchantments(Holder<Enchantment> enchantment) {
        return enchantment.is(EaEEnchantmentTags.DISABLED_ENCHANTMENTS);
    }

    public static boolean onRandomLoot(Holder<Enchantment> enchantment, RandomSource randomSource) {
        if (randomSource.nextInt(0, 3) < 3) {
            return enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EaEEnchantmentTags.ENCHANTING_TABLE_BLESSING) && configureEnchantments(enchantment);
        }
        else return configureEnchantments(enchantment);
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

    public static boolean isEnchantment(Holder<Enchantment> enchantment) {
        return !enchantment.is(EaEEnchantmentTags.BLESSING) && !enchantment.is(EnchantmentTags.CURSE);
    }
}
