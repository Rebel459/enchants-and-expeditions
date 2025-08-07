package net.legacy.enchants_and_expeditions.config;

import com.mojang.datafixers.util.Function3;
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
  public ChiseledConfig chiseled_bookshelves = new ChiseledConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public MiscConfig misc = new MiscConfig();

  @ConfigEntry.Gui.CollapsibleObject
  public IntegrationConfig integrations = new IntegrationConfig();

  public static class EnchantingConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean experience_rebalance = true;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public boolean allow_book_enchanting = false;
  }

  public static class ChiseledConfig {
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    public FormulaType enchantment_chance_formula = FormulaType.EXPONENTIAL;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=1L)
    public float first_book_chance = 0.4F;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=1L)
    public float tenth_book_chance = 0.01F;
    @ConfigEntry.Category("config")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(max=6L)
    public float books_for_enchanting_power = 6F;
  }

  public static class MiscConfig {
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

  public enum FormulaType {
    LINEAR((zero, ten, i) -> zero + (ten - zero) * (i / 10.0f)),
    EXPONENTIAL((zero, ten, i) -> zero * (float) Math.pow(ten / zero, i / 10.0f)),
    QUADRATIC((zero, ten, i) -> zero + (ten - zero) * (float) Math.pow(i / 10.0f, 2));

    private final Function3<Float, Float, Integer, Float> function;

    FormulaType(Function3<Float, Float, Integer, Float> function) {
      this.function = function;
    }

    public float getFormula(float zero, float ten, int index) {
      return Math.min(this.function.apply(zero, ten, index), 1.0f);
    }
  }
}
