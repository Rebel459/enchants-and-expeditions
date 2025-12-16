package net.legacy.enchants_and_expeditions.mixin.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public  class ItemPropertiesMixin {
    @Inject(method = "horseArmor", at = @At("TAIL"), cancellable = true)
    private void horseArmorEnchantability(ArmorMaterial material, CallbackInfoReturnable<Item.Properties> cir) {
        cir.setReturnValue(
                cir.getReturnValue()
                        .enchantable(material.enchantmentValue())
        );
    }
    @Inject(method = "nautilusArmor", at = @At("TAIL"), cancellable = true)
    private void nautilusArmorEnchantability(ArmorMaterial material, CallbackInfoReturnable<Item.Properties> cir) {
        cir.setReturnValue(
                cir.getReturnValue()
                        .enchantable(material.enchantmentValue())
        );
    }
    @Inject(method = "wolfArmor", at = @At("TAIL"), cancellable = true)
    private void wolfArmorEnchantability(ArmorMaterial material, CallbackInfoReturnable<Item.Properties> cir) {
        cir.setReturnValue(
                cir.getReturnValue()
                        .enchantable(material.enchantmentValue())
        );
    }
    @Inject(method = "spear", at = @At("TAIL"), cancellable = true)
    private void addCooldown(ToolMaterial toolMaterial, float f, float g, float h, float i, float j, float k, float l, float m, float n, CallbackInfoReturnable<Item.Properties> cir) {
        cir.setReturnValue(
                cir.getReturnValue()
                        .useCooldown(5F)
        );
    }
}