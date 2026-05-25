package com.example.itemdice.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public final class InventorySnapshotManager {
    public static final String SNAPSHOT_TAG = "InventorySnapshot";

    private InventorySnapshotManager() {}

    public static CompoundTag capture(ServerPlayer player) {
        CompoundTag out = new CompoundTag();
        ListTag main = new ListTag();
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            CompoundTag e = new CompoundTag();
            e.putInt("Slot", i);
            e.put("Stack", player.getInventory().items.get(i).save(new CompoundTag()));
            main.add(e);
        }
        out.put("Main", main);

        ListTag armor = new ListTag();
        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            CompoundTag e = new CompoundTag();
            e.putInt("Slot", i);
            e.put("Stack", player.getInventory().armor.get(i).save(new CompoundTag()));
            armor.add(e);
        }
        out.put("Armor", armor);

        ListTag offhand = new ListTag();
        for (int i = 0; i < player.getInventory().offhand.size(); i++) {
            CompoundTag e = new CompoundTag();
            e.putInt("Slot", i);
            e.put("Stack", player.getInventory().offhand.get(i).save(new CompoundTag()));
            offhand.add(e);
        }
        out.put("Offhand", offhand);
        CuriosCompat.captureCurios(player, out);
        return out;
    }

    public static void restore(ServerPlayer player, CompoundTag snapshot) {
        restoreList(player, snapshot.getList("Main", Tag.TAG_COMPOUND), player.getInventory().items);
        restoreList(player, snapshot.getList("Armor", Tag.TAG_COMPOUND), player.getInventory().armor);
        restoreList(player, snapshot.getList("Offhand", Tag.TAG_COMPOUND), player.getInventory().offhand);
        CuriosCompat.restoreCurios(player, snapshot);
        player.containerMenu.broadcastChanges();
    }

    private static void restoreList(ServerPlayer player, ListTag list, List<ItemStack> target) {
        for (Tag t : list) {
            CompoundTag e = (CompoundTag) t;
            int slot = e.getInt("Slot");
            if (slot < 0 || slot >= target.size()) continue;
            ItemStack desired = ItemStack.of(e.getCompound("Stack"));
            ItemStack existing = target.get(slot);
            if (existing.isEmpty()) {
                target.set(slot, desired);
            } else if (!desired.isEmpty()) {
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(player.getInventory(), desired.copy(), false);
                if (!remainder.isEmpty()) player.drop(remainder, false);
            }
        }
    }

}
