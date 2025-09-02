package net.legacy.enchants_and_expeditions.mixin.inventory;

import com.google.common.collect.Lists;
import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEBlocks;
import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.legacy.enchants_and_expeditions.util.EnchantingAttributes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import java.util.stream.Stream;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin implements EnchantingAttributes {

    @Override
    public Attributes calculateAttributes() {
        int mana = 0;
        int frost = 0;
        int scorch = 0;
        int flow = 0;
        int chaos = 0;
        int greed = 0;
        int might = 0;
        int stability = 10; // Default value
        int divinity = 0;

        // Bookshelf
        mana += this.bookshelves / 2;
        frost += this.bookshelves / 4;
        scorch += this.bookshelves / 4;
        flow += this.bookshelves / 4;
        chaos += this.bookshelves / 4;
        greed += this.bookshelves / 4;
        might += this.bookshelves / 4;
        stability += this.bookshelves / 4;

        // Arcane Bookshelf
        mana += this.arcaneBooksheves;
        flow += this.arcaneBooksheves / 2;
        greed += this.arcaneBooksheves / 2;
        might += this.arcaneBooksheves / 4;

        // Glacial Bookshelf
        frost += this.glacialBooksheves;
        flow += this.glacialBooksheves / 2;
        chaos += this.glacialBooksheves / 2;
        might += this.glacialBooksheves / 4;

        // Infernal Bookshelf
        scorch += this.infernalBooksheves;
        greed += this.infernalBooksheves / 2;
        chaos += this.infernalBooksheves / 2;
        might += this.infernalBooksheves / 4;

        // Altar of Mana
        mana += this.manaAltars * 3;
        flow -= this.manaAltars;
        chaos -= this.manaAltars;
        greed -= this.manaAltars;
        might -= this.manaAltars;
        stability -= this.manaAltars;
        divinity += this.manaAltars;

        // Altar of Frost
        frost += this.frostAltars * 3;
        scorch -= this.frostAltars * 5;
        stability -= this.frostAltars;
        divinity += this.frostAltars;

        // Altar of Scorch
        scorch += this.scorchAltars * 3;
        frost -= this.scorchAltars * 5;
        stability -= this.scorchAltars;
        divinity += this.scorchAltars;

        // Altar of Flow
        flow += this.flowAltars * 5;
        stability -= this.flowAltars * 3;
        divinity += this.flowAltars;

        // Altar of Chaos
        chaos += this.chaosAltars * 5;
        stability -= this.chaosAltars * 3;
        divinity += this.chaosAltars;

        // Altar of Greed
        greed += this.greedAltars * 5;
        stability -= this.greedAltars * 3;
        divinity += this.greedAltars;

        // Altar of Might
        might += this.mightAltars * 5;
        stability -= this.mightAltars * 3;
        divinity += this.mightAltars;

        return new Attributes(mana, frost, scorch, flow, chaos, greed, might, stability, divinity);
    }

    @Shadow @Final private RandomSource random;

    @Shadow @Final private DataSlot enchantmentSeed;

    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final public int[] costs;
    @Shadow @Final public int[] enchantClue;
    @Shadow @Final public int[] levelClue;

    @Shadow protected abstract List<EnchantmentInstance> getEnchantmentList(RegistryAccess registryAccess, ItemStack stack, int slot, int cost);

    @Unique
    private Player player;

    @Unique
    private int totalBookshelves = 0;
    @Unique
    private int bookshelves = 0;
    @Unique
    private int arcaneBooksheves = 0;
    @Unique
    private int glacialBooksheves = 0;
    @Unique
    private int infernalBooksheves = 0;

    @Unique
    private int totalAltars = 0;
    @Unique
    private int manaAltars = 0;
    @Unique
    private int frostAltars = 0;
    @Unique
    private int scorchAltars = 0;
    @Unique
    private int flowAltars = 0;
    @Unique
    private int chaosAltars = 0;
    @Unique
    private int greedAltars = 0;
    @Unique
    private int mightAltars = 0;

    @Unique
    private int mana = 0;
    @Unique
    private int frost = 0;
    @Unique
    private int scorch = 0;
    @Unique
    private int flow = 0;
    @Unique
    private int chaos = 0;
    @Unique
    private int greed = 0;
    @Unique
    private int might = 0;
    @Unique
    private int stability = 0;
    @Unique
    private int divinity = 0;

    // Inject into constructor to capture the player
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void onInit(int syncId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.player = playerInventory.player;
    }

    @Unique
    private static boolean enchantingBlockCheck(Level level, BlockPos enchantingTablePos, BlockPos bookshelfPos, Block block) {
        return level.getBlockState(enchantingTablePos.offset(bookshelfPos)).is(block)
                && level.getBlockState(enchantingTablePos.offset(bookshelfPos.getX() / 2, bookshelfPos.getY(), bookshelfPos.getZ() / 2))
                .is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }

    @Inject(method = "slotsChanged", at = @At(value = "HEAD"), cancellable = true)
    private void EaE$slotsChanged(Container container, CallbackInfo ci) {
        EnchantmentMenu enchantmentMenu = EnchantmentMenu.class.cast(this);
        if (container == this.enchantSlots) {
            ItemStack itemStack = container.getItem(0);
            if (!itemStack.isEmpty() && (itemStack.isEnchantable())) {
                this.access.execute((level, blockPos) -> {
                    IdMap<Holder<Enchantment>> idMap = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
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

                    for (BlockPos blockPos2 : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                        if (EnchantingTableBlock.isValidBookShelf(level, blockPos, blockPos2)) {
                            ix++;
                            this.mana++;
                        }
                        if (this.totalBookshelves < 15) {
                            if (enchantingBlockCheck(level, blockPos, blockPos2, Blocks.BOOKSHELF)) {
                                this.bookshelves++;
                                this.totalBookshelves++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.ARCANE_BOOKSHELF)) {
                                this.arcaneBooksheves++;
                                this.totalBookshelves++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.GLACIAL_BOOKSHELF)) {
                                this.glacialBooksheves++;
                                this.totalBookshelves++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.INFERNAL_BOOKSHELF)) {
                                this.infernalBooksheves++;
                                this.totalBookshelves++;
                            }
                        }
                        if (this.totalAltars < 3) {
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.MANA_ALTAR)) {
                                this.manaAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.FROST_ALTAR)) {
                                this.frostAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.SCORCH_ALTAR)) {
                                this.scorchAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.FLOW_ALTAR)) {
                                this.flowAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.CHAOS_ALTAR)) {
                                this.chaosAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.GREED_ALTAR)) {
                                this.greedAltars++;
                                this.totalAltars++;
                            }
                            if (enchantingBlockCheck(level, blockPos, blockPos2, EaEBlocks.MIGHT_ALTAR)) {
                                this.mightAltars++;
                                this.totalAltars++;
                            }
                        }
                    }
                    calculateAttributes();

                    this.random.setSeed((long)this.enchantmentSeed.get());

                    for (int j = 0; j < 3; j++) {
                        this.costs[j] = EnchantmentHelper.getEnchantmentCost(this.random, j, ix, itemStack);
                        if (this.costs[j] >= 1) {
                            this.costs[j] += itemStack.getEnchantments().size() * 3 + EnchantingHelper.getBlessings(itemStack) * 3 - EnchantingHelper.getCurses(itemStack) * 3 + this.totalAltars * 3;
                        }
                        this.enchantClue[j] = -1;
                        this.levelClue[j] = -1;
                        if (this.costs[j] < j + 1) {
                            this.costs[j] = 0;
                        }
                    }

                    for (int jx = 0; jx < 3; jx++) {
                        if (this.costs[jx] > 0) {
                            List<EnchantmentInstance> list = this.getEnchantmentList(level.registryAccess(), itemStack, jx, this.costs[jx]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentInstance enchantmentInstance = (EnchantmentInstance)list.get(this.random.nextInt(list.size()));
                                this.enchantClue[jx] = idMap.getId(enchantmentInstance.enchantment());
                                this.levelClue[jx] = enchantmentInstance.level();
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
        this.random.setSeed((long)(this.enchantmentSeed.get() + slot));
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

        // Enchantment tags
        List<Holder<Enchantment>> manaEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MANA).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> frostEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FROST).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> scorchEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.SCORCH).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> flowEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.FLOW).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> chaosEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.CHAOS).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> greedEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.GREED).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> mightEnchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.MIGHT).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> stabilityCurses = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.CURSE).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();
        List<Holder<Enchantment>> divinityBlessings = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(EaEEnchantmentTags.BLESSING).map(HolderSet.Named::stream).orElse(Stream.empty()).toList();

        enchantingPower += 1 + random.nextInt(enchantable.value() / 4 + 1) + random.nextInt(enchantable.value() / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
        enchantingPower = Mth.clamp(Math.round((float)enchantingPower + (float)enchantingPower * f), 1, Integer.MAX_VALUE);

        // Add enchants to list
        List<EnchantmentInstance> list2 = Lists.newArrayList();
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, manaEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, frostEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, scorchEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, flowEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, chaosEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, greedEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, mightEnchantments.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, stabilityCurses.stream()));
        list2.addAll(EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, divinityBlessings.stream()));

        List<EnchantmentInstance> manaList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, manaEnchantments.stream());
        List<EnchantmentInstance> frostList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, frostEnchantments.stream());
        List<EnchantmentInstance> scorchList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, scorchEnchantments.stream());
        List<EnchantmentInstance> flowList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, flowEnchantments.stream());
        List<EnchantmentInstance> chaosList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, chaosEnchantments.stream());
        List<EnchantmentInstance> greedList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, greedEnchantments.stream());
        List<EnchantmentInstance> mightList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, mightEnchantments.stream());
        List<EnchantmentInstance> curseList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, stabilityCurses.stream());
        List<EnchantmentInstance> blessingList = EnchantmentHelper.getAvailableEnchantmentResults(enchantingPower, stack, divinityBlessings.stream());

        // Filter enchantments for re-enchanting
        manaList = EnchantingHelper.evaluateEnchantments(stack, manaList);
        frostList = EnchantingHelper.evaluateEnchantments(stack, frostList);
        scorchList = EnchantingHelper.evaluateEnchantments(stack, scorchList);
        flowList = EnchantingHelper.evaluateEnchantments(stack, flowList);
        chaosList = EnchantingHelper.evaluateEnchantments(stack, chaosList);
        greedList = EnchantingHelper.evaluateEnchantments(stack, greedList);
        mightList = EnchantingHelper.evaluateEnchantments(stack, mightList);
        curseList = EnchantingHelper.evaluateEnchantments(stack, curseList);
        blessingList = EnchantingHelper.evaluateEnchantments(stack, blessingList);

        // Check if new list is empty
        if (list2.isEmpty()) {
            return list;
        }

        // Setup
        boolean firstEnchant = false;

        int attempts = 0;

        // Add enchantments
        while ((random.nextInt(50) <= enchantingPower || !firstEnchant || list.isEmpty()) && attempts < 10) {
            if (!list.isEmpty()) {
                EnchantmentHelper.filterCompatibleEnchantments(manaList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(frostList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(scorchList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(flowList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(chaosList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(greedList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(mightList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(curseList, Util.lastOf(list));
                EnchantmentHelper.filterCompatibleEnchantments(blessingList, Util.lastOf(list));
            }

            // Curses & Blessings
            int curseWeight = 10 - this.stability;
            if (curseWeight < 0) {
                curseWeight = 0;
            }
            int blessingWeight = this.divinity;
            if (this.divinity >= 1) {
                blessingWeight += 2;
            }
            if (blessingWeight < 0) {
                blessingWeight = 0;
            }

            if (EnchantingHelper.enchantmentScore(stack) >= EaEConfig.get.enchanting.enchantment_limit && EnchantingHelper.getBlessings(stack) == 0 && this.divinity >= 1) {
                WeightedRandom.getRandomItem(random, blessingList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            // Calculate total weight
            int totalWeight = this.mana + this.frost + this.scorch + this.flow + this.chaos + this.greed + this.might + curseWeight + blessingWeight;

            if (totalWeight <= 0) {
                break;
            }

            int randomValue = random.nextInt(totalWeight);

            int cumulativeWeight = 0;

            if (randomValue < (cumulativeWeight += this.mana)) {
                WeightedRandom.getRandomItem(random, manaList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.frost)) {
                WeightedRandom.getRandomItem(random, frostList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.scorch)) {
                WeightedRandom.getRandomItem(random, scorchList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.flow)) {
                WeightedRandom.getRandomItem(random, flowList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.chaos)) {
                WeightedRandom.getRandomItem(random, chaosList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.greed)) {
                WeightedRandom.getRandomItem(random, greedList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += this.might)) {
                WeightedRandom.getRandomItem(random, mightList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += curseWeight)) {
                WeightedRandom.getRandomItem(random, curseList, EnchantmentInstance::weight).ifPresent(list::add);
            }
            else if (randomValue < (cumulativeWeight += blessingWeight)) {
                WeightedRandom.getRandomItem(random, blessingList, EnchantmentInstance::weight).ifPresent(list::add);
            }

            if (!firstEnchant) {
                firstEnchant = true;
            }
            else if (list.isEmpty()) {
                enchantingPower *= 2;
            }
            else if (!(slot + 1 > list.size() && EaEConfig.get.enchanting.enchantment_limit >= 3)) {
                enchantingPower /= 2;
            }

            attempts += 1;
        }

        // Limit the list size
        while (list.size() + EnchantingHelper.enchantmentScore(stack) > EaEConfig.get.enchanting.enchantment_limit && EaEConfig.get.enchanting.enchantment_limit >= 1) {
            list.remove(random.nextInt(list.size()));
        }

        return list;
    }
}