package net.legacy.enchants_and_expeditions.mixin.entity;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.sound.EaESounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {

    @Shadow private @Nullable Entity lastDeflectedBy;

    @Shadow
    public abstract boolean deflect(ProjectileDeflection projectileDeflection, @org.jspecify.annotations.Nullable Entity entity, @org.jspecify.annotations.Nullable EntityReference<Entity> entityReference, boolean bl);

    @Shadow
    @org.jspecify.annotations.Nullable
    protected EntityReference<Entity> owner;

    @Inject(method = "hitTargetOrDeflectSelf", at = @At(value = "HEAD"), cancellable = true)
    public void slipstream(HitResult hitResult, CallbackInfoReturnable<ProjectileDeflection> cir) {
        Projectile projectile = Projectile.class.cast(this);
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity entity = entityHitResult.getEntity();
            if (entity instanceof LivingEntity livingEntity) {
                tryDeflect(livingEntity, projectile, cir);
                for (int x = 0; x < entity.getPassengers().size(); x++) {
                    if (entity.getPassengers().get(x) instanceof LivingEntity passenger) tryDeflect(passenger, projectile, cir);
                }
            }
        }
    }

    @Unique public void tryDeflect(LivingEntity entity, Projectile projectile, CallbackInfoReturnable<ProjectileDeflection> cir) {
        ItemStack stack = ItemStack.EMPTY;
        Nautilus nautilus = null;
        if (entity.getVehicle() instanceof Nautilus checkedNautilus) {
            stack = checkedNautilus.getItemBySlot(EquipmentSlot.BODY);
            nautilus = checkedNautilus;
        }
        if (entity instanceof Nautilus checkedNautilus) {
            stack = checkedNautilus.getItemBySlot(EquipmentSlot.BODY);
            nautilus = checkedNautilus;
        }
        if (nautilus == null) return;
        if (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.SLIPSTREAM) && (nautilus.isDashing() || nautilus.getJumpCooldown() > 30) && entity.isInWater()) {
            ProjectileDeflection projectileDeflection = ProjectileDeflection.MOMENTUM_DEFLECT;
            if (entity != this.lastDeflectedBy && this.deflect(projectileDeflection, entity, this.owner, false)) {
                this.lastDeflectedBy = entity;
            }
            Level level = nautilus.level();
            level.playSound(null, projectile.blockPosition(), EaESounds.SLIPSTREAM_DEFLECT, entity.getSoundSource(), 1.5F, 1F);
            cir.setReturnValue(projectileDeflection);
        }
    }
}