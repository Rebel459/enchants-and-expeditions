package net.rebel459.enchants_and_expeditions.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.rebel459.enchants_and_expeditions.EnchantsAndExpeditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Config(name = EnchantsAndExpeditions.MOD_ID)
public class EaEConfig implements ConfigData {

    @Contract(pure = true)
    public static @NotNull Path configPath(boolean json5) {
        return Path.of("./config/" + EnchantsAndExpeditions.MOD_ID + "." + (json5 ? "json5" : "json"));
    }

    public static EaEConfig get() {
        return AutoConfig.getConfigHolder(EaEConfig.class).getConfig();
    };

    public static void initClient() {
        AutoConfig.register(EaEConfig.class, JanksonConfigSerializer::new);
    }

    @ConfigEntry.Gui.CollapsibleObject
    public GeneralConfig general = new GeneralConfig();

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
        public boolean experience_rebalance = true;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(max=1L)
        public double anvil_break_chance = 0.12;
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean craftable_experience_bottles = true;
    }

    public static class MiscConfig {
        @ConfigEntry.Category("config")
        @ConfigEntry.Gui.Tooltip
        public boolean ordered_enchantment_tooltips = true;
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
    @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Notice disable_enchantments = Notice.INFO;

    @ConfigEntry.Gui.Excluded
    @Comment("A map to specify custom enchantment slot values. Accepts both item ids (`minecraft:iron_sword`) and item tags (`#minecraft:swords`)\nAnything not specified here falls back to the formula: `enchantability / 4 (min 3, max 5)`")
    public Map<String, Integer> enchantment_slots = new HashMap<>(
            Map.of()
    );

    public enum Notice {
        INFO
    }
}
