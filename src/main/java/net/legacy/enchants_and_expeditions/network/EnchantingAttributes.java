package net.legacy.enchants_and_expeditions.network;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public interface EnchantingAttributes {

    record Request() implements CustomPacketPayload {
        public static final Type<Request> ID = new Type<>(EnchantsAndExpeditions.id("request_enchanting_attributes"));

        public static final StreamCodec<RegistryFriendlyByteBuf, Request> CODEC = new StreamCodec<>() {
            @Override
            public Request decode(RegistryFriendlyByteBuf buf) { return new Request(); }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, Request value) { }
        };

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    record Attributes(
            int mana, int frost, int scorch, int flow, int chaos,
            int greed, int might, int corruption, int divinity
    ) implements CustomPacketPayload {

        public static final Type<Attributes> ID = new Type<>(EnchantsAndExpeditions.id("enchanting_attributes"));

        private record Group1(int mana, int frost, int scorch, int flow, int chaos) {}

        private static final StreamCodec<RegistryFriendlyByteBuf, Group1> GROUP1_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Group1::mana,
                ByteBufCodecs.INT, Group1::frost,
                ByteBufCodecs.INT, Group1::scorch,
                ByteBufCodecs.INT, Group1::flow,
                ByteBufCodecs.INT, Group1::chaos,
                Group1::new
        );

        private record Group2(int greed, int might, int corruption, int divinity) {}

        private static final StreamCodec<RegistryFriendlyByteBuf, Group2> GROUP2_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Group2::greed,
                ByteBufCodecs.INT, Group2::might,
                ByteBufCodecs.INT, Group2::corruption,
                ByteBufCodecs.INT, Group2::divinity,
                Group2::new
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Attributes> CODEC = StreamCodec.composite(
                GROUP1_CODEC, attr -> new Group1(attr.mana, attr.frost, attr.scorch, attr.flow, attr.chaos),
                GROUP2_CODEC, attr -> new Group2(attr.greed, attr.might, attr.corruption, attr.divinity),
                (group1, group2) -> new Attributes(
                        group1.mana, group1.frost, group1.scorch, group1.flow, group1.chaos,
                        group2.greed, group2.might, group2.corruption, group2.divinity
                )
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    Attributes calculateAttributes();
}