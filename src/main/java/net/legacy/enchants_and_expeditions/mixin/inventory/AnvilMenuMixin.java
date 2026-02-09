package net.legacy.enchants_and_expeditions.mixin.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    @Nullable
    private String itemName;

    @Unique
    private boolean onlyRenaming;

    @Inject(method = "createResult", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$createResult(CallbackInfo ci) {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack itemStack = anvilMenu.inputSlots.getItem(0);
        ItemStack itemStack2 = itemStack.copy();
        ItemStack itemStack3 = anvilMenu.inputSlots.getItem(1);
        this.onlyRenaming = this.cost.get() == 0 && itemStack3 == ItemStack.EMPTY; // custom onlyRenaming
        this.cost.set(1);
        int i = 0;
        long l = 0L;
        int j = 0;
        if (!itemStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(itemStack)) {
            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(itemStack2));
            l += (long)itemStack.getOrDefault(DataComponents.REPAIR_COST, 0) + (long)itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);
            anvilMenu.repairItemCountCost = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.has(DataComponents.STORED_ENCHANTMENTS);
                if (itemStack2.isDamageableItem() && EnchantingHelper.isValidRepairItem(itemStack2, itemStack3)) {
                    int k = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    if (k <= 0) {
                        anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int m;
                    for(m = 0; k > 0 && m < itemStack3.getCount(); ++m) {
                        int n = itemStack2.getDamageValue() - k;
                        itemStack2.setDamageValue(n);
                        ++i;
                        k = Math.min(itemStack2.getDamageValue(), itemStack2.getMaxDamage() / 4);
                    }

                    anvilMenu.repairItemCountCost = m;
                } else {
                    if (!bl && (!itemStack2.is(itemStack3.getItem()) || !itemStack2.isDamageableItem())) {
                        anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (itemStack2.isDamageableItem() && !bl) {
                        int k = itemStack.getMaxDamage() - itemStack.getDamageValue();
                        int m = itemStack3.getMaxDamage() - itemStack3.getDamageValue();
                        int n = m + itemStack2.getMaxDamage() * 12 / 100;
                        int o = k + n;
                        int p = itemStack2.getMaxDamage() - o;
                        if (p < 0) {
                            p = 0;
                        }

                        if (p < itemStack2.getDamageValue()) {
                            itemStack2.setDamageValue(p);
                            i += 2;
                        }
                    }

                    ItemEnchantments itemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;

                    for(Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                        Holder<Enchantment> holder = entry.getKey();
                        int q = mutable.getLevel(holder);
                        int r = entry.getIntValue();
                        r = q == r ? r + 1 : Math.max(r, q);
                        Enchantment enchantment = holder.value();
                        boolean bl4 = enchantment.canEnchant(itemStack);
                        if (anvilMenu.player.hasInfiniteMaterials() || itemStack.is(Items.ENCHANTED_BOOK)) {
                            bl4 = true;
                        }

                        for(Holder<Enchantment> holder2 : mutable.keySet()) {
                            if (!holder2.equals(holder) && !Enchantment.areCompatible(holder, holder2)) {
                                bl4 = false;
                                ++i;
                            }
                        }

                        if (!bl4) {
                            bl3 = true;
                        } else {
                            bl2 = true;
                            if (r > enchantment.getMaxLevel()) {
                                r = enchantment.getMaxLevel();
                            }

                            mutable.set(holder, r);
                            int s = enchantment.getAnvilCost();
                            if (bl) {
                                s = Math.max(1, s / 2);
                            }

                            i += s * r;
                            if (itemStack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }

                    if (bl3 && !bl2) {
                        anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                }
            }

            if (this.itemName != null && !StringUtil.isBlank(this.itemName)) {
                if (!this.itemName.equals(itemStack.getHoverName().getString())) {
                    j = 1;
                    i += j;
                    itemStack2.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
                }
            } else if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                j = 1;
                i += j;
                itemStack2.remove(DataComponents.CUSTOM_NAME);
            }

            int t = i <= 0 ? 0 : (int) Mth.clamp(l + (long)i, 0L, 2147483647L);
            this.cost.set(t);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int k = itemStack2.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (k < itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                    k = itemStack3.getOrDefault(DataComponents.REPAIR_COST, 0);
                }

                if (j != i || j == 0) {
                    k = AnvilMenu.calculateIncreasedRepairCost(k);
                }

                itemStack2.set(DataComponents.REPAIR_COST, k);
                ItemStack itemStack4 = itemStack2.copy();
                if (!anvilMenu.player.hasInfiniteMaterials()) mutable.removeIf(holder -> (itemStack4.is(ItemTags.AXES) && holder.is(EaEEnchantmentTags.NOT_ON_AXES)) || (itemStack4.is(EaEItemTags.ANIMAL_ARMOR) && holder.is(EaEEnchantmentTags.NOT_ON_ANIMAL_ARMOR)));
                EnchantmentHelper.setEnchantments(itemStack2, mutable.toImmutable());
            }

            if (itemStack2 != itemStack) anvilMenu.resultSlots.setItem(0, itemStack2);
            else {
                anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
            }

            EaE$bookLimit();
            EaE$modifyPrice();
            EaE$repairBooksOnCombine();

            anvilMenu.broadcastChanges();
        } else {
            anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        }
        ci.cancel();
    }

    @Unique
    private void EaE$bookLimit() {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack inputStack = anvilMenu.inputSlots.getItem(0);
        ItemStack additionStack = anvilMenu.inputSlots.getItem(1);
        ItemStack outputStack = anvilMenu.resultSlots.getItem(0);

        if (EaEConfig.get.general.enchantment_limit != -1 && !this.onlyRenaming) {
            int inputScore = EnchantingHelper.enchantmentScore(inputStack);
            int outputScore = EnchantingHelper.enchantmentScore(outputStack);

            if (outputScore > EaEConfig.get.general.enchantment_limit || EnchantingHelper.getBlessings(outputStack) > 1 || EnchantingHelper.getCurses(outputStack) > 1) {
                anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
                return;
            }
        }
        boolean shouldPass = this.onlyRenaming;
        List<Holder<Enchantment>> list = outputStack.getEnchantments().keySet().stream().toList();
        if (outputStack.getDamageValue() < inputStack.getDamageValue()) shouldPass = true;
        if (!shouldPass) {
            if (outputStack.is(Items.ENCHANTED_BOOK) && outputStack.has(DataComponents.STORED_ENCHANTMENTS)) {
                list = outputStack.get(DataComponents.STORED_ENCHANTMENTS).keySet().stream().toList();
                for (int x = 0; x < outputStack.get(DataComponents.STORED_ENCHANTMENTS).size(); x++) {
                    if (outputStack.get(DataComponents.STORED_ENCHANTMENTS).getLevel(list.get(x)) > EnchantingHelper.getStoredEnchantmentLevel(list.get(x), inputStack)) {
                        shouldPass = true;
                        break;
                    }
                }
            }
            else {
                for (int x = 0; x < outputStack.getEnchantments().size(); x++) {
                    if (outputStack.getEnchantments().getLevel(list.get(x)) > EnchantmentHelper.getItemEnchantmentLevel(list.get(x), inputStack)) {
                        shouldPass = true;
                        break;
                    }
                }
            }
        }
        if (!shouldPass) {
            anvilMenu.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
            return;
        }
    }

    @Unique
    public void EaE$modifyPrice() {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack inputStack = anvilMenu.slots.getFirst().getItem();
        ItemStack additionStack = anvilMenu.slots.get(1).getItem();
        ItemStack outputStack = anvilMenu.resultSlots.getItem(0);

        if ((inputStack.isEnchanted() && additionStack.isEnchanted() && inputStack.getItem() == additionStack.getItem()) || additionStack.is(Items.ENCHANTED_BOOK)) {
            int enchantCost;
            int multipler = 1;
            float reduction = 1;

            if (inputStack.getItem() == additionStack.getItem()) enchantCost = Math.max(evaluateEnchantCost(inputStack, additionStack, outputStack), evaluateEnchantCost(additionStack, inputStack,outputStack));
            else enchantCost = evaluateEnchantCost(inputStack, additionStack, outputStack);

            List<Holder<Enchantment>> outputEnchantmentsList = outputStack.getEnchantments().keySet().stream().toList();
            if (outputStack.is(Items.ENCHANTED_BOOK) && outputStack.has(DataComponents.STORED_ENCHANTMENTS)) {
                outputEnchantmentsList = outputStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).keySet().stream().toList();
            }
            for (Holder<Enchantment> enchantment : outputEnchantmentsList) {
                if (enchantment.is(EaEEnchantmentTags.BLESSING)) multipler += 2;
                else if (EnchantingHelper.isEnchantment(enchantment)) multipler += 1;
                else if (enchantment.is(EnchantmentTags.CURSE)) multipler += 0;
            }
            reduction -= Math.min(25, outputStack.getItem().getEnchantmentValue() * 0.01F);
            float finalCost = enchantCost * multipler * reduction;
            cost.set((int) finalCost);
        }
        else if (!inputStack.is(EaEItemTags.VARIABLE_REPAIR_COST)) cost.set(0);
        else if (inputStack.getItem().getEnchantmentValue() > 0) {
            int repairMultiplier = additionStack.getCount();
            double percentageDamaged = (double) inputStack.getDamageValue() / inputStack.getMaxDamage();
            if (percentageDamaged > 0.75 && repairMultiplier > 4) repairMultiplier = 4;
            else if (percentageDamaged > 0.5 && percentageDamaged <= 0.75 && repairMultiplier > 3) repairMultiplier = 3;
            else if (percentageDamaged > 0.25 && percentageDamaged <= 0.5 && repairMultiplier > 2) repairMultiplier = 2;
            else if (percentageDamaged <= 0.25 && repairMultiplier > 1) repairMultiplier = 1;

            int repairCost = inputStack.getItem().getEnchantmentValue();
            if (repairCost > 25) repairCost = 25;
            repairCost = (26 - repairCost) / 4;
            if (repairCost < 1) repairCost = 1;

            cost.set(repairCost * repairMultiplier);
        }
        if (cost.get() > 99) cost.set(99);
    }

    @Unique
    protected void EaE$repairBooksOnCombine() {
        AnvilMenu anvilMenu = AnvilMenu.class.cast(this);
        ItemStack inputStack = anvilMenu.inputSlots.getItem(0);
        ItemStack additionStack = anvilMenu.inputSlots.getItem(1);
        ItemStack outputStack = anvilMenu.resultSlots.getItem(0);
        if (inputStack.is(Items.ENCHANTED_BOOK) && additionStack.is(Items.ENCHANTED_BOOK) && outputStack.is(Items.ENCHANTED_BOOK)) {
            outputStack.setDamageValue((int) Math.min(0, (inputStack.getDamageValue() + additionStack.getDamageValue()) - outputStack.getMaxDamage() * 1.1F));
        }
    }

    @Inject(method = "onTake", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$onTake(Player player, ItemStack stack, CallbackInfo ci) {
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
                        anvilMenu.player.playSound(SoundEvents.ITEM_BREAK);
                    }
                }
                else {
                    anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
                }
            }
        } else if (!this.onlyRenaming) {
            if (additionItem.is(Items.ENCHANTED_BOOK) && !stack.is(Items.ENCHANTED_BOOK)) {
                additionItem.setDamageValue(additionItem.getDamageValue() + 1);
                if (additionItem.getDamageValue() < additionItem.getMaxDamage())
                    anvilMenu.inputSlots.setItem(1, additionItem);
                else {
                    anvilMenu.inputSlots.setItem(1, ItemStack.EMPTY);
                    anvilMenu.player.playSound(SoundEvents.ITEM_BREAK);
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

        if (anvilMenu.inputSlots.getItem(1).is(Items.ENCHANTED_BOOK)) {
            ItemStack book = anvilMenu.inputSlots.getItem(1).copy();
            ItemEnchantments itemEnchantments = EnchantmentHelper.updateEnchantments(book, mutable -> mutable.removeIf(holder -> holder.is(EnchantmentTags.CURSE) || holder.is(EaEEnchantmentTags.BLESSING)));
            if (book.is(Items.ENCHANTED_BOOK) && itemEnchantments.isEmpty()) {
                book = book.transmuteCopy(Items.BOOK);
            }
            anvilMenu.inputSlots.setItem(1, book);
        }
        ci.cancel();
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$mayPickup(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(player.hasInfiniteMaterials() || player.experienceLevel >= this.cost.get() || this.cost.get() == 0);
    }

    @Inject(method = "calculateIncreasedRepairCost", at = @At(value = "HEAD"), cancellable = true)
    private static void EaE$increaseLimit(int oldRepairCost, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Unique
    public int evaluateEnchantCost(ItemStack stack, ItemStack stack2, ItemStack outputStack) {
        int value = 0;
        Set<Holder<Enchantment>> additionEnchantments = stack2.getEnchantments().keySet();
        if (stack2.is(Items.ENCHANTED_BOOK) && stack2.has(DataComponents.STORED_ENCHANTMENTS)) {
            additionEnchantments = stack2.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).keySet();
        }
        List<Holder<Enchantment>> additionEnchantmentsList = additionEnchantments.stream().toList();
        for (int x = 0; x < additionEnchantments.size(); x++) {
            Holder<Enchantment> enchantment = additionEnchantmentsList.get(x);
            int enchantmentLevel = stack2.getEnchantments().getLevel(enchantment);
            if (stack2.is(Items.ENCHANTED_BOOK) && stack2.has(DataComponents.STORED_ENCHANTMENTS)) {
                enchantmentLevel = stack2.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment);
            }
            ItemEnchantments itemEnchantments = stack.getEnchantments();
            if (stack.is(Items.ENCHANTED_BOOK) && stack.has(DataComponents.STORED_ENCHANTMENTS)) itemEnchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
            ItemEnchantments itemEnchantments2 = stack2.getEnchantments();
            if (stack2.is(Items.ENCHANTED_BOOK) && stack2.has(DataComponents.STORED_ENCHANTMENTS)) itemEnchantments2 = stack2.get(DataComponents.STORED_ENCHANTMENTS);
            ItemEnchantments outputItemEnchantments = outputStack.getEnchantments();
            if (outputStack.is(Items.ENCHANTED_BOOK) && outputStack.has(DataComponents.STORED_ENCHANTMENTS)) outputItemEnchantments = outputStack.get(DataComponents.STORED_ENCHANTMENTS);
            if (!stack.is(Items.ENCHANTED_BOOK) && ((EnchantingHelper.hasEnchantment(stack, enchantment.unwrapKey().get()) && stack.getEnchantments().getLevel(enchantment) >= enchantmentLevel) || !EnchantingHelper.hasEnchantment(stack, enchantment.unwrapKey().get()) || (outputItemEnchantments.getLevel(enchantment) >= itemEnchantments.getLevel(enchantment) && outputItemEnchantments.getLevel(enchantment) >= itemEnchantments2.getLevel(enchantment)))) value += enchantment.value().getAnvilCost();
            if (stack.is(Items.ENCHANTED_BOOK) && stack2.is(Items.ENCHANTED_BOOK) && (EnchantingHelper.getStoredEnchantmentLevel(enchantment, stack) >= enchantmentLevel || !EnchantingHelper.hasEnchantment(stack, enchantment.unwrapKey().get())) && (!EnchantingHelper.hasEnchantment(stack, enchantment.unwrapKey().get()) || !EnchantingHelper.hasEnchantment(stack, enchantment.unwrapKey().get()) || (outputItemEnchantments.getLevel(enchantment) >= itemEnchantments.getLevel(enchantment) && outputItemEnchantments.getLevel(enchantment) >= itemEnchantments2.getLevel(enchantment)))) {
                value += enchantment.value().getAnvilCost();
            }
        }
        return value;
    }
}