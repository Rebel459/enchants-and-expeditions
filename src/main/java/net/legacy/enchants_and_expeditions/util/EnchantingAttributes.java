package net.legacy.enchants_and_expeditions.util;

public interface EnchantingAttributes {
    record Attributes(int mana, int frost, int scorch, int flow, int chaos, int greed, int might, int stability, int divinity) {}
    Attributes calculateAttributes();
}