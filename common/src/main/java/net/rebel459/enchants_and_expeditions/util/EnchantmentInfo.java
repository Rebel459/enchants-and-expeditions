package net.rebel459.enchants_and_expeditions.util;

public record EnchantmentInfo(int blessings, int powerfulEnchantments, int standardEnchantments, int curses, int slotsUsed) {
    public static EnchantmentInfo EMPTY = new EnchantmentInfo(0, 0, 0, 0, 0);
}