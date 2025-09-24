package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.legacy.enchants_and_expeditions.block.AltarBlock;
import net.legacy.enchants_and_expeditions.block.AltarBlockType;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.network.EnchantingAttributes;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin implements EnchantingAttributes {

    @Shadow @Final private RandomSource random;
    @Shadow @Final private DataSlot enchantmentSeed;
    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;
    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost);

    @Unique private Player player;
    @Unique private int totalBookshelves = 0;
    @Unique private int bookshelves = 0;
    @Unique private int arcaneBooksheves = 0;
    @Unique private int glacialBooksheves = 0;
    @Unique private int infernalBooksheves = 0;

    @Unique private int totalAltars = 0;
    @Unique private int manaAltars = 0;
    @Unique private int frostAltars = 0;
    @Unique private int scorchAltars = 0;
    @Unique private int flowAltars = 0;
    @Unique private int chaosAltars = 0;
    @Unique private int greedAltars = 0;
    @Unique private int mightAltars = 0;
    @Unique private int stabilityAltars = 0;
    @Unique private int powerAltars = 0;

    @Unique private int mana = 0;
    @Unique private int frost = 0;
    @Unique private int scorch = 0;
    @Unique private int flow = 0;
    @Unique private int chaos = 0;
    @Unique private int greed = 0;
    @Unique private int might = 0;
    @Unique private int corruption = 0;
    @Unique private int divinity = 0;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void onInit(int syncId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @Unique
    private static String EaE$blockId(BlockState state) {
        try {
            return state.getBlock().builtInRegistryHolder().key().location().toString();
        } catch (Throwable t) {
            return state.getBlock().getClass().getSimpleName();
        }
    }

    @Unique
    private static boolean enchantingBlockCheck(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, Block block) {
        BlockPos targetPos = enchantingTablePos.offset(bookshelfPos);
        BlockPos gapPos = enchantingTablePos.offset(bookshelfPos.getX() / 2, bookshelfPos.getY(), bookshelfPos.getZ() / 2);
        BlockState targetState = level.getBlockState(targetPos);
        BlockState gapState = level.getBlockState(gapPos);
        boolean isMatch = targetState.is(block);
        boolean isTransmitter = gapState.is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
        if (EnchantsAndExpeditions.debug) LogUtils.getLogger().info(
                "[EaE] check targetPos={} targetBlock={} expectMatch={} actualMatch={} gapPos={} gapBlock={} transmitterMatch={}",
                targetPos, EaE$blockId(targetState), block.getDescriptionId(), isMatch, gapPos, EaE$blockId(gapState), isTransmitter
        );
        return isMatch && isTransmitter;
    }

    @Unique
    private static BlockPos getAltarPos(BlockPos enchantingTablePos, BlockPos altarPos) {
        return enchantingTablePos.offset(altarPos);
    }

    @Inject(method = "slotsChanged", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$slotsChanged(Container container, CallbackInfo ci) {
        EnchantmentMenu enchantmentMenu = EnchantmentMenu.class.cast(this);
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (!itemStack.isEmpty() && (itemStack.isEnchantable())) {
                this.access.execute((level, blockPos) -> {
                    IdMap<Holder<net.minecraft.world.item.enchantment.Enchantment>> idMap =
                            level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
                    int ix = 0;

                    this.totalBookshelves = 0;
                    this.bookshelves = 0;
                    this.arcaneBooksheves = 0;
                    this.glacialBooksheves = 0;
                    this.infernalBooksheves = 0;

                    this.totalAltars = 0;
                    this.manaAltars = 0;
                    this.frostAltars = 0;
                    this.scorchAltars = 0;
                    this.flowAltars = 0;
                    this.chaosAltars = 0;
                    this.greedAltars = 0;
                    this.mightAltars = 0;
                    this.stabilityAltars = 0;
                    this.powerAltars = 0;

                    for (BlockPos off : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                        if (EnchantingTableBlock.isValidBookShelf(level, blockPos, off)) {
                            ix++;
                        }
                        if (this.totalBookshelves < 15) {
                            if (enchantingBlockCheck(level, blockPos, off, Blocks.BOOKSHELF)) {
                                this.bookshelves++;
                                this.totalBookshelves++;

                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.ARCANE_BOOKSHELF)) {
                                this.arcaneBooksheves++;
                                this.totalBookshelves++;

                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.GLACIAL_BOOKSHELF)) {
                                this.glacialBooksheves++;
                                this.totalBookshelves++;

                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.INFERNAL_BOOKSHELF)) {
                                this.infernalBooksheves++;
                                this.totalBookshelves++;
                            }
                        }
                        if (this.totalAltars < 3 && enchantingBlockCheck(level, blockPos, off, EaEBlocks.ALTAR) && level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) != AltarBlockType.EMPTY) {
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.MANA_TOME) {
                                this.manaAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.FROST_TOME) {
                                this.frostAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.SCORCH_TOME) {
                                this.scorchAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.FLOW_TOME) {
                                this.flowAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.CHAOS_TOME) {
                                this.chaosAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.GREED_TOME) {
                                this.greedAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.MIGHT_TOME) {
                                this.mightAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.STABILITY_TOME) {
                                this.stabilityAltars++;
                                this.totalAltars++;
                            }
                            if (level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) == AltarBlockType.POWER_TOME) {
                                this.powerAltars++;
                                this.totalAltars++;
                            }
                        }
                    }

                    this.random.setSeed((long)this.enchantmentSeed.get());

                    for (int j = 0; j < 3; j++) {
                        this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, ix, itemStack);
                        if (this.costs[j] >= 1) {
                            this.costs[j] += itemStack.getEnchantments().size() * 3 + EnchantingHelper.getBlessings(itemStack) * 3 - EnchantingHelper.getCurses(itemStack) * 3 + this.totalAltars * 3 - this.stabilityAltars * 6 - this.powerAltars * 6;
                        }
                        if (EnchantmentHelper.getEnchantmentCost(this.random, j, ix, itemStack) >= 1) {
                            if (this.costs[0] < 1) {
                                this.costs[0] = 1;
                            } else if (this.costs[1] < 2) {
                                this.costs[1] = 2;
                            } else if (this.costs[2] < 3) {
                                this.costs[2] = 3;
                            }
                        }
                        this.enchantClue[j] = -1;
                        this.levelClue[j] = -1;
                        if (this.costs[j] < j + 1) {
                            this.costs[j] = 0;
                        }
                    }

                    for (int jx = 0; jx < 3; jx++) {
                        if (this.costs[jx] > 0) {
                            List<EnchantmentInstance> list = this.getEnchantmentList(level.registryAccess(), itemStack, jx, this.costs[jx] + this.powerAltars * 3 + this.stabilityAltars * 3);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentInstance inst = list.get(this.random.nextInt(list.size()));
                                this.enchantClue[jx] = idMap.getId(inst.enchantment());
                                this.levelClue[jx] = inst.level();
                            }
                        }
                    }

                    enchantmentMenu.broadcastChanges();
                });
            } else {
                for (int i = 0; i < 3; i++) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "getEnchantmentList", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int enchantingPower, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        this.random.setSeed(this.enchantmentSeed.get() + slot);
        if (!stack.getComponents().has(DataComponents.ENCHANTABLE)) {
            cir.setReturnValue(List.of());
        } else {
            List<EnchantmentInstance> list = EaE$selectEnchantment(this.random, stack, slot, enchantingPower, registryAccess);
            cir.setReturnValue(list);
        }
    }

    @Unique
    private List<EnchantmentInstance> EaE$selectEnchantment(RandomSource random, ItemStack stack, int slot, int enchantingPower, RegistryAccess registryAccess) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        Enchantable enchantable = stack.get(DataComponents.ENCHANTABLE);
        if (enchantable == null) {
            return list;
        }

        calculateAttributes();

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> baseEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.ENCHANTING_TABLE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> manaEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> frostEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> scorchEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> flowEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> chaosEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> greedEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> mightEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> manaBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> frostBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> scorchBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> flowBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> chaosBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> greedBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> mightBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT_BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> corruptionCurses = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.CURSE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        
        int enchantability = Math.max(0, enchantable.value() + this.powerAltars * 3 - this.stabilityAltars * 3);
        enchantingPower += 1 + random.nextInt(enchantability / 4 + 1) + random.nextInt(enchantability / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
        enchantingPower = Mth.clamp(Math.round((float)enchantingPower + (float)enchantingPower * f), 1, Integer.MAX_VALUE);

        List<EnchantmentInstance> baseList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, baseEnchantments.stream());

        List<EnchantmentInstance> manaList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, manaEnchantments.stream());
        List<EnchantmentInstance> frostList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, frostEnchantments.stream());
        List<EnchantmentInstance> scorchList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, scorchEnchantments.stream());
        List<EnchantmentInstance> flowList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, flowEnchantments.stream());
        List<EnchantmentInstance> chaosList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, chaosEnchantments.stream());
        List<EnchantmentInstance> greedList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, greedEnchantments.stream());
        List<EnchantmentInstance> mightList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, mightEnchantments.stream());

        List<EnchantmentInstance> manaBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, manaBlessings.stream());
        List<EnchantmentInstance> frostBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, frostBlessings.stream());
        List<EnchantmentInstance> scorchBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, scorchBlessings.stream());
        List<EnchantmentInstance> flowBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, flowBlessings.stream());
        List<EnchantmentInstance> chaosBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, chaosBlessings.stream());
        List<EnchantmentInstance> greedBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, greedBlessings.stream());
        List<EnchantmentInstance> mightBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, mightBlessings.stream());

        List<EnchantmentInstance> curseList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, corruptionCurses.stream());

        baseList = EnchantingHelper.evaluateEnchantments(stack, baseList);

        manaList = EnchantingHelper.evaluateEnchantments(stack, manaList);
        frostList = EnchantingHelper.evaluateEnchantments(stack, frostList);
        scorchList = EnchantingHelper.evaluateEnchantments(stack, scorchList);
        flowList = EnchantingHelper.evaluateEnchantments(stack, flowList);
        chaosList = EnchantingHelper.evaluateEnchantments(stack, chaosList);
        greedList = EnchantingHelper.evaluateEnchantments(stack, greedList);
        mightList = EnchantingHelper.evaluateEnchantments(stack, mightList);

        manaBlessingList = EnchantingHelper.evaluateEnchantments(stack, manaBlessingList);
        frostBlessingList = EnchantingHelper.evaluateEnchantments(stack, frostBlessingList);
        scorchBlessingList = EnchantingHelper.evaluateEnchantments(stack, scorchBlessingList);
        flowBlessingList = EnchantingHelper.evaluateEnchantments(stack, flowBlessingList);
        chaosBlessingList = EnchantingHelper.evaluateEnchantments(stack, chaosBlessingList);
        greedBlessingList = EnchantingHelper.evaluateEnchantments(stack, greedBlessingList);
        mightBlessingList = EnchantingHelper.evaluateEnchantments(stack, mightBlessingList);

        curseList = EnchantingHelper.evaluateEnchantments(stack, curseList);

        if (baseList.isEmpty()
                        && manaList.isEmpty() && frostList.isEmpty() && scorchList.isEmpty() && flowList.isEmpty() && chaosList.isEmpty() && greedList.isEmpty() && mightList.isEmpty()
                        && manaBlessingList.isEmpty() && frostBlessingList.isEmpty() && scorchBlessingList.isEmpty() && flowBlessingList.isEmpty() && chaosBlessingList.isEmpty() && greedBlessingList.isEmpty() && mightBlessingList.isEmpty()
                        && curseList.isEmpty()) {
            return list;
        }

        boolean firstEnchant = false;
        int attempts = 0;

        while ((random.nextInt(50) <= enchantingPower || !firstEnchant || list.isEmpty()) && attempts < 10) {
            if (!list.isEmpty()) {
                EnchantmentHelper.filterCompatibleEnchantments(baseList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(manaList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(frostList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(scorchList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(flowList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(chaosList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(greedList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(mightList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(manaBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(frostBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(scorchBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(flowBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(chaosBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(greedBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(mightBlessingList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(curseList, Util.lastOf(list));
            }

            // Divinity enchantment pools
            int manaBlessingWeight = Math.max(0, this.manaAltars * 3);
            int frostBlessingWeight = Math.max(0, this.frostAltars * 3);
            int scorchBlessingWeight = Math.max(0, this.scorchAltars * 3);
            int flowBlessingWeight = Math.max(0, this.flowAltars * 3);
            int chaosBlessingWeight = Math.max(0, this.chaosAltars * 3);
            int greedBlessingWeight = Math.max(0, this.greedAltars * 3);
            int mightBlessingWeight = Math.max(0, this.mightAltars * 3);

            int curseWeight = Math.max(0, this.corruption * 3);

            int totalWeight = this.mana + this.frost + this.scorch + this.flow + this.chaos + this.greed + this.might + curseWeight + manaBlessingWeight + frostBlessingWeight + scorchBlessingWeight + flowBlessingWeight + chaosBlessingWeight + greedBlessingWeight + mightBlessingWeight;

            if (totalWeight <= 0) {
                WeightedRandom.getRandomItem(random, baseList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            int randomValue = random.nextInt(totalWeight);
            int cumulative = 0;

            if (randomValue < (cumulative += this.mana)) {
                WeightedRandom.getRandomItem(random, manaList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.frost)) {
                WeightedRandom.getRandomItem(random, frostList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.scorch)) {
                WeightedRandom.getRandomItem(random, scorchList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.flow)) {
                WeightedRandom.getRandomItem(random, flowList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.chaos)) {
                WeightedRandom.getRandomItem(random, chaosList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.greed)) {
                WeightedRandom.getRandomItem(random, greedList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += this.might)) {
                WeightedRandom.getRandomItem(random, mightList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += manaBlessingWeight)) {
                WeightedRandom.getRandomItem(random, manaBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += frostBlessingWeight)) {
                WeightedRandom.getRandomItem(random, frostBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += scorchBlessingWeight)) {
                WeightedRandom.getRandomItem(random, scorchBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += flowBlessingWeight)) {
                WeightedRandom.getRandomItem(random, flowBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += chaosBlessingWeight)) {
                WeightedRandom.getRandomItem(random, chaosBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += greedBlessingWeight)) {
                WeightedRandom.getRandomItem(random, greedBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += mightBlessingWeight)) {
                WeightedRandom.getRandomItem(random, mightBlessingList, EnchantmentInstance::weight).ifPresent(list::add);
            } else if (randomValue < (cumulative += curseWeight)) {
            WeightedRandom.getRandomItem(random, curseList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            if (!firstEnchant) {
                firstEnchant = true;
            } else if (list.isEmpty()) {
                enchantingPower *= 2;
            } else if (!(slot + 1 > list.size() && EaEConfig.get.general.enchantment_limit >= 3)) {
                enchantingPower /= 2;
            }

            attempts += 1;
        }

        while (list.size() + EnchantingHelper.enchantmentScore(stack) > EaEConfig.get.general.enchantment_limit && EaEConfig.get.general.enchantment_limit >= 1) {
            list.remove(random.nextInt(list.size()));
        }

        return list;
    }

    @Override
    public Attributes calculateAttributes() {
        Attributes result = this.access.evaluate((level, tablePos) -> {
            int tBooks = 0, nBooks = 0, aBooks = 0, gBooks = 0, iBooks = 0;
            int tAltars = 0, aMana = 0, aFrost = 0, aScorch = 0, aFlow = 0, aChaos = 0, aGreed = 0, aMight = 0, aStability = 0, aPower = 0;

            // Count bookshelves and altars
            for (BlockPos off : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (tBooks < 15) {
                    if (enchantingBlockCheck(level, tablePos, off, Blocks.BOOKSHELF)) { nBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.ARCANE_BOOKSHELF)) { aBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.GLACIAL_BOOKSHELF)) { gBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.INFERNAL_BOOKSHELF)) { iBooks++; tBooks++; }
                }
                if (tAltars < 3 && enchantingBlockCheck(level, tablePos, off, EaEBlocks.ALTAR) && level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) != AltarBlockType.EMPTY) {
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.MANA_TOME) { aMana++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.FROST_TOME) { aFrost++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.SCORCH_TOME) { aScorch++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.FLOW_TOME) { aFlow++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.CHAOS_TOME) { aChaos++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.GREED_TOME) { aGreed++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.MIGHT_TOME) { aMight++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.STABILITY_TOME) { aStability++; tAltars++; }
                    if (level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) == AltarBlockType.POWER_TOME) { aPower++; tAltars++; }
                }
            }

            int locMana = 0, locFrost = 0, locScorch = 0, locFlow = 0, locChaos = 0, locGreed = 0, locMight = 0, locCorruption = 0, locDivinity = 0;

            locMana += nBooks / 2;
            locFrost += nBooks / 4;
            locScorch += nBooks / 4;
            locFlow += nBooks / 4;
            locChaos += nBooks / 4;
            locGreed += nBooks / 4;
            locMight += nBooks / 4;

            locMana += aBooks;
            locFlow += aBooks / 2;
            locGreed += aBooks / 2;
            locMight += aBooks / 4;

            locFrost += gBooks;
            locFlow += gBooks / 2;
            locChaos += gBooks / 2;
            locMight += gBooks / 4;

            locScorch += iBooks;
            locGreed += iBooks / 2;
            locChaos += iBooks / 2;
            locMight += iBooks / 4;

            locMana += aMana * 3;
            locChaos -= aMana * 5;
            locCorruption += aMana;
            locDivinity += aMana;

            locFrost += aFrost * 3;
            locScorch -= aFrost * 5;
            locCorruption += aFrost;
            locDivinity += aFrost;

            locScorch += aScorch * 3;
            locFrost -= aScorch * 5;
            locCorruption += aScorch;
            locDivinity += aScorch;

            locFlow += aFlow * 5;
            locCorruption += aFlow;
            locGreed -= aFlow * 3;
            locDivinity += aFlow;

            locChaos += aChaos * 5;
            locCorruption += aChaos;
            locMana -= aChaos * 3;
            locDivinity += aChaos;

            locGreed += aGreed * 5;
            locCorruption += aGreed;
            locFlow -= aGreed * 3;
            locDivinity += aGreed;

            locMight += aMight * 5;
            locCorruption += aMight;
            locChaos -= aMight;
            locGreed -= aMight;
            locFlow -= aMight;
            locScorch -= aMight;
            locFrost -= aMight;
            locMana -= aMight;
            locDivinity += aMight;

            locCorruption -= aStability;

            locMight -= aPower * 5;
            locChaos += aPower;
            locGreed += aPower;
            locFlow += aPower;
            locScorch += aPower;
            locFrost += aPower;
            locMana += aPower;

            this.totalBookshelves = tBooks;
            this.bookshelves = nBooks;
            this.arcaneBooksheves = aBooks;
            this.glacialBooksheves = gBooks;
            this.infernalBooksheves = iBooks;

            this.totalAltars = tAltars;
            this.manaAltars = aMana;
            this.frostAltars = aFrost;
            this.scorchAltars = aScorch;
            this.flowAltars = aFlow;
            this.chaosAltars = aChaos;
            this.greedAltars = aGreed;
            this.mightAltars = aMight;
            this.stabilityAltars = aStability;
            this.powerAltars = aPower;

            this.mana = locMana;
            this.frost = locFrost;
            this.scorch = locScorch;
            this.flow = locFlow;
            this.chaos = locChaos;
            this.greed = locGreed;
            this.might = locMight;
            this.corruption = locCorruption;
            this.divinity = locDivinity;

            return new Attributes(locMana, locFrost, locScorch, locFlow, locChaos, locGreed, locMight, locCorruption, locDivinity);
        }, new Attributes(this.mana, this.frost, this.scorch, this.flow, this.chaos, this.greed, this.might, this.corruption, this.divinity));

        return result;
    }
}