package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @WrapOperation(method = "createResult", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V"))
    private void EaE$bookLimit(AnvilMenu instance, Operation<Void> original) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack inputStack = anvilMenu.inputSlots.getItem(0);
        ItemStack outputStack = anvilMenu.resultSlots.getItem(0);

        if (EaEConfig.get.general.enchantment_limit != -1) {
            int inputScore = EnchantingHelper.enchantmentScore(inputStack);
            int outputScore = EnchantingHelper.enchantmentScore(outputStack);

            if (outputScore > EaEConfig.get.general.enchantment_limit || EnchantingHelper.getBlessings(outputStack) > 1 || EnchantingHelper.getCurses(outputStack) > 1) {
                anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
                return;
            }
        }
        boolean shouldPass = false;
        List<Holder<Enchantment>> list = outputStack.getEnchantments().keySet().stream().toList();
        if (outputStack.getDamageValue() < inputStack.getDamageValue()) shouldPass = true;
        if (!shouldPass) {
            for (int x = 0; x < outputStack.getEnchantments().size(); x++) {
                if (outputStack.getEnchantments().getLevel(list.get(x)) > EnchantmentHelper.getItemEnchantmentLevel(list.get(x), inputStack))
                    shouldPass = true;
            }
        }
        if (!shouldPass) {
            anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }

        original.call(instance);
    }

    @Inject(method = "createResult",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;broadcastChanges()V",
                    shift = At.Shift.BEFORE))
    public void EaE$modifyPrice(CallbackInfo ci) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack inputStack = anvilMenu.slots.getFirst().getItem();
        ItemStack additionStack = anvilMenu.slots.get(1).getItem();
        ItemStack outputStack = anvilMenu.resultSlots.getItem(0);

        if ((inputStack.isEnchanted() && additionStack.isEnchanted() && inputStack.is(additionStack.getItem())) || additionStack.is(Items.ENCHANTED_BOOK)) {
            int enchantCost = 0;
            int multipler = 0;
            float reduction = 1;
            Set<Holder<Enchantment>> additionEnchantments = additionStack.getEnchantments().keySet();
            if (additionStack.is(Items.ENCHANTED_BOOK) && additionStack.has(DataComponents.STORED_ENCHANTMENTS)) {
                additionEnchantments = additionStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).keySet();
            }
            List<Holder<Enchantment>> additionEnchantmentsList = additionEnchantments.stream().toList();
            for (int x = 0; x < additionEnchantments.size(); x++) {
                Holder<Enchantment> enchantment = additionEnchantmentsList.get(x);
                int enchantmentLevel = additionStack.getEnchantments().getLevel(enchantment);
                if (additionStack.is(Items.ENCHANTED_BOOK) && additionStack.has(DataComponents.STORED_ENCHANTMENTS)) {
                    enchantmentLevel = additionStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment);
                }
                if (!inputStack.is(Items.ENCHANTED_BOOK) && EnchantingHelper.hasEnchantment(inputStack, enchantment.unwrapKey().get()) && inputStack.getEnchantments().getLevel(enchantment) >= enchantmentLevel) continue;
                if (inputStack.is(Items.ENCHANTED_BOOK) && EnchantingHelper.hasEnchantment(inputStack, enchantment.unwrapKey().get()) && inputStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment) >= enchantmentLevel) continue;
                enchantCost += enchantment.value().getAnvilCost();
            }
            List<Holder<Enchantment>> outputEnchantmentsList = outputStack.getEnchantments().keySet().stream().toList();
            if (outputStack.is(Items.ENCHANTED_BOOK) && outputStack.has(DataComponents.STORED_ENCHANTMENTS)) {
                outputEnchantmentsList = outputStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).keySet().stream().toList();
            }
            for (Holder<Enchantment> enchantment : outputEnchantmentsList) {
                if (enchantment.is(EaEEnchantmentTags.BLESSING)) multipler += 3;
                else if (EnchantingHelper.isEnchantment(enchantment)) multipler += 2;
                else if (enchantment.is(EnchantmentTags.CURSE)) multipler += 1;
            }
            if (enchantCost >= 1 && multipler == 0) multipler = 1;
            if (inputStack.has(DataComponents.ENCHANTABLE)) reduction -= Math.min(25, inputStack.get(DataComponents.ENCHANTABLE).value() * 0.01F);
            float finalCost = enchantCost * multipler * reduction;
            cost.set((int) finalCost);
        }
        else if (!inputStack.is(EaEItemTags.VARIABLE_REPAIR_COST)) cost.set(0);
        else if (inputStack.getComponents().has(DataComponents.ENCHANTABLE)) {
            int repairMultiplier = additionStack.getCount();
            double percentageDamaged = (double) inputStack.getDamageValue() / inputStack.getMaxDamage();
            if (percentageDamaged > 0.75 && repairMultiplier > 4) repairMultiplier = 4;
            else if (percentageDamaged > 0.5 && percentageDamaged <= 0.75 && repairMultiplier > 3) repairMultiplier = 3;
            else if (percentageDamaged > 0.25 && percentageDamaged <= 0.5 && repairMultiplier > 2) repairMultiplier = 2;
            else if (percentageDamaged <= 0.25 && repairMultiplier > 1) repairMultiplier = 1;

            int repairCost = Objects.requireNonNull(inputStack.get(DataComponents.ENCHANTABLE)).value();
            if (repairCost > 25) repairCost = 25;
            repairCost = (26 - repairCost) / 4;
            if (repairCost < 1) repairCost = 1;

            cost.set(repairCost * repairMultiplier);
        }
    }

    @Inject(method = "onTake", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (EaEConfig.get.general.anvil_break_chance == 0.12) return;

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
                if (additionItem.is(Items.ENCHANTED_BOOK) && !stack.is(Items.ENCHANTED_BOOK)) {
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
            if (additionItem.is(Items.ENCHANTED_BOOK) && !stack.is(Items.ENCHANTED_BOOK)) {
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
            if (!player.hasInfiniteMaterials() && blockState.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < EaEConfig.get.general.anvil_break_chance) {
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