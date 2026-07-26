package net.rebel459.enchants_and_expeditions.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Config(name = EnchantsAndExpeditions.MOD_ID)
public class EaEConfig implements ConfigData {

    @Contract(pure = true)
    public static @NotNull Path configPath(boolean json5) {
        return Path.of("./config/" + EnchantsAndExpeditions.MOD_ID + "." + (json5 ? "json5" : "json"));
    }

    public static EaEConfig get() {
        if (!EnchantsAndExpeditions.registeredConfig) {
            init();
            EnchantsAndExpeditions.registeredConfig = true;
        }
        return AutoConfig.getConfigHolder(EaEConfig.class).getConfig();
    }

    public static void init() {
        Path json5Path = configPath(true);
        Path jsonPath = configPath(false);

        Path existingConfigPath = Files.exists(json5Path) ? json5Path : jsonPath;
        boolean hasExistingConfig = Files.exists(existingConfigPath);

        boolean restoreDisabledEnchantments =
                !hasExistingConfig || !configContainsField(existingConfigPath, "disabled_enchantments");

        boolean restoreEnchantmentSlots =
                !hasExistingConfig || !configContainsField(existingConfigPath, "item_enchantment_slots");

        AutoConfig.register(EaEConfig.class, JanksonConfigSerializer::new);

        var holder = AutoConfig.getConfigHolder(EaEConfig.class);
        EaEConfig config = holder.getConfig();

        if (config.normalizeDefaults(restoreDisabledEnchantments, restoreEnchantmentSlots)) {
            holder.save();
        }
    }

    @ConfigEntry.Gui.CollapsibleObject
    public GeneralConfig general = new GeneralConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public TooltipConfig tooltips = new TooltipConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public MiscConfig misc = new MiscConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public IntegrationConfig integrations = new IntegrationConfig();

