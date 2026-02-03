package net.legacy.enchants_and_expeditions.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HappyGhast.class)
public class HappyGhastMixin {

    @Unique
    private Player player;

    @WrapOperation(
            method = "scanPlayerAboveGhast",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getRootVehicle()Lnet/minecraft/world/entity/Entity;")
    )
    private Entity playerScan(Player player, Operation<Entity> original) {
        this.player = player;
        return original.call(player);
    }

    @WrapOperation(
            method = "scanPlayerAboveGhast",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;contains(Lnet/minecraft/world/phys/Vec3;)Z")
    )
    private boolean scannedPlayerFeatherFlight(AABB instance, Vec3 vec3, Operation<Boolean> original) {
        boolean bl = original.call(instance, vec3);
        HappyGhast happyGhast = HappyGhast.class.cast(this);
        ItemStack stack = happyGhast.getItemBySlot(EquipmentSlot.BODY);
        if (bl && !stack.isEmpty() && EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FEATHER_FLIGHT)) {
            this.player.addTag("has_feather_flight");
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, MobEffectInstance.INFINITE_DURATION));
        }
        return bl;
    }

    @Inject(
            method = "tickRidden",
            at = @At(value = "TAIL")
    )
    private void ridingPlayerFeatherFlight(Player player, Vec3 vec3, CallbackInfo ci) {
        HappyGhast happyGhast = HappyGhast.class.cast(this);
        ItemStack stack = happyGhast.getItemBySlot(EquipmentSlot.BODY);
        if (!stack.isEmpty() && EnchantingHelper.hasEnchantment(stack, EaEEnchantments.FEATHER_FLIGHT)) {
            player.addTag("has_feather_flight");
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, MobEffectInstance.INFINITE_DURATION));
        }
    }
}