package net.rebel459.enchants_and_expeditions.mixin.block;

import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"), cancellable = true)
    private static void shatteringCurse(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool, CallbackInfo ci) {
        if (EnchantingHelper.hasEnchantment(tool, EaEEnchantments.SHATTERING_CURSE)) {
            if (breaker.getRandom().nextInt(0, 6) == 5) ci.cancel();
        }
    }
}