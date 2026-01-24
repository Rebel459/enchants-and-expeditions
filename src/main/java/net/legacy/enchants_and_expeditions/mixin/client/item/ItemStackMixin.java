package net.legacy.enchants_and_expeditions.mixin.client.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.legacy.enchants_and_expeditions.registry.EaEItems;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.legacy.item_tooltips.util.ScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(Item item);

    @Shadow public abstract boolean is(TagKey<Item> tag);

    @Shadow public abstract Item getItem();

    @Shadow public abstract ItemEnchantments getEnchantments();

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
            else if (this.is(EaEItems.TOME_OF_MANA)) {
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
            else if (this.is(EaEItems.TOME_OF_FROST)) {
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
            else if (this.is(EaEItems.TOME_OF_SCORCH)) {
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
            else if (this.is(EaEItems.TOME_OF_FLOW)) {
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
            else if (this.is(EaEItems.TOME_OF_CHAOS)) {
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
            else if (this.is(EaEItems.TOME_OF_GREED)) {
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
            else if (this.is(EaEItems.TOME_OF_MIGHT)) {
                might = "5.0";
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
            else if (this.is(EaEItems.TOME_OF_STABILITY)) {
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
            else if (this.is(EaEItems.TOME_OF_POWER)) {
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
        return Component.literal(" ")
                .append(Component.literal(mana + ", ").withStyle(ChatFormatting.DARK_BLUE))
                .append(Component.literal(frost + ", ").withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal(scorch + ", ").withStyle(ChatFormatting.DARK_RED))
                .append(Component.literal(flow + ", ").withStyle(ChatFormatting.AQUA))
                .append(Component.literal(chaos + ", ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(greed + ", ").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(might + ", ").withStyle(ChatFormatting.DARK_GREEN))
                .append(Component.literal(corruption + ", ").withStyle(ChatFormatting.RED))
                .append(Component.literal(divinity).withStyle(ChatFormatting.GOLD));
    }
}