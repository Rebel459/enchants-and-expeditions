package net.rebel459.enchants_and_expeditions.mixin;

import net.rebel459.enchants_and_expeditions.config.EaEConfig;
import net.rebel459.unified.platform.UnifiedPlatform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class EaEMixinPlugin implements IMixinConfigPlugin {

    private static boolean registeredConfig = false;

    private boolean hasCombatReborn;

    @Override
    public void onLoad(String mixinPackage) {
        if (!registeredConfig) {
            EaEConfig.initClient();
            registeredConfig = true;
        }
        this.hasCombatReborn = UnifiedPlatform.get().isModLoaded("combat_reborn") && EaEConfig.get.integrations.combat_reborn;
    }

    @Override
    @Nullable
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, @NotNull String mixinClassName) {

        if (mixinClassName.contains("integration.combat_reborn.")) return this.hasCombatReborn;

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    @Nullable
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}