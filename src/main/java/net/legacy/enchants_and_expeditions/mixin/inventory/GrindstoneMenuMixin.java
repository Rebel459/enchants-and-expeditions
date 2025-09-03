package net.legacy.enchants_and_expeditions.mixin.inventory;

import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.GrindstoneMenu;
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
        return mutable -> mutable.removeIf(holder ->
                !holder.is(EnchantmentTags.CURSE) && !holder.is(EaEEnchantmentTags.BLESSING));
    }
}