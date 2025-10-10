package net.legacy.enchants_and_expeditions.mixin.item;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(Item item);

    @Shadow public abstract DataComponentMap getComponents();

    @Shadow public abstract boolean is(TagKey<Item> tag);

    @Shadow public abstract Item getItem();

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
}