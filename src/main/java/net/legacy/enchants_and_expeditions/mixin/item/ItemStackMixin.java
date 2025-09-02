package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(Item item);

    @Shadow public abstract DataComponentMap getComponents();

    @Shadow public abstract boolean is(TagKey<Item> tag);

    @Shadow public abstract Item getItem();

    @Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
    private void canEnchant(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = ItemStack.class.cast(this);
        if (!cir.getReturnValue() && stack.isEnchanted() && (EnchantingHelper.enchantmentScore(stack) < EaEConfig.get.enchanting.enchantment_limit)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void modifyWaterBottleStackSize(CallbackInfoReturnable<Integer> cir) {
        if (!EaEConfig.get.items.craftable_experience_bottles) return;
        if (this.is(Items.POTION) || this.is(Items.SPLASH_POTION) || this.is(Items.LINGERING_POTION)) {
            if (!this.getComponents().has(DataComponents.POTION_CONTENTS)) return;
            if (this.getComponents().get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
                cir.setReturnValue(64);
            }
        }
    }

    @Inject(method = "addDetailsToTooltip", at = @At("HEAD"), cancellable = true)
    private void addDescription(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (this.is(EaEItemTags.ENCHANTING_POWER_PROVIDER)) {
            String mana = "0";
            String frost = "0";
            String scorch = "0";
            String flow = "0";
            String chaos = "0";
            String greed = "0";
            String might = "0";
            String stability = "0";
            String divinity = "0";
            if (this.is(Blocks.BOOKSHELF.asItem())) {
                mana = "0.5";
                frost = "0.25";
                scorch = "0.25";
                flow = "0.25";
                chaos = "0.25";
                greed = "0.25";
                might = "0.25";
                stability = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("mana", mana));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
        }
    }

    @Unique
    private MutableComponent placedTooltip() {
        return Component.literal("").append(Component.translatable("desc.enchants_and_expeditions.when_placed").append(":").withColor(ChatFormatting.GRAY.getColor()));
    }

    @Unique
    private MutableComponent attributeTooltip(String attribute, String amount) {
        if (attribute == "divinity") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases_divinity").withColor(ChatFormatting.GOLD.getColor()));
        }
        if (attribute == "stability") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases_stability").withColor(ChatFormatting.RED.getColor()));
        }
        if (attribute == "mana") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.mana").withColor(ChatFormatting.DARK_BLUE.getColor()).append(": " + amount));
        }
        if (attribute == "frost") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.frost").withColor(ChatFormatting.DARK_AQUA.getColor()).append(": " + amount));
        }
        if (attribute == "scorch") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.scorch").withColor(ChatFormatting.DARK_RED.getColor()).append(": " + amount));
        }
        else return Component.literal("");
    }

    @Unique
    private MutableComponent statTooltip(String mana, String frost, String scorch, String flow, String chaos, String greed, String might, String stability, String divinity) {
        return Component.literal(" ")
                .append(Component.literal(mana + ", ").withStyle(ChatFormatting.DARK_BLUE))
                .append(Component.literal(frost + ", ").withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal(scorch + ", ").withStyle(ChatFormatting.DARK_RED))
                .append(Component.literal(flow + ", ").withStyle(ChatFormatting.AQUA))
                .append(Component.literal(chaos + ", ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(greed + ", ").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(might + ", ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(stability + ", ").withStyle(ChatFormatting.RED))
                .append(Component.literal(divinity).withStyle(ChatFormatting.GOLD));
    }
}