package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EaEMobEffects {
    public static final Holder<MobEffect> LIGHTNING_IMMUNE = register(
            "lightning_immune",
            new MobEffect(MobEffectCategory.BENEFICIAL, 0)
    );

    public static void init() {
    }

    private static Holder<MobEffect> register(String path, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, EnchantsAndExpeditions.id(path), effect);
    }
}