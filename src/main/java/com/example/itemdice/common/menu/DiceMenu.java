package com.example.itemdice.common.menu;

import com.example.itemdice.common.item.InventorySnapshotManager;
import com.example.itemdice.common.registry.ItemDiceMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
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
    private final boolean snapshotMode;

    protected DiceMenu(int id, Inventory playerInv, InteractionHand hand, int slotCount) {
        super(ItemDiceMenus.DICE_MENU.get(), id);
        this.hand = hand;
        this.snapshotMode = isD4(playerInv.player);

        if (snapshotMode) {
            addSnapshotLayout(playerInv);
        } else {
            playerInv.player.getItemInHand(hand).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> addDiceSlots(handler, slotCount));
            bindPlayerInventory(playerInv, 140);
        }
    }

    public static DiceMenu server(int id, Inventory inv, InteractionHand hand) {
        int slots = inv.player.getItemInHand(hand).getCapability(ForgeCapabilities.ITEM_HANDLER).map(IItemHandler::getSlots).orElse(0);
        return new DiceMenu(id, inv, hand, slots);
    }

    public static DiceMenu fromNetwork(int id, Inventory inv, FriendlyByteBuf buf) {
        return new DiceMenu(id, inv, buf.readEnum(InteractionHand.class), buf.readInt());
    }

    private boolean isD4(Player player) {
        return "d4".equals(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(player.getItemInHand(hand).getItem()).getPath());
    }

    private void addSnapshotLayout(Inventory inv) {
        // Armor slots (helmet->boots)
        this.addSlot(new Slot(inv, 39, 8, 18) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) { return stack.canEquip(EquipmentSlot.HEAD, inv.player); }
        });
        this.addSlot(new Slot(inv, 38, 8, 36) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) { return stack.canEquip(EquipmentSlot.CHEST, inv.player); }
        });
        this.addSlot(new Slot(inv, 37, 8, 54) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) { return stack.canEquip(EquipmentSlot.LEGS, inv.player); }
        });
        this.addSlot(new Slot(inv, 36, 8, 72) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) { return stack.canEquip(EquipmentSlot.FEET, inv.player); }
        });

        // Main inventory (3x9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 30 + col * 18, 18 + row * 18));
            }
        }

        // Hotbar (0..8)
        for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col, 30 + col * 18, 76));

        // Offhand
        this.addSlot(new Slot(inv, 40, 192, 76));

        // Curios + other modded slots are rendered by their own menus; snapshot supports saving/restoring through compat hooks.
    }

    private void addDiceSlots(IItemHandler handler, int slotCount) {
        int columns = Math.max(1, Math.min(9, (int) Math.ceil(Math.sqrt(slotCount))));
        for (int i = 0; i < slotCount; i++) {
            int x = 8 + (i % columns) * 18;
            int y = 18 + (i / columns) * 18;
            this.addSlot(new SlotItemHandler(handler, i, x, y));
        }
    }

    private void bindPlayerInventory(Inventory inv, int yOffset) {
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, yOffset + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col, 8 + col * 18, yOffset + 58));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide && snapshotMode && player instanceof net.minecraft.server.level.ServerPlayer sp) {
            CompoundTag snapshot = InventorySnapshotManager.capture(sp);
            player.getItemInHand(hand).getOrCreateTag().put(InventorySnapshotManager.SNAPSHOT_TAG, snapshot);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.getItemInHand(hand).isEmpty();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
}
