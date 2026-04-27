package net.rebel459.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.registry.EaEMobEffects;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @WrapOperation(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;getValue()I"))
    private int celerityBlessing(ExperienceOrb orb, Operation<Integer> original, @Local(name = "serverPlayer") ServerPlayer serverPlayer) {
        int value = original.call(orb);
        ItemStack stack = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.CELERITY_BLESSING)) {
            int existingDuration = 0;
            if (serverPlayer.getEffect(EaEMobEffects.CELERITY) != null) existingDuration = serverPlayer.getEffect(EaEMobEffects.CELERITY).getDuration();
            serverPlayer.addEffect(new MobEffectInstance(EaEMobEffects.CELERITY, Math.clamp(Math.clamp(value, 1, 10) * 10L + existingDuration, 1, 400), 0, true, false, false));
        }
        return value;
    }
}