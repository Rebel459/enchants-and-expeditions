package net.legacy.enchants_and_expeditions.tag;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class EaEEnchantmentTags {

    public static final TagKey<Enchantment> NOT_OBTAINABLE_FROM_CHISELED_BOOKSHELF = register("not_available_from_chiseled_bookshelf");

    @NotNull
    private static TagKey<Enchantment> register(@NotNull String path) {
        return TagKey.create(Registries.ENCHANTMENT, EnchantsAndExpeditions.id(path));
    }

}