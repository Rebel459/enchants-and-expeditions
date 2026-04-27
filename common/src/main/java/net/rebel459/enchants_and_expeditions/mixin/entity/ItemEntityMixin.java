package net.rebel459.enchants_and_expeditions.mixin.entity;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.util.FeatherFlightInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Unique
    private int secondProgress = 0;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.secondProgress < 20) {
            this.secondProgress++;
        }
        else {
            second();
            this.secondProgress = 0;
        }
    }

    @Unique
    private void second() {
        temperingBlessing();
        fluidityAndNeptuneBlessings();
    }

    @Unique
    private void temperingBlessing() {
        ItemEntity entity = ItemEntity.class.cast(this);
        if (!(entity.getRemainingFireTicks() > 0) && !entity.getBlockStateOn().is(BlockTags.FIRE) && !entity.getInBlockState().is(BlockTags.FIRE) && !entity.getBlockStateOn().getFluidState().is(FluidTags.LAVA) && !entity.getInBlockState().getFluidState().is(FluidTags.LAVA)) return;
        ItemStack stack = entity.getItem();
        EnchantingHelper.repairItem(stack, EaEEnchantments.TEMPERING_BLESSING);
        entity.setItem(stack);
    }

    @Unique
    private boolean secondTime = false;

    @Unique
    private void fluidityAndNeptuneBlessings() {
        ItemEntity entity = ItemEntity.class.cast(this);
        if (!entity.isInWaterOrRain()) return;
        if (this.secondTime) {
            ItemStack stack = entity.getItem();
            EnchantingHelper.repairItem(stack, EaEEnchantments.FLUIDITY_BLESSING);
            entity.setItem(stack);        } else {
            this.secondTime = true;
        }
    }
}