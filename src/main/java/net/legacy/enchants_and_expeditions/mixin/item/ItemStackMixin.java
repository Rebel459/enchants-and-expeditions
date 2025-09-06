package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.legacy.enchants_and_expeditions.registry.EaEItems;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow @Final private PatchedDataComponentMap components;

    @Shadow public abstract ItemEnchantments getEnchantments();

    @Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
    private void canEnchant(CallbackInfoReturnable<Boolean> cir) {
        if (!EaEConfig.get.general.repeat_table_enchanting) return;
        ItemStack stack = ItemStack.class.cast(this);
        if (!cir.getReturnValue() && stack.isEnchanted() && (EnchantingHelper.enchantmentScore(stack) < EaEConfig.get.general.enchantment_limit)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getMaxStackSize", at = @At("TAIL"), cancellable = true)
    private void modifyWaterBottleStackSize(CallbackInfoReturnable<Integer> cir) {
        if (!EaEConfig.get.general.craftable_experience_bottles) return;
        if ((this.is(Items.POTION) || this.is(Items.SPLASH_POTION) || this.is(Items.LINGERING_POTION)) && cir.getReturnValue() == 1) {
            if (!this.getComponents().has(DataComponents.POTION_CONTENTS)) return;
            if (this.getComponents().get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
                cir.setReturnValue(64);
            }
        }
    }

    @Inject(method = "addDetailsToTooltip", at = @At("HEAD"))
    private void addDescription(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (this.is(EaEItemTags.ENCHANTING_POWER_PROVIDER)) {
            consumer.accept(Component.literal("")); // Line break

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
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("mana", mana));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEBlocks.ARCANE_BOOKSHELF.asItem())) {
                mana = "1.0";
                flow = "0.5";
                greed = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("mana", mana));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEBlocks.GLACIAL_BOOKSHELF.asItem())) {
                frost = "1.0";
                flow = "0.5";
                chaos = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("frost", frost));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEBlocks.INFERNAL_BOOKSHELF.asItem())) {
                scorch = "1.0";
                chaos = "0.5";
                greed = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("scorch", scorch));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_MANA)) {
                mana = "3.0";
                flow = "-1.0";
                chaos = "-1.0";
                greed = "-1.0";
                stability = "-1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("mana", mana));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_FROST)) {
                frost = "3.0";
                scorch = "-5.0";
                stability = "-1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_SCORCH)) {
                scorch = "3.0";
                frost = "-5.0";
                stability = "-1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("scorch", scorch));
                consumer.accept(attributeTooltip("frost", frost));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_FLOW)) {
                flow = "5.0";
                stability = "-3.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_flow", flow));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_CHAOS)) {
                chaos = "5.0";
                stability = "-3.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_chaos", chaos));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_GREED)) {
                greed = "5.0";
                stability = "-3.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_greed", greed));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_MIGHT)) {
                might = "5.0";
                stability = "-3.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", stability));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_might", might));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_STABILITY)) {
                stability = "3.0";
                divinity = "0.5";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("decreases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("decreases_curse_chance", stability));
                if (Screen.hasShiftDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, stability, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_POWER)) {
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("decreases_experience_requirements", String.valueOf(3)));
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
    private MutableComponent altarTooltip() {
        return Component.literal("").append(Component.translatable("desc.enchants_and_expeditions.when_on_altar").append(":").withColor(ChatFormatting.GRAY.getColor()));
    }

    @Unique
    private MutableComponent attributeTooltip(String attribute, String amount) {
        if (attribute == "increases_blessing_chance") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.blessing").withStyle(ChatFormatting.GOLD)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.chance").withStyle(ChatFormatting.BLUE));
        }
        if (attribute == "decreases_blessing_chance") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.blessing").withStyle(ChatFormatting.GOLD)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.chance").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "increases_curse_chance") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.curse").withStyle(ChatFormatting.RED)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.chance").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "decreases_curse_chance") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.curse").withStyle(ChatFormatting.RED)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.chance").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "increases_experience_requirements") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.power").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.requirements").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "decreases_experience_requirements") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.power").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.requirements").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "mana") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.mana").withColor(ChatFormatting.DARK_BLUE.getColor()).append(": " + amount));
        }
        else if (attribute == "frost") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.frost").withColor(ChatFormatting.DARK_AQUA.getColor()).append(": " + amount));
        }
        else if (attribute == "scorch") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.scorch").withColor(ChatFormatting.DARK_RED.getColor()).append(": " + amount));
        }
        else if (attribute == "increases_flow") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.flow").withStyle(ChatFormatting.AQUA)).append(" ");
        }
        else if (attribute == "increases_chaos") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.chaos").withStyle(ChatFormatting.DARK_GRAY)).append(" ");
        }
        else if (attribute == "increases_greed") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.greed").withStyle(ChatFormatting.YELLOW)).append(" ");
        }
        else if (attribute == "increases_might") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.might").withStyle(ChatFormatting.DARK_GREEN)).append(" ");
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
                .append(Component.literal(might + ", ").withStyle(ChatFormatting.DARK_GREEN))
                .append(Component.literal(stability + ", ").withStyle(ChatFormatting.RED))
                .append(Component.literal(divinity).withStyle(ChatFormatting.GOLD));
    }
}