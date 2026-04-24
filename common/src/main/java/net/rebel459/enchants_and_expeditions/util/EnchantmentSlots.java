package net.rebel459.enchants_and_expeditions.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public record EnchantmentSlots(int slots, int modifier) {
    public static final Codec<EnchantmentSlots> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("slots").forGetter(EnchantmentSlots::slots),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("modifier").forGetter(EnchantmentSlots::modifier)
            ).apply(i, EnchantmentSlots::new)
    );

    public static final StreamCodec<ByteBuf, EnchantmentSlots> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EnchantmentSlots::slots,
            ByteBufCodecs.VAR_INT, EnchantmentSlots::modifier,
            EnchantmentSlots::new
    );

    public int getTotal() {
        return this.slots + this.modifier;
    }

    public EnchantmentSlots setModifier(int modifier) {
        return new EnchantmentSlots(this.slots, 0);
    }

    public int getRemaining(ItemStack stack) {
        return getRemaining(EnchantingHelper.getInfo(stack));
    }

    public int getRemaining(EnchantmentInfo info) {
        return getTotal() - info.slotsUsed();
    }

    public static EnchantmentSlots create(int slots) {
        return new EnchantmentSlots(slots, 0);
    }

    public EnchantmentSlots {
        if (slots < 0) {
            throw new IllegalArgumentException("Base slot count must not be negative, but was " + slots);
        }
        else if (getTotal() < 0) {
            throw new IllegalArgumentException("Total slot count must not be negative, but was " + getTotal() + "(" + slots + " + " +  modifier + ")");
        }
    }
}
