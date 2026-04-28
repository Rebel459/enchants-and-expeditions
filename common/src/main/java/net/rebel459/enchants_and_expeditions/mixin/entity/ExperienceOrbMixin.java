package net.rebel459.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @WrapOperation(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;giveExperiencePoints(I)V"))
    private void celerityBlessing(Player player, int i, Operation<Void> original, @Local(name = "serverPlayer") ServerPlayer serverPlayer) {
        ItemStack stack = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.CELERITY_BLESSING)) {
            int existingDuration = 0;
            var haste = serverPlayer.getEffect(MobEffects.HASTE);
            if (haste != null && !haste.isInfiniteDuration() && haste.getAmplifier() >= 2) existingDuration = serverPlayer.getEffect(MobEffects.HASTE).getDuration();
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.HASTE, Math.clamp((Math.clamp(i, 1, 10) * 10L) + existingDuration, 1, 300), 2, true, false, false));
        }
        original.call(player, i);
    }
}