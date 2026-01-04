package net.legacy.enchants_and_expeditions.mixin.inventory;

import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    private List<NonNullList<ItemStack>> compartments;

    @Inject(method = "dropAll", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$keepBoundEquipmentOnDeath(CallbackInfo ci) {
        for(List<ItemStack> list : this.compartments) {
            for(int i = 0; i < list.size(); ++i) {
                ItemStack itemStack = list.get(i);
                if (!itemStack.isEmpty() && (!EnchantingHelper.hasEnchantment(itemStack, EaEEnchantments.BOUNDING_BLESSING) || itemStack.is(EaEItemTags.UNBOUNDABLE))) {
                    this.player.drop(itemStack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
        ci.cancel();
    }
}
