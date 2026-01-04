package net.legacy.enchants_and_expeditions.mixin.client.inventory;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditionsClient;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    private static final int TOOLTIP_BG_COLOR = 0xA0100010; // Semi-translucent background
    @Unique
    private static final int TOOLTIP_BORDER = 0xA028007F; // Semi-translucent darker purple border

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
    private void EaE$enchantingTableClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        Player player = screen.minecraft.player;
        int mouseX = (int) d;
        int mouseY = (int) e;
        if (isOverButton(mouseX, mouseY) && !this.attributesOpened) {
            this.attributesOpened = true;
            player.addTag("show_enchanting_attributes");
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        } else if (isOverButton(mouseX, mouseY)) {
            this.attributesOpened = false;
            player.removeTag("show_enchanting_attributes");
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void EaE$enchantingTableInterface(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);

        // Render the 16x16 texture at the top-left of the enchanting table interface
        if (isOverButton(mouseX, mouseY)) {
            guiGraphics.blit(EnchantsAndExpeditions.id("textures/gui/attributes_hovered.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            guiGraphics.blit(EnchantsAndExpeditions.id("textures/gui/attributes.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        }

        attributesOpened = attributesOpened || screen.minecraft.player.getTags().contains("show_enchanting_attributes");

        if (!attributesOpened) return;

        EnchantingAttributes.Attributes enchantingAttributes = EnchantsAndExpeditionsClient.getClientEnchantingAttributes();
        if (enchantingAttributes == null) {
            guiGraphics.drawString(screen.font, Component.literal("[EaE] awaiting attribute sync..."), leftPos() + 20, topPos() + 20, ChatFormatting.GRAY.getColor());
            return;
        }

        int mana = enchantingAttributes.mana();
        int frost = enchantingAttributes.frost();
        int scorch = enchantingAttributes.scorch();
        int flow = enchantingAttributes.flow();
        int chaos = enchantingAttributes.chaos();
        int greed = enchantingAttributes.greed();
        int might = enchantingAttributes.might();
        int corruption = enchantingAttributes.corruption();
        int divinity = enchantingAttributes.divinity();

        // Calculate tooltip background dimensions
        int padding = 4;
        int textHeight = 9 * 10; // 9 attributes, 10 pixels each (9 pixels font height + 1 spacing)
        int textWidth = 100; // Base width, adjusted based on longest text

        // Calculate exact width using Font#width
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.mana").append(": " + Math.max(0, mana))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.frost").append(": " + Math.max(0, frost))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + Math.max(0, scorch))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.flow").append(": " + Math.max(0, flow))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + Math.max(0, chaos))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.greed").append(": " + Math.max(0, greed))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.might").append(": " + Math.max(0, might))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.corruption").append(": " + Math.max(0, corruption))));
        textWidth = Math.max(textWidth, screen.font.width(Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + Math.max(0, divinity))));

        // Position tooltip relative to the enchanting table GUI
        int x = 10;
        int y = (screen.height - 96) / 2; // Align vertically with the button

        // Ensure tooltip stays within screen bounds
        if (x + textWidth + padding * 2 > screen.width) {
            x = leftPos() - textWidth - padding * 2 - 5; // Move to left of button if it would go off-screen
        }
        if (y + textHeight + padding * 2 > screen.height) {
            y = screen.height - textHeight - padding * 2 - 5; // Adjust vertically if it would go off-screen
        }
        if (y < 0) {
            y = 5; // Prevent tooltip from going off the top of the screen
        }

        // Draw semi-translucent tooltip background
        guiGraphics.fill(x - padding, y - padding, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BG_COLOR);
        // Draw semi-translucent borders
        guiGraphics.fill(x - padding, y - padding, x + textWidth + padding, y - padding + 1, TOOLTIP_BORDER); // Top
        guiGraphics.fill(x - padding, y - padding, x - padding + 1, y + textHeight + padding, TOOLTIP_BORDER); // Left
        guiGraphics.fill(x - padding, y + textHeight + padding - 1, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BORDER); // Bottom
        guiGraphics.fill(x + textWidth + padding - 1, y - padding, x + textWidth + padding, y + textHeight + padding, TOOLTIP_BORDER); // Right

        // Draw attribute text
        y += 1; // Center text in tooltip box
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.mana").append(": " + Math.max(0, mana)), x, y, 0xFF000000 | ChatFormatting.DARK_BLUE.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.frost").append(": " + Math.max(0, frost)), x, y, 0xFF000000 | ChatFormatting.DARK_AQUA.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + Math.max(0, scorch)), x, y, 0xFF000000 | ChatFormatting.DARK_RED.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.flow").append(": " + Math.max(0, flow)), x, y, 0xFF000000 | ChatFormatting.AQUA.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + Math.max(0, chaos)), x, y, 0xFF000000 | ChatFormatting.DARK_GRAY.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.greed").append(": " + Math.max(0, greed)), x, y, 0xFF000000 | ChatFormatting.YELLOW.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.might").append(": " + Math.max(0, might)), x, y, 0xFF000000 | ChatFormatting.DARK_GREEN.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.corruption").append(": " + Math.max(0, corruption)), x, y, 0xFF000000 | ChatFormatting.RED.getColor()); y += 10;
        guiGraphics.drawString(screen.font, Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + Math.max(0, divinity)), x, y, 0xFF000000 | ChatFormatting.GOLD.getColor());
    }

    @Unique
    private boolean isOverButton(int mouseX, int mouseY) {
        return mouseX >= leftPos() && mouseX <= leftPos() + 14 && mouseY >= topPos() && mouseY <= topPos() + 14;
    }
}