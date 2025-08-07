package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {

    @Shadow
    @Final
    private RandomSource random;

    @Shadow @Final private Container enchantSlots;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;
    @Unique
    private final List<EnchantmentInstance> possibleEnchantments = new ArrayList<>();

    @Unique
    private int bookAmount = 0;

    @Unique
    public BlockPos add(int i, int j, int k, BlockPos blockPos) {
        return i == 0 && j == 0 && k == 0 ? blockPos : new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
    }

    @Unique
    public BlockPos add(BlockPos offset, BlockPos tablePos) {
        return new BlockPos(tablePos.getX() + offset.getX(), tablePos.getY() + offset.getY(), tablePos.getZ() + offset.getZ());
    }

    @Inject(method = "method_17411", at = @At(value = "HEAD"))
    private void getEnchantments(ItemStack itemStack, Level level, BlockPos tablePos, CallbackInfo ci) {
        this.possibleEnchantments.clear();
        this.bookAmount = 0;
        for (BlockPos blockPos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {

            if (!(level.getBlockEntity(add(blockPos, tablePos)) instanceof ChiseledBookShelfBlockEntity bookshelf)) {
                continue;
            }
            for (int i = 0; i < bookshelf.getContainerSize(); i++) {
                if (bookshelf.getItem(i).is(Items.ENCHANTED_BOOK)) {
                    ++this.bookAmount;
                    Set<EnchantmentInstance> possibleEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(bookshelf.getItem(i))
                            .entrySet()
                            .stream()
                            .map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue()))
                            .collect(Collectors.toSet());
                    possibleEnchantments.removeIf(entry -> entry.enchantment.is(EaEEnchantmentTags.NOT_OBTAINABLE_FROM_CHISELED_BOOKSHELF));
                    this.possibleEnchantments.addAll(possibleEnchantments);
                }
            }
        }
    }

    @WrapOperation(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/EnchantingTableBlock;isValidBookShelf(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean chiseledBookshelfProvidesPower(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, Operation<Boolean> original) {
        if (!original.call(level, enchantingTablePos, bookshelfPos)) return false;
        if (!(level.getBlockEntity(add(bookshelfPos, enchantingTablePos)) instanceof ChiseledBookShelfBlockEntity bookshelf)) return true;

        int bookCount = 0;
        for (int i = 0; i < bookshelf.getContainerSize(); ++i) {
            ItemStack itemStack = bookshelf.getItem(i);
            if (!itemStack.isEmpty()) ++bookCount;
        }
        return bookCount >= EaEConfig.get.chiseled_bookshelves.books_for_enchanting_power;
    }

    @ModifyReturnValue(method = "getEnchantmentList", at = @At("RETURN"))
    private List<EnchantmentInstance> addEnchantments(List<EnchantmentInstance> list, @Local(ordinal = 0, argsOnly = true) ItemStack stack, @Local(ordinal = 1, argsOnly = true) int level) {
        List<EnchantmentInstance> availableEnchantments = new ArrayList<>(list);

        List<EnchantmentInstance> bookshelfEnchantments = new ArrayList<>();
        for (int i = 0; i < this.bookAmount; i++) {
            if (this.random.nextFloat() >= getProbability(i)) continue;
            bookshelfEnchantments.addAll(this.possibleEnchantments.stream()
                    .filter(e -> !list.contains(e) &&
                            (e.enchantment().value().isSupportedItem(stack) ||
                                    (stack.is(Items.BOOK) && EaEConfig.get.enchanting.allow_book_enchanting)) &&
                            EnchantmentHelper.isEnchantmentCompatible(
                                    EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet(),
                                    e.enchantment))
                    .toList());
            break;
        }

        availableEnchantments.addAll(bookshelfEnchantments);
        if (availableEnchantments.isEmpty()) {
            return list;
        }

        Map<Holder<Enchantment>, EnchantmentInstance> maxLevelEnchantments = availableEnchantments.stream()
                .collect(Collectors.toMap(
                        e -> e.enchantment,
                        Function.identity(),
                        (entry1, entry2) -> entry1.level > entry2.level ? entry1 : entry2
                ));

        List<EnchantmentInstance> entries = EnchantmentHelper.getAvailableEnchantmentResults(level, stack, maxLevelEnchantments.keySet().stream());
        if (entries.isEmpty()) {
            return list;
        }

        List<EnchantmentInstance> compatibleEntries = new ArrayList<>();
        for (EnchantmentInstance candidate : entries) {
            if (compatibleEntries.stream().allMatch(existing -> EnchantmentHelper.isEnchantmentCompatible(Collections.singleton(existing.enchantment), candidate.enchantment))) {
                int maxLevel = maxLevelEnchantments.containsKey(candidate.enchantment)
                        ? maxLevelEnchantments.get(candidate.enchantment).level
                        : candidate.enchantment.value().getMinLevel();
                for (int j = maxLevel; j >= candidate.enchantment.value().getMinLevel(); --j) {
                    if (level >= 1 + 11 * (j - 1) && level <= 21 + 11 * (j - 1)) {
                        compatibleEntries.add(new EnchantmentInstance(candidate.enchantment, j));
                        break;
                    }
                }
                if (!compatibleEntries.contains(new EnchantmentInstance(candidate.enchantment, maxLevel))) {
                    compatibleEntries.add(new EnchantmentInstance(candidate.enchantment, candidate.enchantment.value().getMinLevel()));
                }
            }
        }

        list.clear();

        if (stack.is(Items.BOOK) && EaEConfig.get.enchanting.allow_book_enchanting) {
            if (!compatibleEntries.isEmpty()) {
                EnchantmentInstance selected = compatibleEntries.get(this.random.nextInt(compatibleEntries.size()));
                list.add(selected);
                compatibleEntries.removeIf(e -> !EnchantmentHelper.isEnchantmentCompatible(Collections.singleton(selected.enchantment), e.enchantment));
            }
        } else {
            int targetEnchantments;
            if (level >= 30 && compatibleEntries.size() >= EaEConfig.get.enchanting.enchantment_limit) {
                targetEnchantments = EaEConfig.get.enchanting.enchantment_limit;
            } else if (level >= 15 && compatibleEntries.size() >= EaEConfig.get.enchanting.enchantment_limit - 1 && EaEConfig.get.enchanting.enchantment_limit >= 3) {
                targetEnchantments = EaEConfig.get.enchanting.enchantment_limit - 1;
            } else {
                targetEnchantments = Math.min(compatibleEntries.size(), EaEConfig.get.enchanting.enchantment_limit);
            }
            Random javaRandom = new Random(this.random.nextLong());
            Collections.shuffle(compatibleEntries, javaRandom);
            list.addAll(compatibleEntries.subList(0, Math.min(compatibleEntries.size(), targetEnchantments)));
        }

        return list;
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void addEnchantments(Container container, CallbackInfo ci) {
        if (EaEConfig.get.enchanting.allow_book_enchanting) return;
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (itemStack.is(Items.BOOK)) {
                for(int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
                ci.cancel();
            }
        }
    }

    @Unique
    private float getProbability(int index) {
        if (index > 6) index = 6;
        index = index - 1;
        return EaEConfig.get.chiseled_bookshelves.first_book_chance + EaEConfig.get.chiseled_bookshelves.tenth_book_chance * index;
    }
}