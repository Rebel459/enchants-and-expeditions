package net.rebel459.enchants_and_expeditions.mixin.integration.jei;

import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.gui.plugins.AbstractContainerScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractContainerScreenHandler.class, remap = false)
public abstract class AbstractContainerScreenHandlerMixin {

    @Inject(method = "apply(Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;)Lmezz/jei/api/gui/handlers/IGuiProperties;", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableJeiEnchantingOverlay(AbstractContainerScreen<?> containerScreen, CallbackInfoReturnable<IGuiProperties> cir) {
        if (containerScreen instanceof EnchantmentScreen) cir.setReturnValue(null);
    }
}