package net.legacy.enchants_and_expeditions.mixin.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

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

    @Inject(method = "component", at = @At("TAIL"), cancellable = true)
    private <T> void harnessEnchantability(DataComponentType<T> dataComponentType, T object, CallbackInfoReturnable<Item.Properties> cir) {
        if (!dataComponentType.equals(DataComponents.EQUIPPABLE)) return;
        List<DyeColor> dyeList = Arrays.stream(DyeColor.values()).toList();
        for (DyeColor dyeColor : dyeList) {
            if (object.equals(Equippable.harness(dyeColor))) {
                cir.setReturnValue(cir.getReturnValue().enchantable(10));
                break;
            }
        }
    }
}