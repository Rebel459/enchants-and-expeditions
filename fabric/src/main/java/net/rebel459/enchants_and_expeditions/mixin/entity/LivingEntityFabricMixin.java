package net.rebel459.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFabricMixin {

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @WrapOperation(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float slidingCurse(Block block, Operation<Float> original) {
        float friction = original.call(block);
        ItemStack stack = this.getItemBySlot(EquipmentSlot.FEET);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SLIDING_CURSE) && friction >= 0.6F && friction < 0.98F) friction = 0.98F;
        return friction;
    }
}