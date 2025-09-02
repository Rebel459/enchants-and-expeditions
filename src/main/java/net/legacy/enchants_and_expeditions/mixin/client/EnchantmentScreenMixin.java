package net.legacy.enchants_and_expeditions.mixin.client;

import net.legacy.enchants_and_expeditions.util.EnchantingAttributes;
import net.legacy.enchants_and_expeditions.util.EnchantingAttributesAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {
    @Inject(at = @At("TAIL"), method = "renderBg")
    private void EaE$enchantingTableInterface(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        EnchantmentMenu menu = screen.getMenu();
        EnchantingAttributes attributes = ((EnchantingAttributesAccessor) menu).getEnchantingAttributes();
        int mana = attributes.getMana();
        guiGraphics.drawString(screen.getFont(), "Mana: " + mana, 10, 10, 0xFFFFFF);
    }
}