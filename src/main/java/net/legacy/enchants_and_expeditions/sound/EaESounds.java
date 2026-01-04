package net.legacy.enchants_and_expeditions.sound;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class EaESounds {

	public static final SoundEvent TOME_PLACE = register("item.tome.place");
    public static final SoundEvent TOME_PICKUP = register("item.tome.pickup");

	@NotNull
	private static SoundEvent register(@NotNull String string) {
		ResourceLocation identifier = EnchantsAndExpeditions.id(string);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, EnchantsAndExpeditions.id(string), SoundEvent.createVariableRangeEvent(identifier));
	}

	private static Holder.@NotNull Reference<SoundEvent> registerForHolder(String id) {
		return registerForHolder(EnchantsAndExpeditions.id(id));
	}

	private static Holder.@NotNull Reference<SoundEvent> registerForHolder(ResourceLocation id) {
		return registerForHolder(id, id);
	}

	private static Holder.@NotNull Reference<SoundEvent> registerForHolder(ResourceLocation id, ResourceLocation soundId) {
		return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
	}

	public static void init() {}
}