/*
 * Elemental Enchantments - A mod adding elemental enchantments
 * Copyright (C) 2024-2025 Creerio
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
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

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
