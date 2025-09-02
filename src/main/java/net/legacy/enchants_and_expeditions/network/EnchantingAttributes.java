package net.legacy.enchants_and_expeditions.network;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface EnchantingAttributes {
    record Request() implements CustomPacketPayload {
        public static final Type<Request> ID = new Type<>(EnchantsAndExpeditions.id("request_enchanting_attributes"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Request> CODEC = new StreamCodec<>() {
            public Request decode(RegistryFriendlyByteBuf buf) { return new Request(); }
            public void encode(RegistryFriendlyByteBuf buf, Request value) { }
        };
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    record Attributes(int mana, int frost, int scorch, int flow, int chaos, int greed, int might, int stability, int divinity) implements CustomPacketPayload {
        public static final Type<Attributes> ID = new Type<>(EnchantsAndExpeditions.id("enchanting_attributes"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Attributes> CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Attributes::mana,
                ByteBufCodecs.INT, Attributes::frost,
                ByteBufCodecs.INT, Attributes::scorch,
                ByteBufCodecs.INT, Attributes::flow,
                ByteBufCodecs.INT, Attributes::chaos,
                ByteBufCodecs.INT, Attributes::greed,
                ByteBufCodecs.INT, Attributes::might,
                ByteBufCodecs.INT, Attributes::stability,
                ByteBufCodecs.INT, Attributes::divinity,
                Attributes::new
        );
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    Attributes calculateAttributes();
}