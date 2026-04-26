package net.rebel459.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @WrapOperation(
            method = "removeNonCursesFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;updateEnchantments(Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/world/item/enchantment/ItemEnchantments;"
            )
    )
    private ItemEnchantments EaE$resetEverything(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer, Operation<ItemEnchantments> original) {
        EnchantingHelper.resetEnchantingRerolls(itemStack);
        return EnchantmentHelper.updateEnchantments(itemStack, (enchantments) -> enchantments.removeIf(_ -> true));
    }
}