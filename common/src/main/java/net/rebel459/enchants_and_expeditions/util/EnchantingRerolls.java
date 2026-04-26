package net.rebel459.enchants_and_expeditions.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record EnchantingRerolls(int rerolls, int randomAttempts, List<Integer> lastAttributes, int lastBookshelves, int lastAltars) {
    public static final Codec<EnchantingRerolls> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("rerolls").forGetter(EnchantingRerolls::rerolls),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("random_attempts").forGetter(EnchantingRerolls::randomAttempts),
                    Codec.list(Codec.INT).fieldOf("last_attributes").forGetter(EnchantingRerolls::lastAttributes),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("last_bookshelves").forGetter(EnchantingRerolls::lastBookshelves),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("last_altars").forGetter(EnchantingRerolls::lastAltars)
            ).apply(i, EnchantingRerolls::new)
    );

    public static final StreamCodec<ByteBuf, EnchantingRerolls> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EnchantingRerolls::rerolls,
            ByteBufCodecs.VAR_INT, EnchantingRerolls::randomAttempts,
            ByteBufCodecs.INT.apply(ByteBufCodecs.list()), EnchantingRerolls::lastAttributes,
            ByteBufCodecs.VAR_INT, EnchantingRerolls::lastBookshelves,
            ByteBufCodecs.VAR_INT, EnchantingRerolls::lastAltars,
            EnchantingRerolls::new
    );

    public boolean hasSameSetup(List<Integer> attributes, int bookshelves, int altars) {
        return this.lastAttributes.equals(attributes) && this.lastBookshelves == bookshelves && this.lastAltars == altars && !this.lastAttributes.isEmpty() && this.lastBookshelves != 0 && this.lastAltars != 0;
    }

    public EnchantingRerolls setRerolls(int rerolls) {
        return new EnchantingRerolls(rerolls, this.randomAttempts, this.lastAttributes, this.lastBookshelves, this.lastAltars);
    }

    public EnchantingRerolls setRandomAttempts(int randomAttempts) {
        return new EnchantingRerolls(this.rerolls, randomAttempts, this.lastAttributes, this.lastBookshelves, this.lastAltars);
    }

    public EnchantingRerolls setLastSetup(List<Integer> attributes, int bookshelves, int altars) {
        return new EnchantingRerolls(this.rerolls, this.randomAttempts, attributes, bookshelves, altars);
    }

    public static EnchantingRerolls create() {
        return new EnchantingRerolls(0, 0, List.of(), 0, 0);
    }

    public EnchantingRerolls {
        if (rerolls < 0 || randomAttempts < 0  || lastAltars < 0) {
            throw new IllegalArgumentException("Negative values are not supported");
        }
        if (lastAttributes == null) {
            throw new IllegalArgumentException("List may not be null, create an empty list instead");
        }
    }
}
