package net.legacy.enchants_and_expeditions.mixin.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditionsClient;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.sounds.SoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private boolean EaE$requestedOnce;

    @Unique
    private boolean attributesOpened = false;

    @Unique
    private static final int TOOLTIP_BG_COLOR = 0xA0100010; // Semi-translucent background (alpha = 0xA0, ~63% opaque)
    @Unique
    private static final int TOOLTIP_BORDER = 0xA028007F; // Semi-translucent darker purple border (bottom/right)

    @Inject(method = "init", at = @At("TAIL"))
    private void EaE$requestAttributesOnce(CallbackInfo ci) {
        if (!EaE$requestedOnce) {
            EaE$requestedOnce = true;
            if (EnchantsAndExpeditions.debug) LOGGER.info("[EaE] Client sending C2S Request from EnchantmentScreen.init");
            ClientPlayNetworking.send(new EnchantingAttributes.Request());
        }
    }

    @Unique
    public int leftPos() {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        return (screen.width - 176 - 12) / 2;
    }

    @Unique
    public int topPos() {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        return (screen.height - 166 + 46) / 2;
    }

    @Unique
    public int textureSize = 16;

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void EaE$enchantingTableClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        if (isOverButton((int) mouseX, (int) mouseY) && !this.attributesOpened) {
            this.attributesOpened = true;
            screen.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        } else if (isOverButton((int) mouseX, (int) mouseY)) {
            this.attributesOpened = false;
            screen.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void EaE$enchantingTableInterface(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);

        // Render the 16x16 texture at the top-left of the enchanting table interface
        if (isOverButton(mouseX, mouseY)) {
            guiGraphics.blit(RenderType::guiTextured, EnchantsAndExpeditions.id("textures/gui/attributes_hovered.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            guiGraphics.blit(RenderType::guiTextured, EnchantsAndExpeditions.id("textures/gui/attributes.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        }

        if (!attributesOpened) return;

        EnchantingAttributes.Attributes enchantingAttributes = EnchantsAndExpeditionsClient.getClientEnchantingAttributes();
        if (enchantingAttributes == null) {
            guiGraphics.drawString(screen.getFont(), Component.literal("[EaE] awaiting attribute sync..."), 10, 10, ChatFormatting.GRAY.getColor());
            return;
        }

        int mana = enchantingAttributes.mana();
        int frost = enchantingAttributes.frost();
        int scorch = enchantingAttributes.scorch();
        int flow = enchantingAttributes.flow();
        int chaos = enchantingAttributes.chaos();
        int greed = enchantingAttributes.greed();
        int might = enchantingAttributes.might();
        int stability = enchantingAttributes.stability();
        int divinity = enchantingAttributes.divinity();

        // Calculate tooltip background dimensions
        int x = 10;
        int y = screen.height - 166;
        int padding = 4;
        int textHeight = 9 * 10; // 9 attributes, 10 pixels each (9 pixels font height + 1 spacing)
        int textWidth = 100; // Fixed width, adjust based on longest text if needed

        // Calculate exact width using Font#width
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.mana").append(": " + mana)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.frost").append(": " + frost)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + scorch)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.flow").append(": " + flow)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + chaos)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.greed").append(": " + greed)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.might").append(": " + might)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.stability").append(": " + stability)));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + divinity)));

        // Draw semi-translucent tooltip background
        guiGraphics.fill(x - padding, y - padding, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BG_COLOR);
        // Draw semi-translucent borders
        guiGraphics.fill(x - padding, y - padding, x + textWidth + padding, y - padding + 1, TOOLTIP_BORDER); // Top
        guiGraphics.fill(x - padding, y - padding, x - padding + 1, y + textHeight + padding, TOOLTIP_BORDER); // Left
        guiGraphics.fill(x - padding, y + textHeight + padding - 1, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BORDER); // Bottom
        guiGraphics.fill(x + textWidth + padding - 1, y - padding, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BORDER); // Right

        // Draw attribute text
        y += 1; // Centre text to tooltip box
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.mana").append(": " + mana), x, y, ChatFormatting.DARK_BLUE.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.frost").append(": " + frost), x, y, ChatFormatting.DARK_AQUA.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + scorch), x, y, ChatFormatting.DARK_RED.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.flow").append(": " + flow), x, y, ChatFormatting.AQUA.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + chaos), x, y, ChatFormatting.DARK_GRAY.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.greed").append(": " + greed), x, y, ChatFormatting.YELLOW.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.might").append(": " + might), x, y, ChatFormatting.GREEN.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.stability").append(": " + stability), x, y, ChatFormatting.RED.getColor()); y += 10;
        guiGraphics.drawString(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + divinity), x, y, ChatFormatting.GOLD.getColor());
    }

    @Unique
    private boolean isOverButton(int mouseX, int mouseY) {
        return mouseX >= leftPos() && mouseX <= leftPos() + textureSize && mouseY >= topPos() && mouseY <= topPos() + textureSize;
    }
}