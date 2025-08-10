package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @Shadow @Final private Container resultSlots;

    @Shadow @Final
    Container repairSlots;

    @Inject(method = "createResult", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$disenchantImbuedBook(CallbackInfo ci) {
        ItemStack resultItem = this.repairSlots.getItem(0).copy();
        ItemStack additionItem = this.repairSlots.getItem(1).copy();
        if (resultItem.is(Items.ENCHANTED_BOOK) && (additionItem.isEmpty() || additionItem.is(Items.ENCHANTED_BOOK))) {
            EnchantmentHelper.updateEnchantments(resultItem, mutable -> mutable.removeIf(holder -> !holder.is(EnchantmentTags.CURSE)));
            if (!resultItem.isEnchanted()) {
                ItemStack storedComponents = resultItem.copy();
                storedComponents.remove(DataComponents.DAMAGE);
                storedComponents.remove(DataComponents.MAX_DAMAGE);
                storedComponents.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
                storedComponents.remove(DataComponents.REPAIRABLE);
                storedComponents.remove(DataComponents.MAX_STACK_SIZE);
                storedComponents.remove(DataComponents.STORED_ENCHANTMENTS);
                if (!(storedComponents.getComponents().has(DataComponents.ENCHANTABLE) && storedComponents.getComponents().get(DataComponents.ENCHANTABLE).value() == 15)) {
                    storedComponents.remove(DataComponents.ITEM_NAME);
                    storedComponents.remove(DataComponents.ITEM_MODEL);
                    storedComponents.remove(DataComponents.RARITY);
                }
                else {
                    storedComponents.set(DataComponents.RARITY, Rarity.UNCOMMON);
                }
                resultItem = new ItemStack(Items.BOOK);
                resultItem.applyComponents(storedComponents.getComponents());
            }
            this.resultSlots.setItem(0, resultItem);
            ci.cancel();
        }
        else if (resultItem.is(Items.ENCHANTED_BOOK)) {
            ci.cancel();
        }
    }
}