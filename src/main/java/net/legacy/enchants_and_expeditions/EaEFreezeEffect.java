/*
 * Elemental Enchantments - A mod adding elemental enchantments
 * Copyright (C) 2024 Creerio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.legacy.enchants_and_expeditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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