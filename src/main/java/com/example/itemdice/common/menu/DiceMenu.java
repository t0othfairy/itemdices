package com.example.itemdice.common.menu;

import com.example.itemdice.common.item.InventorySnapshotManager;
import com.example.itemdice.common.registry.ItemDiceMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DiceMenu extends AbstractContainerMenu {
    private final InteractionHand hand;

    protected DiceMenu(int id, Inventory playerInv, InteractionHand hand, int slotCount) {
        super(ItemDiceMenus.DICE_MENU.get(), id);
        this.hand = hand;
        playerInv.player.getItemInHand(hand).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> addDiceSlots(handler, slotCount));
        bindPlayerInventory(playerInv);
    }

    public static DiceMenu server(int id, Inventory inv, InteractionHand hand) {
        int slots = inv.player.getItemInHand(hand).getCapability(ForgeCapabilities.ITEM_HANDLER).map(IItemHandler::getSlots).orElse(0);
        return new DiceMenu(id, inv, hand, slots);
    }

    public static DiceMenu fromNetwork(int id, Inventory inv, FriendlyByteBuf buf) {
        return new DiceMenu(id, inv, buf.readEnum(InteractionHand.class), buf.readInt());
    }

    private void addDiceSlots(IItemHandler handler, int slotCount) {
        int columns = Math.max(1, Math.min(9, (int) Math.ceil(Math.sqrt(slotCount))));
        for (int i = 0; i < slotCount; i++) {
            int x = 8 + (i % columns) * 18;
            int y = 18 + (i / columns) * 18;
            this.addSlot(new SlotItemHandler(handler, i, x, y));
        }
    }

    private void bindPlayerInventory(Inventory inv) {
        int yOffset = 140;
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, yOffset + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col, 8 + col * 18, yOffset + 58));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide && "d4".equals(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem()).getPath()) && player instanceof net.minecraft.server.level.ServerPlayer sp) {
            CompoundTag snapshot = InventorySnapshotManager.capture(sp);
            player.getItemInHand(hand).getOrCreateTag().put(InventorySnapshotManager.SNAPSHOT_TAG, snapshot);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(hand).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
}
