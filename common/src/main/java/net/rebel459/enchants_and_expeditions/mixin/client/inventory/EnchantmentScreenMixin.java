package net.rebel459.enchants_and_expeditions.mixin.client.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditionsClient;
import net.rebel459.enchants_and_expeditions.client.EnchantingAttributesHelper;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.network.EnchantingAttributes;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.unified.platform.client.UnifiedClientHelpers;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {
    @Unique
    private static final int REROLL_CLUE = -2;
    @Unique
    private static final int NO_REROLL_CLUE = -3;

    @Shadow
    @Final
    private static Identifier[] ENABLED_LEVEL_SPRITES;
    @Shadow
    @Final
    private static Identifier[] DISABLED_LEVEL_SPRITES;
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Unique
    private boolean EaE$requestedOnce;

    @Unique
    private static final int TOOLTIP_BG_COLOR = 0xA0100010; // Semi-translucent background
    @Unique
    private static final int TOOLTIP_BORDER = 0xA028007F; // Semi-translucent darker purple border
    @Unique
    private static final FontDescription ENABLED_BADGE_FONT = new FontDescription.Resource(EnchantsAndExpeditions.id("enchant_badge_enabled"));
    @Unique
    private static final FontDescription DISABLED_BADGE_FONT = new FontDescription.Resource(EnchantsAndExpeditions.id("enchant_badge_disabled"));
    @Unique
    private static final int ATTRIBUTE_PANEL_PADDING = 4;
    @Unique
    private static final int ATTRIBUTE_PANEL_MARGIN = 10;

    @Inject(method = "init", at = @At("TAIL"))
    private void EaE$requestAttributesOnce(CallbackInfo ci) {
        if (!EaE$requestedOnce) {
            EaE$requestedOnce = true;
            if (EnchantsAndExpeditions.debug) LOGGER.info("[EaE] Client sending C2S Request from EnchantmentScreen.init");
            UnifiedClientHelpers.NETWORKING.send(new EnchantingAttributes.Request());
        }
    }

    @Inject(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getGoldCount()I", shift =  At.Shift.AFTER), cancellable = true)
    private void EaE$reroll(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        EnchantmentMenu menu = screen.getMenu();
        Player player = screen.minecraft.player;
        boolean hasInfiniteMaterials = player.hasInfiniteMaterials();
        int i = 1;
        boolean isReroll = menu.enchantClue[i] == REROLL_CLUE;
        boolean isNoReroll = menu.enchantClue[i] == NO_REROLL_CLUE;
        if (isReroll || isNoReroll) {
            int rerollXpRequirement = menu.levelClue[i];
            int rerollCost = EnchantingHelper.calculateEnchantingCost(rerollXpRequirement, 2);
            int xo = (screen.width - screen.imageWidth) / 2;
            int yo = (screen.height - screen.imageHeight) / 2;
            int leftPos = xo + 60;
            int leftPosText = leftPos + 20;
            int xx = mouseX - (xo + 60);
            int yy = mouseY - (yo + 14 + 19 * i);
            boolean hasEnoughLapis = menu.getGoldCount() >= rerollCost;
            boolean hasEnoughXp = player.experienceLevel >= rerollXpRequirement;
            boolean canReroll = isReroll && (hasInfiniteMaterials || hasEnoughLapis && hasEnoughXp);
            boolean hovered = xx >= 0 && yy >= 0 && xx < 108 && yy < 19;
            int col = isNoReroll ? ChatFormatting.RED.getColor() : 6839882;

            if (!canReroll) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EnchantmentScreen.ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                if (!isNoReroll) {
                    col = (col & 16711422) >> 1;
                }
            } else {
                if (hovered) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EnchantmentScreen.ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                    graphics.requestCursor(CursorTypes.POINTING_HAND);
                    col = 16777088;
                } else {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EnchantmentScreen.ENCHANTMENT_SLOT_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                }
            }

            if (hovered) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(
                        isNoReroll ? "container.enchants_and_expeditions.no_reroll" : "container.enchants_and_expeditions.reroll"
                ).withStyle(isNoReroll ? ChatFormatting.RED : ChatFormatting.GREEN));
                if (isReroll) {
                    int rerolls = 3 - EnchantingHelper.getRerolls(menu.getSlot(0).getItem());
                    Component rerollsRemaining = Component.translatable("container.enchants_and_expeditions.rerolls_remaining");
                    if (rerolls == 1) rerollsRemaining = Component.translatable("container.enchants_and_expeditions.reroll_remaining");
                    tooltip.add(Component.literal(rerolls + " ").withStyle(ChatFormatting.GRAY).append(rerollsRemaining).withStyle(ChatFormatting.GRAY));
                    if (!hasInfiniteMaterials) {
                    tooltip.add(CommonComponents.SPACE);
                    if (!hasEnoughXp) {
                        tooltip.add(Component.translatable("container.enchant.level.requirement", rerollXpRequirement).withStyle(ChatFormatting.RED));
                    } else {
                        tooltip.add(Component.translatable("container.enchant.lapis.many", rerollCost).withStyle(hasEnoughLapis ? ChatFormatting.GRAY : ChatFormatting.RED));
                        tooltip.add(Component.translatable("container.enchant.level.many", rerollCost).withStyle(ChatFormatting.GRAY));
                    }
                    }
                }
                graphics.setComponentTooltipForNextFrame(screen.font, tooltip, mouseX, mouseY);
            }

            int sprite = i;
            if (rerollXpRequirement < 20 && rerollXpRequirement > 0) {
                sprite = 0;
            }
            if (rerollXpRequirement >= 20) {
                sprite = 1;
            }
            if (rerollXpRequirement >= 30) {
                sprite = 2;
            }

            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    canReroll ? ENABLED_LEVEL_SPRITES[sprite] : DISABLED_LEVEL_SPRITES[sprite],
                    leftPos + 1,
                    yo + 15 + 19 * i,
                    16,
                    16
            );
            if (!isNoReroll) {
                this.EaE$drawBadgeGlyph(graphics, leftPos + 1, yo + 15 + 19 * i, rerollCost, canReroll ? ENABLED_BADGE_FONT : DISABLED_BADGE_FONT);
            }
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EnchantmentScreen.ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14, 108, 19);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EnchantmentScreen.ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 38, 108, 19);
            FormattedText message = EnchantmentNames.getInstance().getRandomName(screen.font, 86);
            graphics.textWithWordWrap(screen.font, message, leftPosText, yo + 16 + 19 * i, 86, col | 0xFF000000, false);
            EaE$enchantingTableInterface(graphics, mouseX, mouseY, a, ci);
            ci.cancel();
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

    @Unique
    private static EaEConfig.PanelVisibility getPanelVisibility() {
        return EaEConfig.get().misc.panel_visibility;
    }

    @Unique
    private static void setPanelVisibility(EaEConfig.PanelVisibility visibility) {
        var holder = AutoConfig.getConfigHolder(EaEConfig.class);
        EaEConfig config = holder.getConfig();
        config.misc.panel_visibility = visibility;
        holder.save();
    }

    @Inject(method = "mouseClicked", at = @At("TAIL"))
    private void EaE$enchantingTableClicked(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        Player player = screen.minecraft.player;
        int mouseX = (int) event.x();
        int mouseY = (int) event.y();
        if (isOverButton(mouseX, mouseY)) {
            switch (getPanelVisibility()) {
                case BOTH -> setPanelVisibility(EaEConfig.PanelVisibility.NONE);
                case LEFT -> setPanelVisibility(EaEConfig.PanelVisibility.BOTH);
                case NONE -> setPanelVisibility(EaEConfig.PanelVisibility.LEFT);
            }
            if (player != null) player.playSound(SoundEvents.UI_BUTTON_CLICK.value());
        }
    }

    @ModifyExpressionValue(
            method = "extractBackground",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/EnchantmentMenu;costs:[I"
            )
    )
    private int[] EaE$fixDisabledSlots(int[] costs, @Local(name = "i") int i) {
        EnchantmentScreen screen = (EnchantmentScreen) (Object) this;

        if (screen.getMenu().enchantClue[i] != -1) {
            return costs;
        }

        int[] copy = costs.clone();
        copy[i] = 0;
        return copy;
    }

    @WrapOperation(
            method = "extractBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
                    ordinal = 2
            )
    )
    private void EaE$drawDisabledBadgeText(GuiGraphicsExtractor graphics, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, Operation<Void> original, @Local(name = "i") int i) {

        original.call(graphics, renderPipeline, location, x, y, width, height);
        this.EaE$drawBadgeGlyph(graphics, x, y, EnchantingHelper.calculateEnchantingCost(EnchantmentScreen.class.cast(this).getMenu().costs[i], i), DISABLED_BADGE_FONT);
    }

    @WrapOperation(
            method = "extractBackground",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
                    ordinal = 5
            )
    )
    private void EaE$drawEnabledBadgeText(GuiGraphicsExtractor graphics, RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, Operation<Void> original, @Local(name = "i") int i) {
        original.call(graphics, renderPipeline, location, x, y, width, height);
        this.EaE$drawBadgeGlyph(graphics, x, y, EnchantingHelper.calculateEnchantingCost(EnchantmentScreen.class.cast(this).getMenu().costs[i], i), ENABLED_BADGE_FONT);
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void EaE$enchantingTableInterface(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);

        // Render the 16x16 texture at the top-left of the enchanting table interface
        if (isOverButton(mouseX, mouseY)) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, EnchantsAndExpeditions.id("textures/gui/attributes_hovered.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, EnchantsAndExpeditions.id("textures/gui/attributes.png"), leftPos(), topPos(), 0, 0, textureSize, textureSize, textureSize, textureSize);
        }

        if (getPanelVisibility() == EaEConfig.PanelVisibility.NONE) return;

        EnchantingAttributes.Attributes enchantingAttributes = EnchantsAndExpeditionsClient.getClientEnchantingAttributes();
        if (enchantingAttributes == null) {
            graphics.text(screen.getFont(), Component.literal("[EaE] awaiting attribute sync..."), leftPos() + 20, topPos() + 20, ChatFormatting.GRAY.getColor());
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
        int padding = ATTRIBUTE_PANEL_PADDING;
        int textHeight = 9 * 10; // 9 attributes, 10 pixels each (9 pixels font height + 1 spacing)
        int textWidth = 100; // Base width, adjusted based on longest text

        // Calculate exact width using Font#width
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.mana").append(": " + Math.max(0, mana))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.frost").append(": " + Math.max(0, frost))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + Math.max(0, scorch))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.flow").append(": " + Math.max(0, flow))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + Math.max(0, chaos))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.greed").append(": " + Math.max(0, greed))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.might").append(": " + Math.max(0, might))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.corruption").append(": " + Math.max(0, corruption))));
        textWidth = Math.max(textWidth, screen.getFont().width(Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + Math.max(0, divinity))));

        // Position tooltip relative to the enchanting table GUI
        int x = ATTRIBUTE_PANEL_MARGIN;
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

        int leftPanelX = x;
        int leftPanelY = y;

        EaE$drawPanelBackground(graphics, leftPanelX, leftPanelY, textWidth, textHeight, padding);

        int symbolX = leftPanelX - 4;
        int symbolY = leftPanelY + 1;
        x = leftPanelX + 11;

        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("mana"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("frost"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("scorch"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("flow"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("chaos"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("greed"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("might"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("corruption"), symbolX, symbolY, 0xFF000000); symbolY += 10;
        graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol("divinity"), symbolX, symbolY, 0xFF000000);;

        // Draw attribute text
        int leftTextY = leftPanelY + 1; // Center text in tooltip box
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.mana").append(": " + Math.max(0, mana)), x, leftTextY, 0xFF000000 | ChatFormatting.DARK_BLUE.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.frost").append(": " + Math.max(0, frost)), x, leftTextY, 0xFF000000 | ChatFormatting.DARK_AQUA.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.scorch").append(": " + Math.max(0, scorch)), x, leftTextY, 0xFF000000 | EnchantingAttributesHelper.ORANGE); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.flow").append(": " + Math.max(0, flow)), x, leftTextY, 0xFF000000 | ChatFormatting.AQUA.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.chaos").append(": " + Math.max(0, chaos)), x, leftTextY, 0xFF000000 | ChatFormatting.DARK_GRAY.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.greed").append(": " + Math.max(0, greed)), x, leftTextY, 0xFF000000 | ChatFormatting.YELLOW.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.might").append(": " + Math.max(0, might)), x, leftTextY, 0xFF000000 | ChatFormatting.DARK_GREEN.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.corruption").append(": " + Math.max(0, corruption)), x, leftTextY, 0xFF000000 | ChatFormatting.RED.getColor()); leftTextY += 10;
        graphics.text(screen.getFont(), Component.translatable("desc.enchants_and_expeditions.divinity").append(": " + Math.max(0, divinity)), x, leftTextY, 0xFF000000 | ChatFormatting.GOLD.getColor());

        if (getPanelVisibility() != EaEConfig.PanelVisibility.BOTH) return;

        List<EaE$InfoEntry> entries = List.of(
                new EaE$InfoEntry(
                        "treasure",
                        Component.translatable("container.enchants_and_expeditions.treasure"),
                        EnchantingAttributesHelper.LIGHT_GREEN,
                        Component.translatable("container.enchants_and_expeditions.treasure_desc")
                ),
                new EaE$InfoEntry(
                        "powerful",
                        Component.translatable("container.enchants_and_expeditions.powerful"),
                        ChatFormatting.BLUE.getColor(),
                        Component.translatable("container.enchants_and_expeditions.powerful_desc")
                ),
                new EaE$InfoEntry(
                        "generic",
                        Component.translatable("container.enchants_and_expeditions.generic"),
                        ChatFormatting.WHITE.getColor(),
                        Component.translatable("container.enchants_and_expeditions.generic_desc")
                )
        );

        final int rightPanelEntrySpacing = 2;
        final int rightPanelTitleDescSpacing = 1;
        final int rightPanelTextIndent = 9;
        final float rightPanelDescriptionScale = 0.6F;

        int rightPanelWidth = textWidth;
        int rightPanelTextWidth = rightPanelWidth - rightPanelTextIndent;
        int rightPanelContentHeight = 0;
        for (EaE$InfoEntry entry : entries) {
            rightPanelContentHeight += screen.getFont().lineHeight;
            rightPanelContentHeight += rightPanelTitleDescSpacing;
            rightPanelContentHeight += EaE$getWrappedTextHeight(screen, entry.description(), rightPanelTextWidth, rightPanelDescriptionScale);
            rightPanelContentHeight += rightPanelEntrySpacing;
        }
        rightPanelContentHeight -= rightPanelEntrySpacing;
        int rightPanelHeight = Math.max(textHeight, rightPanelContentHeight);

        int rightPanelX = screen.width - leftPanelX - rightPanelWidth;
        int rightPanelY = leftPanelY;
        if (rightPanelY + rightPanelHeight > screen.height - 5) {
            rightPanelY = screen.height - rightPanelHeight - 5;
        }
        if (rightPanelY < 5) {
            rightPanelY = 5;
        }

        EaE$drawPanelBackground(graphics, rightPanelX, rightPanelY, rightPanelWidth, rightPanelHeight, padding);

        int rightPanelSymbolX = rightPanelX - 4;
        int titleX = rightPanelX + rightPanelTextIndent;
        int descriptionX = titleX - 4;
        int textY = rightPanelY + 1;
        for (EaE$InfoEntry entry : entries) {
            graphics.text(screen.getFont(), EnchantingAttributesHelper.addAttributeSymbol(entry.attribute(), true), rightPanelSymbolX, textY, 0xFF000000);
            graphics.text(screen.getFont(), entry.title(), titleX, textY, 0xFF000000 | entry.color());
            textY += screen.getFont().lineHeight + rightPanelTitleDescSpacing;
            textY = EaE$drawWrappedText(graphics, screen, entry.description(), descriptionX, textY, rightPanelTextWidth, 0xFF000000 | ChatFormatting.GRAY.getColor(), rightPanelDescriptionScale);
            textY += rightPanelEntrySpacing;
        }
    }

    @Unique
    private void EaE$drawPanelBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int padding) {
        graphics.fill(x - padding, y - padding, x + width + padding, y + height + padding, TOOLTIP_BG_COLOR);
        graphics.fill(x - padding, y - padding, x + width + padding, y - padding + 1, TOOLTIP_BORDER);
        graphics.fill(x - padding, y - padding, x - padding + 1, y + height + padding, TOOLTIP_BORDER);
        graphics.fill(x - padding, y + height + padding - 1, x + width + padding, y + height + padding, TOOLTIP_BORDER);
        graphics.fill(x + width + padding - 1, y - padding, x + width + padding, y + height + padding, TOOLTIP_BORDER);
    }

    @Unique
    private int EaE$getWrappedTextHeight(EnchantmentScreen screen, Component text, int width, float scale) {
        int scaledWidth = Math.max(1, Math.round(width / scale));
        int lines = screen.getFont().split(text, scaledWidth).size();
        return Math.round(lines * screen.getFont().lineHeight * scale);
    }

    @Unique
    private int EaE$drawWrappedText(GuiGraphicsExtractor graphics, EnchantmentScreen screen, Component text, int x, int y, int width, int color, float scale) {
        int scaledWidth = Math.max(1, Math.round(width / scale));
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        int scaledY = 0;
        for (FormattedCharSequence line : screen.getFont().split(text, scaledWidth)) {
            graphics.text(screen.getFont(), line, 0, scaledY, color, false);
            scaledY += screen.getFont().lineHeight;
        }
        graphics.pose().popMatrix();
        return y + Math.round(scaledY * scale);
    }

    @Unique
    private record EaE$InfoEntry(String attribute, Component title, int color, Component description) {
    }

    @Unique
    private boolean isOverButton(int mouseX, int mouseY) {
        return mouseX >= leftPos() && mouseX <= leftPos() + 14 && mouseY >= topPos() && mouseY <= topPos() + 14;
    }

    @Unique
    private static final int BADGE_DIGIT_STEP = 6;
    @Unique
    private static final int BADGE_TEXT_OFFSET_X = 0;
    @Unique
    private static final int BADGE_TEXT_OFFSET_Y = 0;

    @Unique
    private void EaE$drawBadgeGlyph(GuiGraphicsExtractor graphics, int x, int y, int value, FontDescription fontId) {
        EnchantmentScreen screen = EnchantmentScreen.class.cast(this);
        String text = Integer.toString(value);

        int totalWidth = (text.length() - 1) * BADGE_DIGIT_STEP + 16;
        int startX = x + (16 - totalWidth) / 2 + BADGE_TEXT_OFFSET_X;
        int drawY = y + BADGE_TEXT_OFFSET_Y;

        for (int index = 0; index < text.length(); index++) {
            Component glyph = Component.literal(String.valueOf(text.charAt(index)))
                    .withStyle(style -> style.withFont(fontId));

            graphics.text(
                    screen.getFont(),
                    glyph,
                    startX + index * BADGE_DIGIT_STEP,
                    drawY,
                    0xFFFFFFFF,
                    false
            );
        }
    }

    @ModifyVariable(method = "extractRenderState", at = @At(value = "STORE"), name = "cost")
    private int showEnchantingCost(int cost, @Local(ordinal = 4) int minLevel) {
        return EnchantingHelper.calculateEnchantingCost(minLevel, cost);
    }

    @ModifyConstant(method = "extractBackground", constant = @Constant(intValue = 1, ordinal = 0))
    private int enchantmentSlotDisabledTexture(int original, @Local(name = "i") int i, @Local(name = "cost") int cost) {
        return EnchantingHelper.calculateEnchantingCost(cost, original) - i;
    }
}
