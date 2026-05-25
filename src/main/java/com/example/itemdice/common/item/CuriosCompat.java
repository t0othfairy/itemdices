package com.example.itemdice.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;

public final class CuriosCompat {
    private CuriosCompat() {}

    public static void captureCurios(ServerPlayer player, CompoundTag snapshot) {
        if (!ModList.get().isLoaded("curios")) return;
        top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player).ifPresent(inv -> snapshot.put("Curios", inv.serializeNBT()));
    }

    public static void restoreCurios(ServerPlayer player, CompoundTag snapshot) {
        if (!ModList.get().isLoaded("curios") || !snapshot.contains("Curios")) return;
        top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player).ifPresent(inv -> inv.deserializeNBT(snapshot.getCompound("Curios")));
    }
}
