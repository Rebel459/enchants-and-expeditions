package net.legacy.enchants_and_expeditions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @WrapOperation(method = "createResult", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean particleTint(Enchantment instance, ItemStack stack, Operation<Boolean> original) {
        return false;
    }

    @Inject(method = "createResult",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V",
                    shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void priceless(CallbackInfo ci) {
        cost.set(0);
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    protected void mayPickup(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "calculateIncreasedRepairCost", at = @At(value = "HEAD"), cancellable = true)
    private static void increaseLimit(int oldRepairCost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

}