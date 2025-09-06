package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {


    @Inject(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;heal(F)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void modifyHealingCondition(CallbackInfo ci) {
        AbstractHorse horse = AbstractHorse.class.cast(this);
        if (EnchantingHelper.hasEnchantment(horse.getBodyArmorItem(), EaEEnchantments.RECOVERY) && horse.level() instanceof ServerLevel && horse.isAlive() && horse.getRandom().nextInt(900 - EnchantingHelper.getLevel(horse.getBodyArmorItem(), EaEEnchantments.RECOVERY) * 150) == 0 && horse.deathTime == 0) {
            horse.heal(1.0F);
            ci.cancel();
        }
    }
}