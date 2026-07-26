package net.rebel459.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.enchants_and_expeditions.registry.EaEEnchantments;
import net.rebel459.enchants_and_expeditions.util.EnchantingHelper;
import net.rebel459.enchants_and_expeditions.util.FeatherFlightInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin implements FeatherFlightInterface {

    @Unique
    private boolean featherFlight;

    @Override
    public boolean getFeatherFlight() {
        return this.featherFlight;
    }

    @Override
    public void setFeatherFlight(boolean featherFlight) {
        this.featherFlight = featherFlight;
    }

    @Shadow public int experienceLevel;

    @Shadow public abstract SoundSource getSoundSource();

    @Inject(method = "getXpNeededForNextLevel", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$experienceRebalance(CallbackInfoReturnable<Integer> cir) {
        if (!EaEConfig.get().general.experience_rebalance) return;

        if (this.experienceLevel < 100)
            cir.setReturnValue(20 + this.experienceLevel * 2);
        else
            cir.setReturnValue(500 + (this.experienceLevel - 99) * 50);
    }

    @Inject(at = @At("TAIL"), cancellable = true, method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;")
    private void EaE$infinityBlessing(ItemStack weaponStack, CallbackInfoReturnable<ItemStack> cir) {
        Player player = Player.class.cast(this);
        if (player.level() instanceof ServerLevel level && cir.getReturnValue().isEmpty()) {
            if (EnchantmentHelper.processAmmoUse(level, weaponStack, Items.ARROW.getDefaultInstance(), 1) == 0) {
                cir.setReturnValue(Items.ARROW.getDefaultInstance());
            }
        }
    }

    @Inject(method = "killedEntity", at = @At(value = "HEAD"))
    private void bloodlust(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Player player = Player.class.cast(this);
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BLOODLUST)) {
            int amount = EnchantingHelper.getLevel(stack, EaEEnchantments.BLOODLUST);
            player.setHealth(player.getHealth() + amount);
            if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            serverLevel.playSound(player, player.blockPosition(), SoundEvents.THORNS_HIT, this.getSoundSource(), 1F, 1F);
        }
    }

    @Inject(method = "killedEntity", at = @At(value = "HEAD"))
    private void quickstep(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Player player = Player.class.cast(this);
        ItemStack stack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.QUICKSTEP)) {
            int seconds = EnchantingHelper.getLevel(stack, EaEEnchantments.QUICKSTEP) * 2;
            int ticks = seconds * 20;
            if (!player.hasEffect(MobEffects.SPEED) || player.getEffect(MobEffects.SPEED).getDuration() < ticks) player.addEffect(new MobEffectInstance(MobEffects.SPEED, ticks));
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void featherFlight(CallbackInfo ci) {
        Player player = Player.class.cast(this);
        if (player.hasEffect(MobEffects.SLOW_FALLING) && player.getEffect(MobEffects.SLOW_FALLING).isInfiniteDuration() && player instanceof FeatherFlightInterface flight && flight.getFeatherFlight()) {
            if (player.isFallFlying() || !player.getBlockStateOn().is(BlockTags.AIR) || player.isInWater()) {
                flight.setFeatherFlight(false);
                player.removeEffect(MobEffects.SLOW_FALLING);
            }
        }
    }

    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;canCriticalAttack(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean criticalStrike(Player player, Entity entity, Operation<Boolean> original) {
        boolean canCriticalAttack = original.call(player, entity);
        ItemStack stack = player.getWeaponItem();
        int level = EnchantingHelper.getLevel(stack, EaEEnchantments.CRITICAL_STRIKE);
        if (level > 0) {
            int random = player.getRandom().nextIntBetweenInclusive(1, 10);
            if (level + 1 >= random) canCriticalAttack  = true;
        }
        return canCriticalAttack;
    }
}