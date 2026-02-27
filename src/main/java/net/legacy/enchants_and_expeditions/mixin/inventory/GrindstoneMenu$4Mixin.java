package net.legacy.enchants_and_expeditions.mixin.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public abstract class GrindstoneMenu$4Mixin {

    @Inject(method = "getExperienceFromItem(Lnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;is(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
    private void modifyGrindstoneExperience(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        int i = 0;
        ItemEnchantments itemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);

        for(Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            int j = entry.getIntValue();
            if (!holder.is(EnchantmentTags.CURSE) && (!holder.is(EaEEnchantmentTags.BLESSING) || EnchantingHelper.enchantmentScore(itemStack) == 0)) {
                i += holder.value().getMinCost(j);
            }
        }

        cir.setReturnValue(i);
    }
}