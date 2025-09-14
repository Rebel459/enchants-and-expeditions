package net.legacy.enchants_and_expeditions.registry;

import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class EaEItems {

    // Items
    public static final Item ICE_SHARD = register("ice_shard",
            Item::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    // Tomes
    public static final Item TOME_OF_MANA = register("tome_of_mana",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_FROST = register("tome_of_frost",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_SCORCH = register("tome_of_scorch",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .fireResistant()
                    .stacksTo(1)
    );
    public static final Item TOME_OF_FLOW = register("tome_of_flow",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_CHAOS = register("tome_of_chaos",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_GREED = register("tome_of_greed",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_MIGHT = register("tome_of_might",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_STABILITY = register("tome_of_stability",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );
    public static final Item TOME_OF_POWER = register("tome_of_power",
            Item::new,
            new Item.Properties()
                    .rarity(Rarity.UNCOMMON)
                    .stacksTo(1)
    );

    public static void init() {
    }

    private static @NotNull <T extends Item> T register(String name, @NotNull Function<Item.Properties, Item> function, @NotNull Item.Properties properties) {
        return (T) Items.registerItem(ResourceKey.create(Registries.ITEM, EnchantsAndExpeditions.id(name)), function, properties);
    }

}