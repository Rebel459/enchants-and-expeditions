package net.legacy.enchants_and_expeditions.mixin.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @Shadow @Final
    ItemStack result;

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"))
    private void EaE$keepBookEnchantments(CraftingInput craftingInput, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> cir) {
        if (this.result.is(EaEItems.IMBUED_ENCHANTED_BOOK))
            this.result.applyComponents(craftingInput.getItem(1, 1).getComponentsPatch());
    }
}