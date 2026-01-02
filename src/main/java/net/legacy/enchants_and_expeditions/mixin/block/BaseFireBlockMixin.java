package net.legacy.enchants_and_expeditions.mixin.block;

import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Inject(method = "entityInside", at = @At(value = "HEAD"), cancellable = true)
    private void blazingFireImmunity(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, CallbackInfo ci) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        ItemStack stack;
        if (entity instanceof Animal) stack = livingEntity.getItemBySlot(EquipmentSlot.BODY);
        else stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BLAZING)) {
            if (!livingEntity.hasEffect(MobEffects.SPEED)) livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED));
            if (entity.getRemainingFireTicks() == 0) entity.setRemainingFireTicks(1);
            ci.cancel();
        }
    }
}