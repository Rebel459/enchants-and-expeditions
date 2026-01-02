package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomStandGoal.class)
public abstract class RandomStandGoalMixin {

    @Shadow
    @Final
    private AbstractHorse horse;

    @Inject(method = "canUse", at = @At(value = "HEAD"), cancellable = true)
    private void getDamageSource(CallbackInfoReturnable<Boolean> cir) {
        AbstractHorse horse = this.horse;
        ItemStack stack = horse.getBodyArmorItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.EQUESTRIAN)) {
            cir.setReturnValue(false);
        }
    }
}