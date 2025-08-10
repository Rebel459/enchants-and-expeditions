package net.legacy.enchants_and_expeditions.mixin.recipe;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
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

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$craftImbuedBook(CraftingInput craftingInput, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> cir) {
        if (this.result.is(Items.ENCHANTED_BOOK) && craftingInput.getItem(1, 1).is(Items.ENCHANTED_BOOK)) {
            this.result.applyComponents(craftingInput.getItem(1, 1).getComponentsPatch());
            if (!EaEConfig.get.items.imbued_books)
                cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$craftExperienceBottles(CraftingInput craftingInput, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack waterBottle = craftingInput.getItem(1, 1);
        if (this.result.is(Items.EXPERIENCE_BOTTLE) && waterBottle.is(Items.POTION))
            if (!(waterBottle.getComponents().has(DataComponents.POTION_CONTENTS) && waterBottle.getComponents().get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) || !EaEConfig.get.items.craftable_experience_bottles)
                cir.setReturnValue(ItemStack.EMPTY);
    }
}