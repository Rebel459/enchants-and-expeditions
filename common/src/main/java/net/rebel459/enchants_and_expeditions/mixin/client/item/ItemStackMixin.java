package net.rebel459.enchants_and_expeditions.mixin.client.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.rebel459.enchants_and_expeditions.registry.EaEBlocks;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.registry.EaEItems;
import net.rebel459.enchants_and_expeditions.tag.EaEItemTags;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.util.EnchantmentSlots;
import net.rebel459.item_tooltips.util.ScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(value = ItemStack.class, priority = 999)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Shadow public abstract ItemEnchantments getEnchantments();

    @Shadow
    public abstract boolean is(Predicate<Holder<Item>> item);

    @Inject(method = "addAttributeTooltips", at = @At("TAIL"))
    private void addEnchantingSlots(Consumer<Component> consumer, TooltipDisplay display, @Nullable Player player, CallbackInfo ci) {
        ItemStack stack = ItemStack.class.cast(this);
        if (EnchantingHelper.hasSlots(stack) && !stack.has(DataComponents.STORED_ENCHANTMENTS)) {
            EnchantmentSlots slots = stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get());
            consumer.accept(Component.literal("Slots Used: " + (slots.getTotal() - slots.getRemaining(stack)) + " / " + slots.getTotal()).withStyle(ChatFormatting.GRAY));
        }
    }

    @Unique
    private static String format(double value) {
        return ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value);
    }

    @Inject(method = "addDetailsToTooltip", at = @At("TAIL"))
    private void addBookAttributes(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        ItemStack stack = ItemStack.class.cast(this);
        if (stack.has(DataComponents.STORED_ENCHANTMENTS)) {
            List<Double> bookAttributes = EnchantingHelper.getBookAttributes(stack.get(DataComponents.STORED_ENCHANTMENTS));
            String mana = format(bookAttributes.getFirst());
            String frost = format(bookAttributes.get(1));
            String scorch = format(bookAttributes.get(2));
            String flow = format(bookAttributes.get(3));
            String chaos = format(bookAttributes.get(4));
            String greed = format(bookAttributes.get(5));
            String might = format(bookAttributes.get(6));
            String corruption = format(bookAttributes.get(7));
            String divinity = format(bookAttributes.getLast());
            if (ScreenHelper.hasKeyDown()) {
                consumer.accept(Component.literal(""));
                consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity, true));
            }
        }
    }

    @Inject(method = "addDetailsToTooltip", at = @At("HEAD"))
    private void addAttributes(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (this.is(item -> item.is(EaEItemTags.ENCHANTING_POWER_PROVIDER))) {
            consumer.accept(Component.literal("")); // Line break

            String mana = "0";
            String frost = "0";
            String scorch = "0";
            String flow = "0";
            String chaos = "0";
            String greed = "0";
            String might = "0";
            String corruption = "0";
            String divinity = "0";
            if (this.is(Blocks.BOOKSHELF.asItem())) {
                mana = "0.2";
                frost = "0.2";
                scorch = "0.2";
                flow = "0.2";
                chaos = "0.2";
                greed = "0.2";
                might = "0.2";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("mana", mana));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEBlocks.ARCANE_BOOKSHELF.asItem())) {
                mana = "1.0";
                flow = "0.5";
                greed = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("mana", mana));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEBlocks.GLACIAL_BOOKSHELF.asItem())) {
                frost = "1.0";
                flow = "0.5";
                chaos = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("frost", frost));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEBlocks.INFERNAL_BOOKSHELF.asItem())) {
                scorch = "1.0";
                chaos = "0.5";
                greed = "0.5";
                might = "0.25";
                consumer.accept(placedTooltip());
                consumer.accept(attributeTooltip("scorch", scorch));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_MANA.get())) {
                mana = "3.0";
                chaos = "-5.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("mana", mana));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_FROST.get())) {
                frost = "3.0";
                scorch = "-5.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_SCORCH.get())) {
                scorch = "3.0";
                frost = "-5.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("scorch", scorch));
                consumer.accept(attributeTooltip("frost", frost));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_FLOW.get())) {
                flow = "5.0";
                greed = "-3.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_flow", flow));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_CHAOS.get())) {
                chaos = "5.0";
                mana = "-3.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_chaos", chaos));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_GREED.get())) {
                greed = "5.0";
                flow = "-3.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_greed", greed));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_MIGHT.get())) {
                might = "7.0";
                chaos = "-1.0";
                flow = "-1.0";
                greed = "-1.0";
                scorch = "-1.0";
                frost = "-1.0";
                mana = "-1.0";
                corruption = "1.0";
                divinity = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_blessing_chance", divinity));
                consumer.accept(attributeTooltip("increases_curse_chance", corruption));
                consumer.accept(attributeTooltip("increases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("increases_might", might));
                consumer.accept(attributeTooltip("mana", mana));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_STABILITY.get())) {
                corruption = "-1.0";
                might = "-5.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("decreases_curse_chance", corruption));
                consumer.accept(attributeTooltip("decreases_enchanting_power", String.valueOf(3)));
                consumer.accept(attributeTooltip("decreases_experience_requirements", String.valueOf(3)));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
                }
            }
            else if (this.is(EaEItems.TOME_OF_POWER.get())) {
                might = "1.0";
                chaos = "1.0";
                flow = "1.0";
                greed = "1.0";
                scorch = "1.0";
                frost = "1.0";
                mana = "1.0";
                consumer.accept(altarTooltip());
                consumer.accept(attributeTooltip("increases_enchanting_power", String.valueOf(3)));
                consumer.accept(attributeTooltip("decreases_experience_requirements", String.valueOf(3)));
                consumer.accept(attributeTooltip("mana", mana));
                consumer.accept(attributeTooltip("frost", frost));
                consumer.accept(attributeTooltip("scorch", scorch));
                if (ScreenHelper.Tooltip.hasKeyDown()) {
                    consumer.accept(Component.literal(""));
                    consumer.accept(statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity));
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
        else if (attribute == "increases_enchanting_power") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.enchanting").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.power").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "decreases_enchanting_power") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.enchanting").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.power").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "increases_experience_requirements") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.increases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.experience").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.requirements").withStyle(ChatFormatting.BLUE));
        }
        else if (attribute == "decreases_experience_requirements") {
            return Component.literal(" ").append(Component.translatable("desc.enchants_and_expeditions.decreases").withStyle(ChatFormatting.BLUE)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.experience").withStyle(ChatFormatting.GREEN)).append(" ").append(Component.translatable("desc.enchants_and_expeditions.requirements").withStyle(ChatFormatting.BLUE));
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
    private MutableComponent statTooltip(String mana, String frost, String scorch, String flow, String chaos, String greed, String might, String corruption, String divinity) {
        return statTooltip(mana, frost, scorch, flow, chaos, greed, might, corruption, divinity, false);
    }

    @Unique
    private MutableComponent statTooltip(String mana, String frost, String scorch, String flow, String chaos, String greed, String might, String corruption, String divinity, boolean skipZero) {
        MutableComponent component = Component.literal(" ");
        boolean hasPrevious = false;

        hasPrevious = appendStat(component, hasPrevious, mana, ChatFormatting.DARK_BLUE, skipZero);
        hasPrevious = appendStat(component, hasPrevious, frost, ChatFormatting.DARK_AQUA, skipZero);
        hasPrevious = appendStat(component, hasPrevious, scorch, ChatFormatting.DARK_RED, skipZero);
        hasPrevious = appendStat(component, hasPrevious, flow, ChatFormatting.AQUA, skipZero);
        hasPrevious = appendStat(component, hasPrevious, chaos, ChatFormatting.DARK_GRAY, skipZero);
        hasPrevious = appendStat(component, hasPrevious, greed, ChatFormatting.YELLOW, skipZero);
        hasPrevious = appendStat(component, hasPrevious, might, ChatFormatting.DARK_GREEN, skipZero);
        hasPrevious = appendStat(component, hasPrevious, corruption, ChatFormatting.RED, skipZero);
        appendStat(component, hasPrevious, divinity, ChatFormatting.GOLD, skipZero);

        return component;
    }

    @Unique
    private boolean appendStat(MutableComponent component, boolean hasPrevious, String attribute, ChatFormatting formatting, boolean skipZero) {
        if (!(!skipZero || !Objects.equals(attribute, "0"))) {
            return hasPrevious;
        }

        if (hasPrevious) {
            component.append(Component.literal(", "));
        }

        component.append(Component.literal(attribute).withStyle(formatting));
        return true;
    }

    @Unique
    private boolean is(Item item) {
        return this.is(holder -> holder.value() == item);
    }
}