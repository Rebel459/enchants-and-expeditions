package net.legacy.enchants_and_expeditions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.tools.Tool;

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
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack itemStack = anvilMenu.slots.getFirst().getItem();
        if ((itemStack.is(EaEItemTags.RINGS) || itemStack.is(EaEItemTags.NECKLACES)) && itemStack.getComponents().has(DataComponents.ENCHANTABLE)) {
            int enchantability = itemStack.get(DataComponents.ENCHANTABLE).value();
            if (enchantability > 25) enchantability = 25;
            cost.set(26 - enchantability);
            return;
        }
        cost.set(0);
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    protected void mayPickup(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}