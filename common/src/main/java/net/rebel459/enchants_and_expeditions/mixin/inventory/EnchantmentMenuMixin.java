package net.rebel459.enchants_and_expeditions.mixin.inventory;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.enchants_and_expeditions.block.AltarBlock;
import net.rebel459.enchants_and_expeditions.block.AltarBlockType;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.registry.EaEDataComponents;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.network.EnchantingAttributes;
import net.rebel459.enchants_and_expeditions.registry.EaEBlocks;
import net.rebel459.enchants_and_expeditions.tag.EaEEnchantmentTags;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.rebel459.enchants_and_expeditions.util.EnchantmentInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin implements EnchantingAttributes {
    @Unique private static final int REROLL_CLUE = -2;
    @Unique private static final int NO_REROLL_CLUE = -3;

    @Shadow @Final private RandomSource random;
    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;
    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost);

    @Shadow
    @Final
    private DataSlot enchantmentSeed;

    @Shadow
    public abstract void slotsChanged(Container container);

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

    @Unique private List<ItemStack> chiseledBookshelfItems = new ArrayList<>();

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void onInit(int syncId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @Unique
    private static String EaE$blockId(BlockState state) {
        try {
            return state.getBlock().builtInRegistryHolder().key().identifier().toString();
        } catch (Throwable t) {
            return state.getBlock().getClass().getSimpleName();
        }
    }

    @Unique
    private boolean enchantingBlockCheck(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, Block block) {
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
    private int collectChiseledBookshelfItems(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, List<ItemStack> sink) {
        BlockPos targetPos = enchantingTablePos.offset(bookshelfPos);
        if (level.getBlockEntity(targetPos) instanceof ChiseledBookShelfBlockEntity chiseledBookShelf) {
            List<ItemStack> items = chiseledBookShelf.getItems();
            sink.addAll(items);
            return items.size();
        }
        return 0;
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
            if (!itemStack.isEmpty() && itemStack.isEnchantable() && !itemStack.is(Items.BOOK) && !itemStack.is(Items.ENCHANTED_BOOK)) {
                this.access.execute((level, blockPos) -> {
                    IdMap<Holder<net.minecraft.world.item.enchantment.Enchantment>> idMap =
                            level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
                    float ix = 0;

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

                    this.chiseledBookshelfItems.clear();

                    for (BlockPos off : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                        if (this.totalBookshelves < 15) {
                            if (EnchantingTableBlock.isValidBookShelf(level, blockPos, off)) {
                                ix++;
                            }
                            if (enchantingBlockCheck(level, blockPos, off, Blocks.CHISELED_BOOKSHELF)) {
                                this.totalBookshelves++;
                                ix += (collectChiseledBookshelfItems(level, blockPos, off, this.chiseledBookshelfItems) / 6F);
                            }
                            if (enchantingBlockCheck(level, blockPos, off, Blocks.BOOKSHELF)) {
                                this.bookshelves++;
                                this.totalBookshelves++;
                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.ARCANE_BOOKSHELF.get())) {
                                this.arcaneBooksheves++;
                                this.totalBookshelves++;

                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.GLACIAL_BOOKSHELF.get())) {
                                this.glacialBooksheves++;
                                this.totalBookshelves++;

                            }
                            if (enchantingBlockCheck(level, blockPos, off, EaEBlocks.INFERNAL_BOOKSHELF.get())) {
                                this.infernalBooksheves++;
                                this.totalBookshelves++;
                            }
                        }
                        if (this.totalAltars < 3 && enchantingBlockCheck(level, blockPos, off, EaEBlocks.ALTAR.get()) && level.getBlockState(getAltarPos(blockPos, off)).getValue(AltarBlock.TOME) != AltarBlockType.EMPTY) {
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

                    this.random.setSeed(this.enchantmentSeed.get());

                    int bookshelfPower = Math.round(ix);
                    int rerollPower = EnchantingHelper.getRerolls(itemStack) * 3;
                    EnchantmentInfo info = EnchantingHelper.getInfo(itemStack);

                    for (int j = 0; j < 3; j++) {
                        this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, bookshelfPower, itemStack);
                        if (this.costs[j] >= 1) {
                            this.costs[j] += rerollPower + info.standardEnchantments() + info.powerfulEnchantments() * 2 + info.blessings() * 3 + this.totalAltars * 3 - this.stabilityAltars * 6 - this.powerAltars * 6;
                        }
                        if (EnchantmentHelper.getEnchantmentCost(this.random, j, bookshelfPower, itemStack) >= 1) {
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
                            List<EnchantmentInstance> list = this.getEnchantmentList(level.registryAccess(), itemStack, jx, this.costs[jx] - rerollPower + this.powerAltars * 3);

                            if (list == null || list.isEmpty()) {
                                this.costs[jx] = 0;
                                this.enchantClue[jx] = -1;
                                this.levelClue[jx] = -1;
                                continue;
                            }

                            EnchantmentInstance inst = list.get(this.random.nextInt(list.size()));
                            this.enchantClue[jx] = idMap.getId(inst.enchantment());
                            this.levelClue[jx] = inst.level();
                        }
                    }

                    int rerollCost = EnchantingHelper.getRerollCost(itemStack);

                    if (this.enchantClue[0] == -1 && this.enchantClue[1] == -1 &&
                            this.enchantClue[2] == -1 &&
                            !(EnchantingHelper.allMaxLevel(itemStack) && this.totalAltars - this.powerAltars - this.stabilityAltars <= 0) &&
                            bookshelfPower * 2 + this.totalAltars * 3 - this.stabilityAltars * 3 - this.powerAltars * 3 > rerollCost
                    ) {
                        this.enchantClue[1] = EnchantingHelper.getRerolls(itemStack) < 3 ? REROLL_CLUE : NO_REROLL_CLUE;
                        this.levelClue[1] = rerollCost;
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
    private int calculateEnchantingPower(Enchantable enchantable, ItemStack stack, EnchantmentInfo info, int basePower, int secondaryPower, int slot) {
        int returnPower = (int) (basePower * 0.25 + secondaryPower * 1.25);
        int enchantability = Math.max(0, enchantable.value() + this.powerAltars * 3 - this.stabilityAltars * 3);
        returnPower += 1 + random.nextInt(enchantability / 4 + 1) + random.nextInt(enchantability / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
        returnPower = Mth.clamp(Math.round((float) returnPower + (float) returnPower * f), 1, Integer.MAX_VALUE);
        returnPower += EnchantingHelper.getRerolls(stack);
        int maxPower = 36;
        if (stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(info) == 0 && info.blessings() == 0 && EnchantingHelper.allMaxLevel(stack)) maxPower = 48;
        return Math.min(returnPower * (slot + 1) / 3, maxPower);
    }

    @Unique
    private List<EnchantmentInstance> EaE$selectEnchantment(RandomSource random, ItemStack stack, int slot, int enchantingPower, RegistryAccess registryAccess) {
        List<EnchantmentInstance> list = Lists.newArrayList();
        Enchantable enchantable = stack.get(DataComponents.ENCHANTABLE);
        if (enchantable == null || !EnchantingHelper.hasSlots(stack)) {
            return list;
        }

        calculateAttributes();

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> baseEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GENERIC).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> manaEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> frostEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> scorchEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> flowEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> chaosEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> greedEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> mightEnchantments = new ArrayList<>(registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT).map(HolderSet.Named::stream).orElse(Stream.empty()).toList());

        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> manaTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> frostTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> scorchTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> flowTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> chaosTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> greedTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<net.minecraft.world.item.enchantment.Enchantment>> mightTreasure = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT_TREASURE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();

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

        EnchantmentInfo info = EnchantingHelper.getInfo(stack);

        int basePower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, enchantingPower, slot);
        int manaPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.mana * 2, slot);
        int frostPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.frost * 2, slot);
        int scorchPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.scorch * 2, slot);
        int flowPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.flow * 2, slot);
        int chaosPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.chaos * 2, slot);
        int greedPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.greed * 2, slot);
        int mightPower = calculateEnchantingPower(enchantable, stack, info, enchantingPower, this.might * 2, slot);

        this.chiseledBookshelfItems.forEach(book -> book.getEnchantments().keySet().forEach(enchantment -> {
            if (enchantment.is(EaEEnchantmentTags.GENERIC_TREASURE) && !baseEnchantments.contains(enchantment)) baseEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.MANA_TREASURE) && !manaEnchantments.contains(enchantment)) manaEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.FROST_TREASURE) && !frostEnchantments.contains(enchantment)) frostEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.SCORCH_TREASURE) && !scorchEnchantments.contains(enchantment)) scorchEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.FLOW_TREASURE) && !flowEnchantments.contains(enchantment)) flowEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.CHAOS_TREASURE) && !chaosEnchantments.contains(enchantment)) chaosEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.GREED_TREASURE) && !greedEnchantments.contains(enchantment)) greedEnchantments.add(enchantment);
            else if (enchantment.is(EaEEnchantmentTags.MIGHT_TREASURE) && !mightEnchantments.contains(enchantment)) mightEnchantments.add(enchantment);
        }));

        List<EnchantmentInstance> baseList = EnchantmentHelper.getAvailableEnchantmentResults(basePower, stack, baseEnchantments.stream());
        
        List<EnchantmentInstance> manaList = EnchantmentHelper.getAvailableEnchantmentResults(manaPower, stack, manaEnchantments.stream());
        List<EnchantmentInstance> frostList = EnchantmentHelper.getAvailableEnchantmentResults(frostPower, stack, frostEnchantments.stream());
        List<EnchantmentInstance> scorchList = EnchantmentHelper.getAvailableEnchantmentResults(scorchPower, stack, scorchEnchantments.stream());
        List<EnchantmentInstance> flowList = EnchantmentHelper.getAvailableEnchantmentResults(flowPower, stack, flowEnchantments.stream());
        List<EnchantmentInstance> chaosList = EnchantmentHelper.getAvailableEnchantmentResults(chaosPower, stack, chaosEnchantments.stream());
        List<EnchantmentInstance> greedList = EnchantmentHelper.getAvailableEnchantmentResults(greedPower, stack, greedEnchantments.stream());
        List<EnchantmentInstance> mightList = EnchantmentHelper.getAvailableEnchantmentResults(mightPower, stack, mightEnchantments.stream());

        List<EnchantmentInstance> manaBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(manaPower, stack, manaBlessings.stream());
        List<EnchantmentInstance> frostBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(frostPower, stack, frostBlessings.stream());
        List<EnchantmentInstance> scorchBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(scorchPower, stack, scorchBlessings.stream());
        List<EnchantmentInstance> flowBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(flowPower, stack, flowBlessings.stream());
        List<EnchantmentInstance> chaosBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(chaosPower, stack, chaosBlessings.stream());
        List<EnchantmentInstance> greedBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(greedPower, stack, greedBlessings.stream());
        List<EnchantmentInstance> mightBlessingList = EnchantmentHelper.getAvailableEnchantmentResults(mightPower, stack, mightBlessings.stream());

        List<EnchantmentInstance> curseList = EnchantmentHelper.getAvailableEnchantmentResults(basePower, stack, corruptionCurses.stream());

        baseList = EnchantingHelper.evaluateEnchantments(stack, baseList, basePower);

        manaList = EnchantingHelper.evaluateEnchantments(stack, manaList, manaPower);
        frostList = EnchantingHelper.evaluateEnchantments(stack, frostList, frostPower);
        scorchList = EnchantingHelper.evaluateEnchantments(stack, scorchList, scorchPower);
        flowList = EnchantingHelper.evaluateEnchantments(stack, flowList, flowPower);
        chaosList = EnchantingHelper.evaluateEnchantments(stack, chaosList, chaosPower);
        greedList = EnchantingHelper.evaluateEnchantments(stack, greedList, greedPower);
        mightList = EnchantingHelper.evaluateEnchantments(stack, mightList, mightPower);

        manaBlessingList = EnchantingHelper.evaluateEnchantments(stack, manaBlessingList, manaPower);
        frostBlessingList = EnchantingHelper.evaluateEnchantments(stack, frostBlessingList, frostPower);
        scorchBlessingList = EnchantingHelper.evaluateEnchantments(stack, scorchBlessingList, scorchPower);
        flowBlessingList = EnchantingHelper.evaluateEnchantments(stack, flowBlessingList, flowPower);
        chaosBlessingList = EnchantingHelper.evaluateEnchantments(stack, chaosBlessingList, chaosPower);
        greedBlessingList = EnchantingHelper.evaluateEnchantments(stack, greedBlessingList, greedPower);
        mightBlessingList = EnchantingHelper.evaluateEnchantments(stack, mightBlessingList, mightPower);

        curseList = EnchantingHelper.evaluateEnchantments(stack, curseList, basePower);

        if (baseList.isEmpty()
                        && manaList.isEmpty() && frostList.isEmpty() && scorchList.isEmpty() && flowList.isEmpty() && chaosList.isEmpty() && greedList.isEmpty() && mightList.isEmpty()
                        && manaBlessingList.isEmpty() && frostBlessingList.isEmpty() && scorchBlessingList.isEmpty() && flowBlessingList.isEmpty() && chaosBlessingList.isEmpty() && greedBlessingList.isEmpty() && mightBlessingList.isEmpty()
                        && curseList.isEmpty()) {
            return list;
        }

        boolean baseTable = this.mana == 0 && this.frost == 0 && this.scorch == 0 && this.flow == 0 && this.chaos == 0 && this.greed == 0 && this.might == 0;

        boolean firstEnchant = false;
        int attempts = 0;

        int manaBlessingWeight = Math.max(0, this.manaAltars * 3);
        int frostBlessingWeight = Math.max(0, this.frostAltars * 3);
        int scorchBlessingWeight = Math.max(0, this.scorchAltars * 3);
        int flowBlessingWeight = Math.max(0, this.flowAltars * 3);
        int chaosBlessingWeight = Math.max(0, this.chaosAltars * 3);
        int greedBlessingWeight = Math.max(0, this.greedAltars * 3);
        int mightBlessingWeight = Math.max(0, this.mightAltars * 3);

        int curseWeight = Math.max(0, this.corruption * 3);

        int totalWeight = this.mana + this.frost + this.scorch + this.flow + this.chaos + this.greed + this.might + curseWeight + manaBlessingWeight + frostBlessingWeight + scorchBlessingWeight + flowBlessingWeight + chaosBlessingWeight + greedBlessingWeight + mightBlessingWeight;

        while ((random.nextInt(50) <= basePower || !firstEnchant || list.isEmpty()) && attempts < 10) {
            if (!list.isEmpty()) {
                EnchantmentHelper.filterCompatibleEnchantments(baseList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(manaList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(frostList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(scorchList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(flowList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(chaosList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(greedList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(mightList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(manaBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(frostBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(scorchBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(flowBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(chaosBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(greedBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(mightBlessingList, list.getLast());
                EnchantmentHelper.filterCompatibleEnchantments(curseList, list.getLast());
            }

            if (totalWeight <= 0) {
                WeightedRandom.getRandomItem(random, baseList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            if (!baseTable) {
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
            }
            else {
                WeightedRandom.getRandomItem(random, baseList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            if (!firstEnchant) {
                firstEnchant = true;
            } else if (list.isEmpty()) {
                basePower *= 2;
            } else {
                basePower /= 2;
            }

            attempts += 1;
        }

        if (EnchantingHelper.hasSlots(stack)) {
            while (EnchantingHelper.combinedEnchantmentScore(stack, list) > stack.get(EaEDataComponents.ENCHANTMENT_SLOTS.get()).getRemaining(stack) && EaEConfig.get().general.enchantment_slots) {
                if (list.size() == 1) {
                    list.removeFirst();
                    break;
                }
                list.remove(random.nextInt(1, list.size()));
            }
        }

        if (list.isEmpty() || !stack.isEnchantable()) {
            this.enchantClue[slot] = -1;
            this.levelClue[slot] = -1;
            this.costs[slot] = 0;
        }

        return list;
    }

    @Inject(method = "clickMenuButton", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$clickRerollButton(Player player, int buttonId, CallbackInfoReturnable<Boolean> cir) {
        if (buttonId == 1 && (this.enchantClue[1] == REROLL_CLUE || this.enchantClue[1] == NO_REROLL_CLUE)) {
            if (this.enchantClue[1] == NO_REROLL_CLUE || EnchantingHelper.getRerolls(this.enchantSlots.getItem(0)) >= 3) {
                cir.setReturnValue(false);
                return;
            }
            int rerollXpRequirement = this.levelClue[1] > 0 ? this.levelClue[1] : EnchantingHelper.getRerollCost(this.enchantSlots.getItem(0));
            int rerollCost = EnchantingHelper.calculateEnchantingCost(rerollXpRequirement, 2);
            boolean hasEnoughLapis = this.enchantSlots.getItem(1).getCount() >= rerollCost;
            boolean hasEnoughXp = player.experienceLevel >= rerollXpRequirement;
            if (!player.hasInfiniteMaterials() && (!hasEnoughLapis || !hasEnoughXp)) {
                cir.setReturnValue(false);
                return;
            }
            this.access.execute((level, pos) -> {
                ItemStack lapisStack = this.enchantSlots.getItem(1);
                player.onEnchantmentPerformed(this.enchantSlots.getItem(0), rerollCost);
                if (!player.hasInfiniteMaterials()) {
                    lapisStack.consume(rerollCost, player);
                    if (lapisStack.isEmpty()) {
                        this.enchantSlots.setItem(1, ItemStack.EMPTY);
                    }
                }
                EnchantingHelper.addReroll(this.enchantSlots.getItem(0));
                this.enchantSlots.setChanged();
                this.enchantmentSeed.set(player.getEnchantmentSeed());
                this.slotsChanged(this.enchantSlots);
                level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            });
            cir.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "clickMenuButton", at = @At(value = "STORE"), name = "enchantmentCost")
    private int EaE$correctEnchantmentcost(int enchantmentCost) {
        int slot = enchantmentCost - 1;
        return EnchantingHelper.calculateEnchantingCost(this.costs[slot], slot);
    }

    @Override
    public Attributes calculateAttributes() {
        Attributes result = this.access.evaluate((level, tablePos) -> {
            int tBooks = 0, nBooks = 0, aBooks = 0, gBooks = 0, iBooks = 0;
            int tAltars = 0, aMana = 0, aFrost = 0, aScorch = 0, aFlow = 0, aChaos = 0, aGreed = 0, aMight = 0, aStability = 0, aPower = 0;
            List<ItemStack> chiseledBooks = new ArrayList<>();

            // Count bookshelves and altars
            for (BlockPos off : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (tBooks < 15) {
                    if (enchantingBlockCheck(level, tablePos, off, Blocks.CHISELED_BOOKSHELF)) {
                        tBooks++;
                        collectChiseledBookshelfItems(level, tablePos, off, chiseledBooks);
                    }
                    if (enchantingBlockCheck(level, tablePos, off, Blocks.BOOKSHELF)) { nBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.ARCANE_BOOKSHELF.get())) { aBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.GLACIAL_BOOKSHELF.get())) { gBooks++; tBooks++; }
                    if (enchantingBlockCheck(level, tablePos, off, EaEBlocks.INFERNAL_BOOKSHELF.get())) { iBooks++; tBooks++; }
                }
                if (tAltars < 3 && enchantingBlockCheck(level, tablePos, off, EaEBlocks.ALTAR.get()) && level.getBlockState(getAltarPos(tablePos, off)).getValue(AltarBlock.TOME) != AltarBlockType.EMPTY) {
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

            double locMana = 0, locFrost = 0, locScorch = 0, locFlow = 0, locChaos = 0, locGreed = 0, locMight = 0, locCorruption = 0, locDivinity = 0;

            locMana += nBooks * 0.2;
            locFrost += nBooks * 0.2;
            locScorch += nBooks * 0.2;
            locFlow += nBooks * 0.2;
            locChaos += nBooks * 0.2;
            locGreed += nBooks * 0.2;
            locMight += nBooks * 0.2;

            locMana += aBooks;
            locFlow += aBooks * 0.5;
            locGreed += aBooks * 0.5;
            locMight += aBooks * 0.25;

            locFrost += gBooks;
            locFlow += gBooks * 0.5;
            locChaos += gBooks * 0.5;
            locMight += gBooks * 0.25;

            locScorch += iBooks;
            locGreed += iBooks * 0.5;
            locChaos += iBooks * 0.5;
            locMight += iBooks * 0.25;

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

            locMight += aMight * 7;
            locCorruption += aMight;
            locChaos -= aMight;
            locGreed -= aMight;
            locFlow -= aMight;
            locScorch -= aMight;
            locFrost -= aMight;
            locMana -= aMight;
            locDivinity += aMight;

            locCorruption -= aStability;
            locMight -= aStability * 5;

            locMight += aPower;
            locChaos += aPower;
            locGreed += aPower;
            locFlow += aPower;
            locScorch += aPower;
            locFrost += aPower;
            locMana += aPower;

            for (ItemStack book : chiseledBooks) {
                if (book.has(DataComponents.STORED_ENCHANTMENTS)) {
                    List<Double> bookAttributes = EnchantingHelper.getBookAttributes(book.get(DataComponents.STORED_ENCHANTMENTS));
                    locMana += bookAttributes.getFirst();
                    locFrost += bookAttributes.get(1);
                    locScorch += bookAttributes.get(2);
                    locFlow += bookAttributes.get(3);
                    locChaos += bookAttributes.get(4);
                    locGreed += bookAttributes.get(5);
                    locMight += bookAttributes.get(6);
                    locCorruption += bookAttributes.get(7);
                    locDivinity += bookAttributes.getLast();
                }
            }

            int finalMana = (int) Math.ceil(locMana);
            int finalFrost = (int) Math.ceil(locFrost);
            int finalScorch = (int) Math.ceil(locScorch);
            int finalFlow = (int) Math.ceil(locFlow);
            int finalChaos = (int) Math.ceil(locChaos);
            int finalGreed = (int) Math.ceil(locGreed);
            int finalMight = (int) Math.ceil(locMight);
            int finalCorruption = (int) Math.ceil(locCorruption);
            int finalDivinity = (int) Math.ceil(locDivinity);

            this.mana = finalMana;
            this.frost = finalFrost;
            this.scorch = finalScorch;
            this.flow = finalFlow;
            this.chaos = finalChaos;
            this.greed = finalGreed;
            this.might = finalMight;
            this.corruption = finalCorruption;
            this.divinity = finalDivinity;

            return new Attributes(finalMana, finalFrost, finalScorch, finalFlow, finalChaos, finalGreed, finalMight, finalCorruption, finalDivinity);
        }, new Attributes(this.mana, this.frost, this.scorch, this.flow, this.chaos, this.greed, this.might, this.corruption, this.divinity));

        return result;
    }
}
