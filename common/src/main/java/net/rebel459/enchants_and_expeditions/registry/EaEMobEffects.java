package net.rebel459.enchants_and_expeditions.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.AbsorptionMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.unified.platform.UnifiedRegistries;

public class EaEMobEffects {

    static UnifiedRegistries.DeferredRegistry EFFECTS = UnifiedRegistries.DeferredRegistry.create(EnchantsAndExpeditions.MOD_ID, BuiltInRegistries.MOB_EFFECT);

    public static final Holder<MobEffect> LIGHTNING_IMMUNE = EFFECTS.registerHolder(
            "lightning_immune",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0)
    );

    public static final Holder<MobEffect> OVERHEAL = EFFECTS.registerHolder(
            "overheal",
            () -> new OverhealMobEffect(MobEffectCategory.BENEFICIAL, 2445989)
                    .addAttributeModifier(Attributes.MAX_ABSORPTION, EnchantsAndExpeditions.id("effect.overheal"), 1.0, AttributeModifier.Operation.ADD_VALUE)
    );

    public static final Holder<MobEffect> CELERITY = EFFECTS.registerHolder(
            "celerity",
            () -> new OverhealMobEffect(MobEffectCategory.BENEFICIAL, 0)
                    .addAttributeModifier(Attributes.MINING_EFFICIENCY, EnchantsAndExpeditions.id("effect.celerity"), 6.0, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, EnchantsAndExpeditions.id("effect.celerity"), 0.4, AttributeModifier.Operation.ADD_VALUE)
    );

    public static void init() {}

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