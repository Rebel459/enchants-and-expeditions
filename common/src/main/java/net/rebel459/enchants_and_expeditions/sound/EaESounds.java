package net.rebel459.enchants_and_expeditions.sound;

import net.minecraft.sounds.SoundEvent;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public class EaESounds {

	static UnifiedRegistries.SoundEvents SOUNDS = UnifiedRegistries.SoundEvents.create(EnchantsAndExpeditions.MOD_ID);

	public static final Supplied<SoundEvent> TOME_PLACE = SOUNDS.register("item.tome.place");
    public static final Supplied<SoundEvent> TOME_PICKUP = SOUNDS.register("item.tome.pickup");

    public static final Supplied<SoundEvent> JOUSTING_RESTORE = SOUNDS.register("enchantment.jousting.restore");
    public static final Supplied<SoundEvent> JOUSTING_RESTORE_HORSE = SOUNDS.register("enchantment.jousting.restore_horse");
    public static final Supplied<SoundEvent> SLIPSTREAM_DEFLECT = SOUNDS.register("enchantment.slipstream.deflect");

	public static void init() {}
}