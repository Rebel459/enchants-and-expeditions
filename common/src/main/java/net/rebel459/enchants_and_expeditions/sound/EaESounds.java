package net.rebel459.enchants_and_expeditions.sound;

import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.rebel459.unified.platform.UnifiedRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class EaESounds {

	static UnifiedRegistries.SoundEvents SOUNDS = UnifiedRegistries.SoundEvents.create(EnchantsAndExpeditions.MOD_ID);

	public static final Supplier<SoundEvent> TOME_PLACE = SOUNDS.register("item.tome.place");
    public static final Supplier<SoundEvent> TOME_PICKUP = SOUNDS.register("item.tome.pickup");

    public static final Supplier<SoundEvent> JOUSTING_RESTORE = SOUNDS.register("enchantment.jousting.restore");
    public static final Supplier<SoundEvent> JOUSTING_RESTORE_HORSE = SOUNDS.register("enchantment.jousting.restore_horse");
    public static final Supplier<SoundEvent> SLIPSTREAM_DEFLECT = SOUNDS.register("enchantment.slipstream.deflect");

	public static void init() {}
}