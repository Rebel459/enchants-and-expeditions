package net.legacy.enchants_and_expeditions.sound;

import net.minecraft.world.level.block.SoundType;

public final class EaEBlockSounds {

    public static final SoundType ARCANE_BOOKSHELF = new SoundType(1F, 1F,
            SoundType.CALCITE.getBreakSound(),
            SoundType.WOOD.getStepSound(),
            SoundType.CALCITE.getPlaceSound(),
            SoundType.WOOD.getHitSound(),
            SoundType.WOOD.getFallSound()
    );
    public static final SoundType GLACIAL_BOOKSHELF = new SoundType(1F, 1F,
            SoundType.GLASS.getBreakSound(),
            SoundType.WOOD.getStepSound(),
            SoundType.GLASS.getPlaceSound(),
            SoundType.WOOD.getHitSound(),
            SoundType.WOOD.getFallSound()
    );
    public static final SoundType INFERNAL_BOOKSHELF = new SoundType(1F, 1F,
            SoundType.GILDED_BLACKSTONE.getBreakSound(),
            SoundType.WOOD.getStepSound(),
            SoundType.GILDED_BLACKSTONE.getPlaceSound(),
            SoundType.WOOD.getHitSound(),
            SoundType.WOOD.getFallSound()
    );

    public static final SoundType ALTAR = new SoundType(1F, 1F,
            SoundType.AMETHYST.getBreakSound(),
            SoundType.DEEPSLATE_TILES.getStepSound(),
            SoundType.DEEPSLATE_TILES.getPlaceSound(),
            SoundType.DEEPSLATE_TILES.getHitSound(),
            SoundType.DEEPSLATE_TILES.getFallSound()
    );

    public static void init() {}
}