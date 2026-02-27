package net.legacy.enchants_and_expeditions.mixin.inventory;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Consumer;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @ModifyArg(
            method = "removeNonCursesFrom(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;updateEnchantments(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/world/item/enchantment/ItemEnchantments;"
            ),
            index = 1
    )
    private Consumer<ItemEnchantments.Mutable> EaE$keepBlessings(Consumer<ItemEnchantments.Mutable> updater) {
        return mutable -> {
            var enchantList = mutable.keySet().stream().toList();
            boolean hasEnchants = false;
            for (Holder<Enchantment> enchant : enchantList) {
                if (EnchantingHelper.isEnchantment(enchant)) {
                    hasEnchants = true;
                    break;
                }
            }
            if (hasEnchants) {
                mutable.removeIf(holder -> !holder.is(EnchantmentTags.CURSE) && !holder.is(EaEEnchantmentTags.BLESSING));
            }
            else {
                mutable.removeIf(holder -> true);
            }
        };
    }
}