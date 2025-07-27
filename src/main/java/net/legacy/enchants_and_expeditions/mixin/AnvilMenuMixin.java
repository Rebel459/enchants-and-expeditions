package net.legacy.enchants_and_expeditions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
    public void modifyPrice(CallbackInfo ci) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack itemStack = anvilMenu.slots.getFirst().getItem();
        ItemStack repairItem = anvilMenu.slots.get(1).getItem();
        if (!itemStack.is(EaEItemTags.VARIABLE_REPAIR_COST)) cost.set(0);
        else if (itemStack.getComponents().has(DataComponents.ENCHANTABLE)) {
            int repairMultiplier = repairItem.getCount();
            double percentageDamaged = (double) itemStack.getDamageValue() / itemStack.getMaxDamage();
            if (percentageDamaged > 0.75 && repairMultiplier > 4) repairMultiplier = 4;
            else if (percentageDamaged > 0.5 && percentageDamaged <= 0.75 && repairMultiplier > 3) repairMultiplier = 3;
            else if (percentageDamaged > 0.25 && percentageDamaged <= 0.5 && repairMultiplier > 2) repairMultiplier = 2;
            else if (percentageDamaged <= 0.25 && repairMultiplier > 1) repairMultiplier = 1;

            int repairCost = itemStack.get(DataComponents.ENCHANTABLE).value();
            if (repairCost > 25) repairCost = 25;
            repairCost = (26 - repairCost) / 4;
            if (repairCost < 1) repairCost = 1;

            cost.set(repairCost * repairMultiplier);
        }
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