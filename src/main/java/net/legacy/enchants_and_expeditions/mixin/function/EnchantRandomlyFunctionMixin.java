package net.legacy.enchants_and_expeditions.mixin.function;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {

    @Shadow @Final private boolean onlyCompatible;

    @Shadow @Final public Optional<HolderSet<Enchantment>> options;

    @Unique
    ItemStack itemStack;

    @Unique
    RandomSource randomSource;

    @Inject(method = "run", at = @At(value = "HEAD"))
    protected void EaE$run(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        this.itemStack = stack;
        this.randomSource = context.getRandom();
    }

    @Redirect(
            method = "run(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/storage/loot/LootContext;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;")
    )
    private Stream<Holder<Enchantment>> EaE$filterRandomEnchantments(Stream<Holder<Enchantment>> stream, Predicate<? super Stream<Holder<Enchantment>>> predicate) {
        ItemStack stack = this.itemStack;
        RandomSource random = this.randomSource;
        boolean bl = stack.is(Items.BOOK);
        boolean bl2 = !bl && this.onlyCompatible;
        return stream.filter(holder -> {
            return (!bl2 || holder.value().canEnchant(stack)) && !EnchantingHelper.onRandomlyEnchantedLoot(holder, random) && enchantFallback(holder);
        });
    }

    @Unique
    public boolean enchantFallback(Holder<Enchantment> holder) {
        if (this.options.isPresent() || !EaEConfig.get.misc.enchant_function_fallback) return true;
        else return holder.is(EnchantmentTags.ON_RANDOM_LOOT);
    }
}