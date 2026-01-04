package net.legacy.enchants_and_expeditions.mixin.item;

import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public  class ItemMixin {

    @Inject(method = "getEnchantmentValue", at = @At("TAIL"), cancellable = true)
    private void animalArmorEnchantability(CallbackInfoReturnable<Integer> cir) {
        Item item = Item.class.cast(this);
        ItemStack itemStack = item.getDefaultInstance();
        if (item instanceof AnimalArmorItem animalArmor) cir.setReturnValue(animalArmor.getMaterial().value().enchantmentValue());
        if (itemStack.is(Items.COMPASS)) cir.setReturnValue(1);
        if (itemStack.is(Items.RECOVERY_COMPASS)) cir.setReturnValue(1);
        if (itemStack.is(Items.SHIELD)) cir.setReturnValue(10);
        if (itemStack.is(Items.ELYTRA)) cir.setReturnValue(10);
        if (itemStack.is(Items.BRUSH)) cir.setReturnValue(10);
        if (itemStack.is(Items.SHEARS)) cir.setReturnValue(10);
        if (itemStack.is(Items.FLINT_AND_STEEL)) cir.setReturnValue(10);
        if (itemStack.is(Items.CARROT_ON_A_STICK)) cir.setReturnValue(10);
        if (itemStack.is(Items.WARPED_FUNGUS_ON_A_STICK)) cir.setReturnValue(10);
    }

    @Inject(method = "isValidRepairItem", at = @At("TAIL"), cancellable = true)
    private void additionalRepairMaterials(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(Items.ENCHANTED_BOOK) && itemStack2.is(Items.LAPIS_LAZULI)) cir.setReturnValue(true);
        if (itemStack.is(Items.BOW) && itemStack2.is(Items.STRING)) cir.setReturnValue(true);
        if (itemStack.is(Items.CROSSBOW) && itemStack2.is(Items.TRIPWIRE_HOOK)) cir.setReturnValue(true);
        if (itemStack.is(Items.TRIDENT) && itemStack2.is(Items.PRISMARINE_SHARD)) cir.setReturnValue(true);
        if (itemStack.is(Items.FISHING_ROD) && itemStack2.is(Items.STRING)) cir.setReturnValue(true);
        if (itemStack.is(Items.BRUSH) && itemStack2.is(Items.FEATHER)) cir.setReturnValue(true);
        if (itemStack.is(Items.SHEARS) && itemStack2.is(Items.IRON_INGOT)) cir.setReturnValue(true);
        if (itemStack.is(Items.FLINT_AND_STEEL) && itemStack2.is(Items.FLINT)) cir.setReturnValue(true);
        if (itemStack.is(Items.CARROT_ON_A_STICK) && itemStack2.is(Items.CARROT)) cir.setReturnValue(true);
        if (itemStack.is(Items.WARPED_FUNGUS_ON_A_STICK) && itemStack2.is(Items.WARPED_FUNGUS)) cir.setReturnValue(true);
    }
}