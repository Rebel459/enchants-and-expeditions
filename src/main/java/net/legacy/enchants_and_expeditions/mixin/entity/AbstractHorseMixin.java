package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin {

    @Inject(
            method = "getRiddenSpeed",
            at = @At(value = "TAIL"),
            cancellable = true
    )
    private void galloping(Player player, CallbackInfoReturnable<Float> cir) {
        AbstractHorse horse = AbstractHorse.class.cast(this);
        ItemStack stack = horse.getBodyArmorItem();
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.GALLOPING)) {
            int level = EnchantingHelper.getLevel(stack, EaEEnchantments.GALLOPING);
            cir.setReturnValue(cir.getReturnValue() * (1 + level * 0.1F));
        }
    }
}