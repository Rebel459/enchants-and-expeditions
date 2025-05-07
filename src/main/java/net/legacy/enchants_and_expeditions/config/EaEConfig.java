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

  @ConfigEntry.Category("default")
  public boolean loot_table_injects = true;

  @ConfigEntry.Category("default")
  public boolean legacies_and_legends_integration = true;

  @ConfigEntry.Category("default")
  public boolean progression_reborn_integration = true;

  @ConfigEntry.Category("default")
  public boolean enderscape_integration = true;

}
