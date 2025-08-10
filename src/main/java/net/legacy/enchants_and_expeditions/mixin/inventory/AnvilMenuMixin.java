package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @WrapOperation(method = "createResult", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean EaE$createResult(Enchantment instance, ItemStack stack, Operation<Boolean> original) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack itemStack = anvilMenu.slots.getFirst().getItem();
        ItemStack additionItem = anvilMenu.slots.get(1).getItem();
        if (additionItem.is(Items.ENCHANTED_BOOK) && !itemStack.is(Items.ENCHANTED_BOOK) && EaEConfig.get.enchanting.anvil_book_enchanting && !stack.isEnchanted()) return original.call(instance, stack);
        return false;
    }

    @Inject(method = "createResult",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V",
                    shift = At.Shift.BEFORE))
    public void EaE$modifyPrice(CallbackInfo ci) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack itemStack = anvilMenu.slots.getFirst().getItem();
        ItemStack additionItem = anvilMenu.slots.get(1).getItem();

        if ((additionItem.is(Items.ENCHANTED_BOOK)) && EaEConfig.get.enchanting.anvil_book_enchanting) {
            int bookCost = 0;
            int multiplier = 6;
            if (additionItem.getComponents().has(DataComponents.ENCHANTABLE) && additionItem.getComponents().get(DataComponents.ENCHANTABLE).value() == 15) multiplier = multiplier / 2;
            if (itemStack.getComponents().has(DataComponents.ENCHANTABLE))
                bookCost = itemStack.get(DataComponents.ENCHANTABLE).value();
            bookCost = (26 - bookCost) + additionItem.getEnchantments().size() * multiplier;
            if (bookCost < 1) bookCost = 1;
            cost.set(bookCost);
        }
        else if (!itemStack.is(EaEItemTags.VARIABLE_REPAIR_COST)) cost.set(0);
        else if (itemStack.getComponents().has(DataComponents.ENCHANTABLE)) {
            int repairMultiplier = additionItem.getCount();
            double percentageDamaged = (double) itemStack.getDamageValue() / itemStack.getMaxDamage();
            if (percentageDamaged > 0.75 && repairMultiplier > 4) repairMultiplier = 4;
            else if (percentageDamaged > 0.5 && percentageDamaged <= 0.75 && repairMultiplier > 3) repairMultiplier = 3;
            else if (percentageDamaged > 0.25 && percentageDamaged <= 0.5 && repairMultiplier > 2) repairMultiplier = 2;
            else if (percentageDamaged <= 0.25 && repairMultiplier > 1) repairMultiplier = 1;

            int repairCost = Objects.requireNonNull(itemStack.get(DataComponents.ENCHANTABLE)).value();
            if (repairCost > 25) repairCost = 25;
            repairCost = (26 - repairCost) / 4;
            if (repairCost < 1) repairCost = 1;

            cost.set(repairCost * repairMultiplier);
        }
    }

    @Inject(method = "onTake", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (!EaEConfig.get.enchanting.anvil_book_enchanting && EaEConfig.get.enchanting.anvil_break_chance == 0.12) return;

        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack additionItem = anvilMenu.slots.get(1).getItem();

        if (!player.hasInfiniteMaterials()) {
            player.giveExperienceLevels(-this.cost.get());
        }

        if (anvilMenu.repairItemCountCost > 0) {
            ItemStack itemStack = anvilMenu.inputSlots.getItem(1);
            if (!itemStack.isEmpty() && itemStack.getCount() > anvilMenu.repairItemCountCost) {
                itemStack.shrink(anvilMenu.repairItemCountCost);
                anvilMenu.inputSlots.setItem(1, itemStack);
            } else {
                if (additionItem.is(Items.ENCHANTED_BOOK)) {
                    additionItem.setDamageValue(additionItem.getDamageValue() + 1);
                    if (additionItem.getDamageValue() < additionItem.getMaxDamage())
                        anvilMenu.inputSlots.setItem(1, additionItem);
                    else {
                        anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
                        anvilMenu.player.playSound(SoundEvents.ITEM_BREAK.value());
                    }
                }
                else
                    anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else if (!anvilMenu.onlyRenaming) {
            if (additionItem.is(Items.ENCHANTED_BOOK)) {
                additionItem.setDamageValue(additionItem.getDamageValue() + 1);
                if (additionItem.getDamageValue() < additionItem.getMaxDamage())
                    anvilMenu.inputSlots.setItem(1, additionItem);
                else {
                    anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
                    anvilMenu.player.playSound(SoundEvents.ITEM_BREAK.value());
                }
            }
            else
                anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        anvilMenu.inputSlots.setItem(0, ItemStack.EMPTY);
        anvilMenu.access.execute((level, blockPos) -> {
            BlockState blockState = level.getBlockState(blockPos);
            if (!player.hasInfiniteMaterials() && blockState.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < EaEConfig.get.enchanting.anvil_break_chance) {
                BlockState blockState2 = AnvilBlock.damage(blockState);
                if (blockState2 == null) {
                    level.removeBlock(blockPos, false);
                    level.levelEvent(1029, blockPos, 0);
                } else {
                    level.setBlock(blockPos, blockState2, 2);
                    level.levelEvent(1030, blockPos, 0);
                }
            } else {
                level.levelEvent(1030, blockPos, 0);
            }

        });
        ci.cancel();
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$mayPickup(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "calculateIncreasedRepairCost", at = @At(value = "HEAD"), cancellable = true)
    private static void EaE$increaseLimit(int oldRepairCost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }
}