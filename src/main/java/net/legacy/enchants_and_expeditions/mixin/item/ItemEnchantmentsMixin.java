package net.legacy.enchants_and_expeditions.mixin.item;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.selectEnchantment;

@Mixin(ItemEnchantments.class)
public abstract class ItemEnchantmentsMixin {

    @Shadow @Final
    Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    private void EaE$enchantmentTooltipOrder(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter, CallbackInfo ci) {
        if (!EaEConfig.get.misc.ordered_enchantment_tooltips) return;

        HolderLookup.Provider provider = context.registries();

        HolderSet<Enchantment> blessingSet = ItemEnchantments.getTagOrEmpty(provider, Registries.ENCHANTMENT, EaEEnchantmentTags.BLESSING);
        HolderSet<Enchantment> enchantmentSet = ItemEnchantments.getTagOrEmpty(provider, Registries.ENCHANTMENT, EaEEnchantmentTags.ENCHANTMENT_ORDER);
        HolderSet<Enchantment> curseSet = ItemEnchantments.getTagOrEmpty(provider, Registries.ENCHANTMENT, EnchantmentTags.CURSE);

        for (Holder<Enchantment> holder : blessingSet) {
            int i = this.enchantments.getInt(holder);
            if (i > 0) {
                tooltipAdder.accept(Enchantment.getFullname(holder, i));
            }
        }

        for (Holder<Enchantment> holder : enchantmentSet) {
            int i = this.enchantments.getInt(holder);
            if (i > 0 && !blessingSet.contains(holder) && !curseSet.contains(holder)) {
                tooltipAdder.accept(Enchantment.getFullname(holder, i));
            }
        }
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : this.enchantments.object2IntEntrySet()) {
            Holder<Enchantment> holder2 = entry.getKey();
            if (!blessingSet.contains(holder2) && !enchantmentSet.contains(holder2) && !curseSet.contains(holder2)) {
                tooltipAdder.accept(Enchantment.getFullname(entry.getKey(), entry.getIntValue()));
            }
        }

        for (Holder<Enchantment> holder : curseSet) {
            int i = this.enchantments.getInt(holder);
            if (i > 0) {
                tooltipAdder.accept(Enchantment.getFullname(holder, i));
            }
        }

        ci.cancel();
    }
}