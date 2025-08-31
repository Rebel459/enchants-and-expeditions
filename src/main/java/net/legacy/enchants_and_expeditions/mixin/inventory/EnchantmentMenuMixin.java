package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.google.common.collect.Lists;
import net.legacy.enchants_and_expeditions.tag.EaEBlockTags;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantingTableBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    @Shadow @Final private RandomSource random;

    @Shadow @Final private DataSlot enchantmentSeed;

    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;

    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost);

    @Shadow public abstract void slotsChanged(Container container);

    @Unique
    private int mana = 0;
    @Unique
    private boolean maxMana = false;

    @Unique
    private int frost = 0;

    @Unique
    private int scorch = 0;

    @Unique
    public BlockPos add(int i, int j, int k, BlockPos blockPos) {
        return i == 0 && j == 0 && k == 0 ? blockPos : new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
    }

    @Unique
    public BlockPos add(BlockPos offset, BlockPos tablePos) {
        return new BlockPos(tablePos.getX() + offset.getX(), tablePos.getY() + offset.getY(), tablePos.getZ() + offset.getZ());
    }

    @Unique
    private static boolean providesMana(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos) {
        return level.getBlockState(enchantingTablePos.offset(bookshelfPos)).is(EaEBlockTags.MANA_PROVIDER)
                && level.getBlockState(enchantingTablePos.offset(bookshelfPos.getX() / 2, bookshelfPos.getY(), bookshelfPos.getZ() / 2))
                .is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }
    @Unique
    private static boolean providesFrost(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos) {
        return level.getBlockState(enchantingTablePos.offset(bookshelfPos)).is(EaEBlockTags.FROST_PROVIDER)
                && level.getBlockState(enchantingTablePos.offset(bookshelfPos.getX() / 2, bookshelfPos.getY(), bookshelfPos.getZ() / 2))
                .is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }
    @Unique
    private static boolean providesScorch(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos) {
        return level.getBlockState(enchantingTablePos.offset(bookshelfPos)).is(EaEBlockTags.SCORCH_PROVIDER)
                && level.getBlockState(enchantingTablePos.offset(bookshelfPos.getX() / 2, bookshelfPos.getY(), bookshelfPos.getZ() / 2))
                .is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }

    @Inject(method = "slotsChanged", at = @At(value = "HEAD"))
    private void EaE$slotsChanged(Container container, CallbackInfo ci) {
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
                this.access.execute((level, blockPos) -> {
                    this.mana = 0;
                    this.maxMana = false;
                    this.frost = 0;
                    this.scorch = 0;
                    for (BlockPos blockPos2 : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                        if (providesMana(level, blockPos, blockPos2)) {
                            this.mana++;
                        }
                        if (providesScorch(level, blockPos, blockPos2)) {
                            this.scorch++;
                        }
                        if (providesFrost(level, blockPos, blockPos2)) {
                            this.frost++;
                        }
                    }
                    if (this.mana >= 20) {
                        this.mana = 20;
                        this.maxMana = true;
                    }
                });
            }
        }
    }

    @Inject(method = "getEnchantmentList", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        this.random.setSeed((long)(this.enchantmentSeed.get() + slot));
        if (!stack.getComponents().has(DataComponents.ENCHANTABLE)) {
            cir.setReturnValue(List.of());
        } else {
            int value = stack.getComponents().get(DataComponents.ENCHANTABLE).value();
            int multiplier = 1 + value / 10;

            List<EnchantmentInstance> list = EaE$selectEnchantment(this.random, stack, this.mana * multiplier, this.frost * multiplier, this.scorch * multiplier, registryAccess);

            if (stack.is(Items.BOOK) && list.size() > 1) {
                list.remove(this.random.nextInt(list.size()));
            }

            cir.setReturnValue(list);
        }
    }

    @Unique
    private List<EnchantmentInstance> EaE$selectEnchantment(RandomSource random, ItemStack stack, int mana, int frost, int scorch, RegistryAccess registryAccess) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        Enchantable enchantable = stack.get(DataComponents.ENCHANTABLE);
        if (enchantable == null) {
            return list;
        }

        // Enchantment tags
        Stream<Holder<Enchantment>> manaEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> frostEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> scorchEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> flowEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> elementusEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> mightEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED).map(HolderSet.Named::stream).orElse(Stream.empty());
        Stream<Holder<Enchantment>> archaiaEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT).map(HolderSet.Named::stream).orElse(Stream.empty());

        // Sub-stats
        int flow = (mana + frost) / 2;
        int elementus = (frost + scorch) / 2;
        int might = (scorch + mana) / 2;
        int archaia = (mana + frost + scorch) / 3;

        // Variability TODO: WIP
        mana = mana * (1 + random.nextInt(enchantable.value()) / 10);
        frost = frost * (1 + random.nextInt(enchantable.value()) / 10);
        scorch = scorch * (1 + random.nextInt(enchantable.value()) / 10);
        flow = flow * (1 + random.nextInt(enchantable.value()) / 10);
        elementus = elementus * (1 + random.nextInt(enchantable.value()) / 10);
        might = might * (1 + random.nextInt(enchantable.value()) / 10);
        archaia = archaia * (1 + random.nextInt(enchantable.value()) / 10);

        // Add enchants to list
        List<EnchantmentInstance> list2 = Lists.newArrayList();
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(mana, stack, manaEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(frost, stack, frostEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(scorch, stack, scorchEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(flow, stack, flowEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(elementus, stack, elementusEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(might, stack, mightEnchantments));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(archaia, stack, archaiaEnchantments));

        // Check if new list is empty
        if (list2.isEmpty()) {
            return list;
        }

        // Add a random enchantment
        WeightedRandom.getRandomItem(random, list2, EnchantmentInstance::weight).ifPresent(list::add);

        // Add enchantments
        while (random.nextInt(50) <= mana || random.nextInt(50) <= frost || random.nextInt(50) <= scorch ||
                random.nextInt(50) <= flow || random.nextInt(50) <= elementus || random.nextInt(50) <= might ||
                random.nextInt(50) <= archaia) {
            if (!list.isEmpty()) {
                EnchantmentHelper.filterCompatibleEnchantments(list2, Util.lastOf(list));
            }

            if (list2.isEmpty()) {
                break;
            }

            WeightedRandom.getRandomItem(random, list2, EnchantmentInstance::weight).ifPresent(list::add);
            mana /= 2;
            frost /= 2;
            scorch /= 2;
            flow /= 2;
            elementus /= 2;
            might /= 2;
            archaia /= 2;
        }

        // Add blessings TODO: WIP
        if (this.maxMana) {
            list.add(new EnchantmentInstance(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MENDING), 1));
        }

        // Limit the list size to 3 TODO: Configurable limit
        while (list.size() > 3) {
            list.remove(random.nextInt(list.size()));
        }

        return list;
    }
}