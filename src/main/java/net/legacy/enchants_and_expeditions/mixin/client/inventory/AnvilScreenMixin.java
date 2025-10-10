package net.legacy.enchants_and_expeditions.mixin.client.inventory;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin {
    public AnvilScreenMixin() {
    }

    @ModifyConstant(method = "renderLabels", constant = @Constant(intValue = 40))
    private int EaE$hideTooExpensive(int constant) {
        return Integer.MAX_VALUE;
    }
}