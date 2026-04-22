package net.rebel459.enchants_and_expeditions.registry;

import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.SuppliedItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class EaEItems {
    
    static UnifiedRegistries.Items ITEMS = UnifiedRegistries.Items.create(EnchantsAndExpeditions.MOD_ID);

    // Items
    public static final SuppliedItem ICE_SHARD = ITEMS.register("ice_shard",
            Item::new,
            () -> new Item.Properties()
                    .stacksTo(64)
    );

    // Tomes
    public static final SuppliedItem TOME_OF_MANA = ITEMS.register("tome_of_mana",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_FROST = ITEMS.register("tome_of_frost",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_SCORCH = ITEMS.register("tome_of_scorch",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_FLOW = ITEMS.register("tome_of_flow",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_CHAOS = ITEMS.register("tome_of_chaos",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_GREED = ITEMS.register("tome_of_greed",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_MIGHT = ITEMS.register("tome_of_might",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_STABILITY = ITEMS.register("tome_of_stability",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );
    public static final SuppliedItem TOME_OF_POWER = ITEMS.register("tome_of_power",
            Item::new,
            () -> new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );

    public static void init() {}

}