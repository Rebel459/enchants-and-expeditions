package net.legacy.enchants_and_expeditions.util;

import net.legacy.enchants_and_expeditions.tag.EaEEnchantmentTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class EnchantingAttributes {
    private int mana;
    private int frost;
    private int scorch;
    private int flow;
    private int chaos;
    private int greed;
    private int might;
    private int stability;
    private int divinity;

    public EnchantingAttributes() {
        this.mana = 0;
        this.frost = 0;
        this.scorch = 0;
        this.flow = 0;
        this.chaos = 0;
        this.greed = 0;
        this.might = 0;
        this.stability = 10;
        this.divinity = 0;
    }

    public int getMana() { return this.mana; }
    public void setMana(int amount) { this.mana = amount; }
    public int getFrost() { return this.frost; }
    public void setFrost(int amount) { this.frost = amount; }
    public int getScorch() { return this.scorch; }
    public void setScorch(int amount) { this.scorch = amount; }
    public int getFlow() { return this.flow; }
    public void setFlow(int amount) { this.flow = amount; }
    public int getChaos() { return this.chaos; }
    public void setChaos(int amount) { this.chaos = amount; }
    public int getGreed() { return this.greed; }
    public void setGreed(int amount) { this.greed = amount; }
    public int getMight() { return this.might; }
    public void setMight(int amount) { this.might = amount; }
    public int getStability() { return this.stability; }
    public void setStability(int amount) { this.stability = amount; }
    public int getDivinity() { return this.divinity; }
    public void setDivinity(int amount) { this.divinity = amount; }
}
