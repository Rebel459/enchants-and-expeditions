package net.legacy.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow public int experienceLevel;

    @Inject(method = "getXpNeededForNextLevel", at = @At(value = "HEAD"), cancellable = true)
    protected void experienceRebalance(CallbackInfoReturnable<Integer> cir) {
        if (!EaEConfig.get.enchanting.experience_rebalance) return;

        // 0-30

        if (this.experienceLevel <= 5)
            cir.setReturnValue(20);
        else if (this.experienceLevel <= 10)
            cir.setReturnValue(30);
        else if (this.experienceLevel <= 15)
            cir.setReturnValue(40);
        else if (this.experienceLevel <= 20)
            cir.setReturnValue(55);
        else if (this.experienceLevel <= 25)
            cir.setReturnValue(70);
        else if (this.experienceLevel <= 30)
            cir.setReturnValue(85);

        // 31-90

        else if (this.experienceLevel <= 40)
            cir.setReturnValue(100);
        else if (this.experienceLevel <= 50)
            cir.setReturnValue(110);
        else if (this.experienceLevel <= 60)
            cir.setReturnValue(120);
        else if (this.experienceLevel <= 70)
            cir.setReturnValue(130);
        else if (this.experienceLevel <= 80)
            cir.setReturnValue(140);
        else if (this.experienceLevel <= 90)
            cir.setReturnValue(150);

        // 91+

        else if (this.experienceLevel <= 95)
            cir.setReturnValue(200);
        else if (this.experienceLevel <= 100)
            cir.setReturnValue(1000);
        else
            cir.setReturnValue(5000);
    }
}