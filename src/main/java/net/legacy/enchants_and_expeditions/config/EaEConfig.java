package net.legacy.enchants_and_expeditions.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.legacy.enchants_and_expeditions.EnchantsAndExpeditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Config(name = EnchantsAndExpeditions.MOD_ID)
public class EaEConfig implements ConfigData {

  @Contract(pure = true)
  public static @NotNull Path configPath(boolean json5) {
    return Path.of("./config/" + EnchantsAndExpeditions.MOD_ID + "." + (json5 ? "json5" : "json"));
  }

  public static EaEConfig get;

  public static void initClient() {
    AutoConfig.register(EaEConfig.class, JanksonConfigSerializer::new);
    get = AutoConfig.getConfigHolder(EaEConfig.class).getConfig();
  }

  @ConfigEntry.Gui.CollapsibleObject
  public GeneralConfig general = new GeneralConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public EnchantmentConfig enchantments = new EnchantmentConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public LootConfig loot = new LootConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public IntegrationConfig integrations = new IntegrationConfig();

  public static class GeneralConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=10L)
    public int enchantment_limit = 3;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean table_multi_enchanting = true;
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

  public static class EnchantmentConfig {
    @ConfigEntry.Category("config")
    public boolean mending_blessing = true;
    @ConfigEntry.Category("config")
    public boolean infinity_blessing = true;
    @ConfigEntry.Category("config")
    public boolean channeling_blessing = true;
    @ConfigEntry.Category("config")
    public boolean bloodlust = true;
    @ConfigEntry.Category("config")
    public boolean extraction = true;
    @ConfigEntry.Category("config")
    public boolean fragility_curse = true;
  }

  public static class LootConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean enchant_function_fallback = true;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean loot_table_injects = true;
  }

  public static class IntegrationConfig {
    @ConfigEntry.Category("config")
    public boolean legacies_and_legends_integration = true;

    @ConfigEntry.Category("config")
    public boolean progression_reborn_integration = true;

    @ConfigEntry.Category("config")
    public boolean trailier_tales_integration = true;

    @ConfigEntry.Category("config")
    public boolean enderscape_integration = true;
  }
}
