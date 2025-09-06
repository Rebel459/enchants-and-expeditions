package net.legacy.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.EnumMap;
import java.util.function.BiFunction;

@Mixin(EntityEquipment.class)
public abstract class EntityEquipmentMixin {

    @Shadow @Final public EnumMap<EquipmentSlot, ItemStack> items;

    @WrapOperation(
            method = "dropAll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
            )
    )
    private ItemEntity EaE$keepBoundedEquipment(LivingEntity instance, ItemStack stack, boolean randomizeMotion, boolean includeThrower, Operation<ItemEntity> original) {
        if (!EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BOUNDING_BLESSING)) {
            instance.drop(stack, true, false);
        }
        return null;
    }

    @WrapOperation(
            method = "clear()V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;replaceAll(Ljava/util/function/BiFunction;)V",
                    opcode = Opcodes.INVOKEINTERFACE
            )
    )
    private void EaE$keepBoundedEquipmentFromClearing(EnumMap instance, BiFunction biFunction, Operation<Void> original) {
        this.items.replaceAll((equipmentSlot, itemStack) -> EnchantingHelper.hasEnchantment(itemStack, EaEEnchantments.BOUNDING_BLESSING) ? itemStack : ItemStack.EMPTY);
    }
}