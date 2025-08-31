package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(Item item);

    @Shadow public abstract DataComponentMap getComponents();

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
}