    public static class GeneralConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean enchantment_slots = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean powerful_enchantments = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean repeat_table_enchanting = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean new_table_costs = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean experience_rebalance = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean updated_librarian_trades = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(max=1L)
        public double anvil_break_chance = 0.12;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean craftable_experience_bottles = true;
    }

    public static class TooltipConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public TooltipType slot_tooltip = TooltipType.ALWAYS;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public TooltipType enchantment_symbols = TooltipType.ALWAYS;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean ordered_enchantments = true;
    }

    public static class MiscConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public PanelVisibility panel_visibility = PanelVisibility.BOTH;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean enchant_function_fallback = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean loot_table_injects = true;
    }

    public static class IntegrationConfig {
        @ConfigEntry.Category("config")
        public boolean item_tooltips_overrides = true;

        @ConfigEntry.Category("config")
        public boolean combat_reborn = true;

        @ConfigEntry.Category("config")
        public boolean legacies_and_legends = true;

        @ConfigEntry.Category("config")
        public boolean trailier_tales = true;

        @ConfigEntry.Category("config")
        public boolean remnants = true;

        @ConfigEntry.Category("config")
        public boolean enderscape = true;
    }


    @ConfigEntry.Gui.Tooltip
    public List<String> disabled_enchantments = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    public List<ItemEnchantmentSlots> item_enchantment_slots = new ArrayList<>();

    private static List<String> defaultDisabledEnchantments() {
        return List.of(
                "#enchants_and_expeditions:disabled_enchantments"
        );
    }

    private static List<ItemEnchantmentSlots> defaultEnchantmentSlots() {
        return List.of(
                new ItemEnchantmentSlots("*wood", 3),
                new ItemEnchantmentSlots("*leather", 3),
                new ItemEnchantmentSlots("*stone", 3),
                new ItemEnchantmentSlots("*copper", 3),
                new ItemEnchantmentSlots("*chain", 4),
                new ItemEnchantmentSlots("*iron", 4),
                new ItemEnchantmentSlots("*golden", 5),
                new ItemEnchantmentSlots("*diamond", 4),
                new ItemEnchantmentSlots("*netherite", 4),
                new ItemEnchantmentSlots("*turtle", 4),
                new ItemEnchantmentSlots("*book", 5),
                new ItemEnchantmentSlots("*shield", 3),
                new ItemEnchantmentSlots("minecraft:fishing_rod", 3),
                new ItemEnchantmentSlots("minecraft:mace", 4),
                new ItemEnchantmentSlots("minecraft:trident", 4),
                new ItemEnchantmentSlots("minecraft:bow", 4),
                new ItemEnchantmentSlots("minecraft:crossbow", 4),
                new ItemEnchantmentSlots("minecraft:wolf_armor", 4),
                new ItemEnchantmentSlots("#minecraft:harnesses", 4),
                new ItemEnchantmentSlots("*rose", 5),
                new ItemEnchantmentSlots("*remnant", 4),
                new ItemEnchantmentSlots("*end_reborn:netherite", 5),
                new ItemEnchantmentSlots("*featherzeal", 4),
                new ItemEnchantmentSlots("legacies_and_legends:reinforced_chestplate", 4),
                new ItemEnchantmentSlots("legacies_and_legends:travelling_strides", 3),
                new ItemEnchantmentSlots("legacies_and_legends:wanderer_boots", 4),
                new ItemEnchantmentSlots("legacies_and_legends:verdant_sword", 4),
                new ItemEnchantmentSlots("legacies_and_legends:frosted_spear", 4),
                new ItemEnchantmentSlots("legacies_and_legends:cleaving_battleaxe", 4),
                new ItemEnchantmentSlots("legacies_and_legends:molten_pickaxe", 5),
                new ItemEnchantmentSlots("legacies_and_legends:prospector_shovel", 3),
                new ItemEnchantmentSlots("legacies_and_legends:withered_hoe", 4),
                new ItemEnchantmentSlots("legacies_and_legends:boomerang", 5),
                new ItemEnchantmentSlots("legacies_and_legends:knife", 4),
                new ItemEnchantmentSlots("legacies_and_legends:hook", 4),
                new ItemEnchantmentSlots("remnants:katana", 4),
                new ItemEnchantmentSlots("enderscape:drift_leggings", 3),
                new ItemEnchantmentSlots("*shadoline", 4)
        );
    }

    private static boolean sameEnchantmentSlots(List<ItemEnchantmentSlots> left, List<ItemEnchantmentSlots> right) {
        if (left.size() != right.size()) return false;

        for (int i = 0; i < left.size(); i++) {
            ItemEnchantmentSlots leftEntry = left.get(i);
            ItemEnchantmentSlots rightEntry = right.get(i);
            if (leftEntry == rightEntry) continue;
            if (leftEntry == null || rightEntry == null) return false;
            if (!Objects.equals(leftEntry.key, rightEntry.key) || leftEntry.slots != rightEntry.slots) return false;
        }

        return true;
    }

    public static class ItemEnchantmentSlots {
        @ConfigEntry.Gui.Tooltip
        public String key;

        @ConfigEntry.Gui.Tooltip
        public int slots;

        public ItemEnchantmentSlots() {}

        public ItemEnchantmentSlots(String key, int slots) {
            this.key = key;
            this.slots = slots;
        }
    }

    public enum TooltipType {
        ALWAYS,
        HOLD_KEY,
        NEVER
    }

    public enum PanelVisibility {
        LEFT,
        BOTH,
        NONE
    }

    private static boolean configContainsField(Path path, String fieldName) {
        try {
            return Files.readString(path).contains("\"" + fieldName + "\"");
        } catch (Exception ignored) {
            return true;
        }
    }

    private boolean normalizeDefaults(boolean restoreDisabledEnchantments, boolean restoreEnchantmentSlots) {
        boolean changed = false;

        if (restoreDisabledEnchantments && this.disabled_enchantments.isEmpty()) {
            this.disabled_enchantments.addAll(defaultDisabledEnchantments());
            changed = true;
        }

        List<String> normalizedDisabledEnchantments = new ArrayList<>(new LinkedHashSet<>(this.disabled_enchantments));
        if (!normalizedDisabledEnchantments.equals(this.disabled_enchantments)) {
            this.disabled_enchantments = normalizedDisabledEnchantments;
            changed = true;
        }

        if (restoreEnchantmentSlots && this.item_enchantment_slots.isEmpty()) {
            this.item_enchantment_slots.addAll(defaultEnchantmentSlots());
            changed = true;
        }

        LinkedHashMap<String, Integer> normalizedEnchantmentSlotsMap = new LinkedHashMap<>();
        for (ItemEnchantmentSlots entry : this.item_enchantment_slots) {
            if (entry == null || entry.key == null || entry.key.isBlank()) continue;
            normalizedEnchantmentSlotsMap.remove(entry.key);
            normalizedEnchantmentSlotsMap.put(entry.key, entry.slots);
        }

        List<ItemEnchantmentSlots> normalizedItemEnchantmentSlots = new ArrayList<>();
        normalizedEnchantmentSlotsMap.forEach((key, slots) -> normalizedItemEnchantmentSlots.add(new ItemEnchantmentSlots(key, slots)));
        if (!sameEnchantmentSlots(normalizedItemEnchantmentSlots, this.item_enchantment_slots)) {
            this.item_enchantment_slots = normalizedItemEnchantmentSlots;
            changed = true;
        }

        return changed;
    }
}
