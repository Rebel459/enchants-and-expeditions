package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.config.EaEConfig;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow public int experienceLevel;

    @Inject(method = "getXpNeededForNextLevel", at = @At(value = "HEAD"), cancellable = true)
    protected void EaE$experienceRebalance(CallbackInfoReturnable<Integer> cir) {
        if (!EaEConfig.get.general.experience_rebalance) return;

        // 0-30

        if (this.experienceLevel <= 5)
            cir.setReturnValue(20);
        else if (this.experienceLevel <= 10)
            cir.setReturnValue(30);
        else if (this.experienceLevel <= 15)
            cir.setReturnValue(40);
        else if (this.experienceLevel <= 20)
            cir.setReturnValue(55);
        else if (this.experienceLevel <= 25)
            cir.setReturnValue(70);
        else if (this.experienceLevel <= 30)
            cir.setReturnValue(85);

        // 31-90

        else if (this.experienceLevel <= 40)
            cir.setReturnValue(100);
        else if (this.experienceLevel <= 50)
            cir.setReturnValue(110);
        else if (this.experienceLevel <= 60)
            cir.setReturnValue(120);
        else if (this.experienceLevel <= 70)
            cir.setReturnValue(130);
        else if (this.experienceLevel <= 80)
            cir.setReturnValue(140);
        else if (this.experienceLevel <= 90)
            cir.setReturnValue(150);

        // 91+

        else if (this.experienceLevel == 91)
            cir.setReturnValue(200);
        else if (this.experienceLevel == 92)
            cir.setReturnValue(250);
        else if (this.experienceLevel == 93)
            cir.setReturnValue(300);
        else if (this.experienceLevel == 94)
            cir.setReturnValue(350);
        else if (this.experienceLevel == 95)
            cir.setReturnValue(400);
        else if (this.experienceLevel == 96)
            cir.setReturnValue(500);
        else if (this.experienceLevel == 97)
            cir.setReturnValue(600);
        else if (this.experienceLevel == 98)
            cir.setReturnValue(800);
        else if (this.experienceLevel == 99)
            cir.setReturnValue(1100);
        else if (this.experienceLevel == 100)
            cir.setReturnValue(1500);
        else
            cir.setReturnValue(5000);
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
}