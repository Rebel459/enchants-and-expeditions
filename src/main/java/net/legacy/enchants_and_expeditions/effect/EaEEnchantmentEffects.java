package net.legacy.enchants_and_expeditions.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;

public class EaEEnchantmentEffects {

    private static void registerEntityEffect(final ResourceLocation identifier, final MapCodec<? extends EnchantmentEntityEffect> codec) {
        registerLocationBasedEffect(identifier, codec);
        Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, identifier, codec);
    }

    private static void registerLocationBasedEffect(final ResourceLocation identifier, final MapCodec<? extends EnchantmentLocationBasedEffect> codec) {
        Registry.register(BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, identifier, codec);
    }

    public static void register() {
        registerEntityEffect(EaEFreezeEffect.IDENTIFIER, EaEFreezeEffect.CODEC);
    }

    private EaEEnchantmentEffects() {}
}