package net.legacy.enchants_and_expeditions.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record EaEFreezeEffect(LevelBasedValue duration) implements EnchantmentEntityEffect {

    public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath(EnchantsAndExpeditions.MOD_ID, "freeze");

    public static final MapCodec<EaEFreezeEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                    .group(LevelBasedValue.CODEC.fieldOf("duration").forGetter(effect -> effect.duration))
                    .apply(instance, EaEFreezeEffect::new)
    );

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity user, Vec3 pos) {
        user.setTicksFrozen((int) Math.ceil(duration.calculate(level)));

        world.sendParticles(ParticleTypes.SNOWFLAKE, user.getX(), user.getRandomY(), user.getZ(), 10, 0, -1, 0, 0.5);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}