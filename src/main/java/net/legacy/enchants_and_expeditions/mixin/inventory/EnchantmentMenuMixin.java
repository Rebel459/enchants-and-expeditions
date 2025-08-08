package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.mixin.item.EnchantmentHelperMixin;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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
public abstract class EnchantmentMenuMixin {

    @Shadow
    @Final
    private RandomSource random;

    @Shadow @Final private Container enchantSlots;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final private DataSlot enchantmentSeed;

    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost);

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
    private boolean chiseledBookshelfPower(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, Operation<Boolean> original) {
        if (!original.call(level, enchantingTablePos, bookshelfPos)) return false;
        if (!(level.getBlockEntity(add(bookshelfPos, enchantingTablePos)) instanceof ChiseledBookShelfBlockEntity bookshelf)) return true;

        int bookCount = 0;
        for (int i = 0; i < bookshelf.getContainerSize(); ++i) {
            ItemStack itemStack = bookshelf.getItem(i);
            if (!itemStack.isEmpty()) ++bookCount;
        }
        return bookCount >= EaEConfig.get.enchanting.books_for_enchanting_power;
    }

    @ModifyReturnValue(method = "getEnchantmentList", at = @At("RETURN"))
    private List<EnchantmentInstance> addEnchantments(List<EnchantmentInstance> list, @Local(ordinal = 0, argsOnly = true) ItemStack stack, @Local(ordinal = 1, argsOnly = true) int level) {
        List<EnchantmentInstance> possibleEnchantments = this.possibleEnchantments.stream().filter(e -> !list.contains(e) && (e.enchantment().value().isSupportedItem(stack) || (stack.is(Items.BOOK) && EaEConfig.get.enchanting.allow_book_enchanting)) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet(), e.enchantment)).toList();
        if (possibleEnchantments.isEmpty()) {
            return list;
        }

        for (int i = 0; i < this.bookAmount; i++) {
            if (this.random.nextFloat() >= getProbability(i)) continue;
            Map<Holder<Enchantment>, EnchantmentInstance> maxLevelEnchantments = possibleEnchantments.stream()
                    .collect(Collectors.toMap(
                            e -> e.enchantment,
                            Function.identity(),
                            (entry1, entry2) -> entry1.level > entry2.level ? entry1 : entry2
                    ));

            List<EnchantmentInstance> entries = EnchantmentHelper.getAvailableEnchantmentResults(level / (int) Math.pow(2, list.size() - 1), stack, possibleEnchantments.stream().map(e -> e.enchantment));
            if (entries.isEmpty()) return list;
            entries = entries.stream().map(e -> {
                for (int j = maxLevelEnchantments.get(e.enchantment).level; j >= Math.min(e.enchantment.value().getMinLevel(), maxLevelEnchantments.get(e.enchantment).level); --j) {
                    if (level < 1 + 11 * (j - 1) || level > 21 + 11 * (j - 1))
                        continue;
                    return new EnchantmentInstance(e.enchantment, j);
                }
                return new EnchantmentInstance(e.enchantment, e.enchantment.value().getMinLevel());
            }).collect(Collectors.toCollection(ArrayList::new));
            if (stack.is(Items.BOOK)) {
                if (!list.isEmpty()) list.remove(this.random.nextInt(list.size()));
                if (entries.size() > 1) entries.remove(this.random.nextInt(entries.size()));
            }

            while (entries.size() > EaEConfig.get.enchanting.enchantment_limit)
                entries.remove(this.random.nextInt(entries.size()));

            list.addAll(entries);
            break;
        }
        return list;
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void addEnchantments(Container container, CallbackInfo ci) {
        if (EaEConfig.get.enchanting.allow_book_enchanting) return;
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (itemStack.is(Items.BOOK)) {
                for (int i = 0; i < 3; ++i) {
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
        return (float) (EaEConfig.get.enchanting.first_book_chance + EaEConfig.get.enchanting.subsequent_book_chance * index);
    }
}