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
  public EnchantingConfig enchanting = new EnchantingConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public LootConfig loot = new LootConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public IntegrationConfig integrations = new IntegrationConfig();

  public static class EnchantingConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean experience_rebalance = true;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean imbued_books = true;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean anvil_book_enchanting = true;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=1L)
    public double anvil_break_chance = 0.12;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean enchantable_books = false;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=1L)
    public double first_book_chance = 0.5;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=1L)
    public double subsequent_book_chance = 0.1;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=6L)
    public float books_for_enchanting_power = 6F;
  }

  public static class LootConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=10L)
    public int world_enchantment_limit = 3;
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
