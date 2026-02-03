package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.AbsorptionMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EaEMobEffects {
    public static final Holder<MobEffect> LIGHTNING_IMMUNE = register(
            "lightning_immune",
            new MobEffect(MobEffectCategory.BENEFICIAL, 0)
    );

    public static final Holder<MobEffect> OVERHEAL = register(
            "overheal",
            new OverhealMobEffect(MobEffectCategory.BENEFICIAL, 2445989)
                    .addAttributeModifier(Attributes.MAX_ABSORPTION, EnchantsAndExpeditions.id("effect.overheal"), 1.0, AttributeModifier.Operation.ADD_VALUE)
    );

    public static void init() {
    }

    private static Holder<MobEffect> register(String path, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, EnchantsAndExpeditions.id(path), effect);
    }

    public static class OverhealMobEffect extends AbsorptionMobEffect {
        protected OverhealMobEffect(MobEffectCategory mobEffectCategory, int i) {
            super(mobEffectCategory, i);
        }

        public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
            return livingEntity.getAbsorptionAmount() > 0.0F;
        }

        public boolean shouldApplyEffectTickThisTick(int i, int j) {
            return true;
        }

        public void onEffectStarted(LivingEntity livingEntity, int i) {
            super.onEffectStarted(livingEntity, i);
            livingEntity.setAbsorptionAmount(Math.max(livingEntity.getAbsorptionAmount(), (float)(4 * (1 + i))));
        }
    }
}