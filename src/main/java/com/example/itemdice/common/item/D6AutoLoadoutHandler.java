package com.example.itemdice.common.item;

import com.example.itemdice.common.config.ItemDiceConfig;
import com.example.itemdice.common.registry.ItemDiceItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "itemdice", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class D6AutoLoadoutHandler {
    private static final String LOCK_TAG = "ItemDiceD6RestoreLock";
    private static final String PLAYER_COOLDOWN_TAG = "ItemDiceD6NextAllowedGameTime";

    private D6AutoLoadoutHandler() {}

    @SubscribeEvent
    public static void onPickup(EntityItemPickupEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!ItemDiceConfig.D6_AUTO_RESTORE_ON_PICKUP.get()) return;

        ItemStack pickedStack = event.getItem().getItem();
        if (!pickedStack.is(ItemDiceItems.D6.get())) return;

        CompoundTag tag = pickedStack.getTag();
        if (tag == null || !tag.contains(InventorySnapshotManager.SNAPSHOT_TAG)) return;
        if (tag.getBoolean(LOCK_TAG)) return;

        long now = player.level().getGameTime();
        CompoundTag persistent = player.getPersistentData();
        long nextAllowed = persistent.getLong(PLAYER_COOLDOWN_TAG);
        if (now < nextAllowed) return;

        tag.putBoolean(LOCK_TAG, true);
        try {
            InventorySnapshotManager.restore(player, tag.getCompound(InventorySnapshotManager.SNAPSHOT_TAG));
            int cooldown = Math.max(0, ItemDiceConfig.ROLL_COOLDOWN_TICKS.get());
            persistent.putLong(PLAYER_COOLDOWN_TAG, now + cooldown);
        } finally {
            tag.putBoolean(LOCK_TAG, false);
        }
    }
}
