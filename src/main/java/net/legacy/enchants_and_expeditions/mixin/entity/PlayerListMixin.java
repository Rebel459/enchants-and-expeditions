package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.lib.EnchantingHelper;
import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Unique
    private NonNullList<ItemStack> savedItems;
    @Unique
    private Map<EquipmentSlot, ItemStack> savedEquipment;

    @Inject(method = "respawn", at = @At("HEAD"))
    private void EaE$saveItems(ServerPlayer player, boolean keepInventory, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        Inventory inventory = player.getInventory();
        // Save main inventory
        savedItems = NonNullList.withSize(inventory.items.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.items.size(); i++) {
            ItemStack itemStack = inventory.items.get(i);
            if (shouldReserve(itemStack, player, keepInventory)) {
                savedItems.set(i, itemStack.copy());
            }
        }
        // Save equipment (armor and offhand)
        savedEquipment = new HashMap<>();
        inventory.equipment.items.forEach((slot, itemStack) -> {
            if (shouldReserve(itemStack, player, keepInventory)) {
                savedEquipment.put(slot, itemStack.copy());
            }
        });
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    private void EaE$restoreItems(ServerPlayer player, boolean keepInventory, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer newPlayer = cir.getReturnValue();
        Inventory newInventory = newPlayer.getInventory();
        // Restore main inventory
        for (int i = 0; i < savedItems.size() && i < newInventory.items.size(); i++) {
            newInventory.items.set(i, savedItems.get(i).copy());
        }
        // Restore equipment (armor and offhand)
        savedEquipment.forEach((slot, itemStack) -> newInventory.equipment.set(slot, itemStack.copy()));
        // Clear saved data
        savedItems = null;
        savedEquipment = null;
    }

    @Unique
    private boolean shouldReserve(ItemStack stack, ServerPlayer player, boolean keepInventory) {
        if (stack.isEmpty()) return false;
        return player.server.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || keepInventory || (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BOUNDING_BLESSING) && !stack.is(EaEItemTags.UNBOUNDABLE));
    }
}