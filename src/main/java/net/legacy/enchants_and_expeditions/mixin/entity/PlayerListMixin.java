package net.legacy.enchants_and_expeditions.mixin.entity;

import net.legacy.enchants_and_expeditions.registry.EaEEnchantments;
import net.legacy.enchants_and_expeditions.tag.EaEItemTags;
import net.legacy.enchants_and_expeditions.util.EnchantingHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Unique
    private NonNullList<ItemStack> savedItems;

    @Unique
    private NonNullList<ItemStack> savedArmor;

    @Unique
    private NonNullList<ItemStack> savedOffhand;

    @Inject(method = "respawn", at = @At("HEAD"))
    private void EaE$saveItems(ServerPlayer player, boolean keepInventory, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        Inventory inventory = player.getInventory();
        // Save inventory
        savedItems = NonNullList.withSize(inventory.items.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.items.size(); i++) {
            ItemStack itemStack = inventory.items.get(i);
            if (shouldReserve(itemStack, player, keepInventory)) {
                savedItems.set(i, itemStack.copy());
            }
        }
        // Save armor
        savedArmor = NonNullList.withSize(inventory.armor.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.armor.size(); i++) {
            ItemStack itemStack = inventory.armor.get(i);
            if (shouldReserve(itemStack, player, keepInventory)) {
                savedArmor.set(i, itemStack.copy());
            }
        }
        // Save offhand
        savedOffhand = NonNullList.withSize(inventory.offhand.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.offhand.size(); i++) {
            ItemStack itemStack = inventory.offhand.get(i);
            if (shouldReserve(itemStack, player, keepInventory)) {
                savedOffhand.set(i, itemStack.copy());
            }
        }
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    private void EaE$restoreItems(ServerPlayer player, boolean keepInventory, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer newPlayer = cir.getReturnValue();
        Inventory newInventory = newPlayer.getInventory();
        // Restore inventory
        for (int i = 0; i < savedItems.size() && i < newInventory.items.size(); i++) {
            newInventory.items.set(i, savedItems.get(i).copy());
        }
        // Restore armor
        for (int i = 0; i < savedArmor.size() && i < newInventory.armor.size(); i++) {
            newInventory.armor.set(i, savedArmor.get(i).copy());
        }
        // Restore offhand
        for (int i = 0; i < savedOffhand.size() && i < newInventory.offhand.size(); i++) {
            newInventory.offhand.set(i, savedOffhand.get(i).copy());
        }
        savedItems = null;
        savedArmor = null;
        savedOffhand = null;
    }

    @Unique
    private boolean shouldReserve(ItemStack stack, ServerPlayer player, boolean keepInventory) {
        if (stack.isEmpty()) return false;
        return player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || keepInventory || (EnchantingHelper.hasEnchantment(stack, EaEEnchantments.BOUNDING_BLESSING) && !stack.is(EaEItemTags.UNBOUNDABLE));
    }
}