package net.legacy.enchants_and_expeditions.mixin.client;

import net.legacy.enchants_and_expeditions.mixin.inventory.EnchantmentMenuMixin;
import net.legacy.enchants_and_expeditions.util.EnchantingAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {
    @Inject(at = @At("HEAD"), method = "renderBg")
    private void EaE$enchantingTableInterface(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        EnchantmentMenu menu = screen.getMenu();

        EnchantingAttributes attributes = ((EnchantingAttributes) menu);
        EnchantingAttributes.Attributes getAttribute = attributes.calculateAttributes();

        attributes.calculateAttributes();

        int mana = getAttribute.mana();
        int frost = getAttribute.frost();
        int scorch = getAttribute.scorch();
        int flow = getAttribute.flow();
        int chaos = getAttribute.chaos();
        int greed = getAttribute.greed();
        int might = getAttribute.might();
        int stability = getAttribute.stability();
        int divinity = getAttribute.divinity();

        int yOffset = 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.mana").append(": " + mana), 10, yOffset, ChatFormatting.DARK_BLUE.getColor());
        yOffset += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.frost").append(": " + frost), 10, yOffset, ChatFormatting.DARK_AQUA.getColor());
        yOffset += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + scorch), 10, yOffset, ChatFormatting.DARK_RED.getColor());
        yOffset += 10;
        if (EnchantmentScreen.hasShiftDown()) {
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.flow").append(": " + flow), 10, yOffset, ChatFormatting.AQUA.getColor());
            yOffset += 10;
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + chaos), 10, yOffset, ChatFormatting.GRAY.getColor());
            yOffset += 10;
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.greed").append(": " + greed), 10, yOffset, ChatFormatting.YELLOW.getColor());
            yOffset += 10;
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.might").append(": " + might), 10, yOffset, ChatFormatting.GREEN.getColor());
            yOffset += 10;
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.stability").append(": " + stability), 10, yOffset, ChatFormatting.RED.getColor());
            yOffset += 10;
            guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + divinity), 10, yOffset, ChatFormatting.GOLD.getColor());
        }
    }
}