package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public abstract class WolfMixin {


    @Inject(method = "aiStep", at = @At(value = "TAIL"), cancellable = true)
    private void modifyHealingCondition(CallbackInfo ci) {
        Wolf wolf = Wolf.class.cast(this);
        if (EnchantingHelper.hasEnchantment(wolf.getBodyArmorItem(), EaEEnchantments.RECOVERY) && wolf.level() instanceof ServerLevel && wolf.isAlive() && wolf.getRandom().nextInt(900) == 0 && wolf.deathTime == 0) {
            wolf.heal(1.0F);
            ci.cancel();
        }
    }
}