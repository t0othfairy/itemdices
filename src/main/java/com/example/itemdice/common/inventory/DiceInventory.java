package com.example.itemdice.common.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class DiceInventory extends ItemStackHandler {
    public DiceInventory(int size) { super(size); }

    @Override
    public int getSlotLimit(int slot) { return 64; }

    public CompoundTag saveToTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("Items", serializeNBT().getList("Items", Tag.TAG_COMPOUND));
        tag.putInt("Size", getSlots());
        return tag;
    }

    public void loadFromTag(CompoundTag tag) {
        if (tag.contains("Items", Tag.TAG_LIST)) {
            ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
            CompoundTag nbt = new CompoundTag();
            nbt.put("Items", items);
            deserializeNBT(nbt);
        }
    }

    public boolean isValidInsert(int slot, ItemStack stack) {
        return !stack.isEmpty() && isItemValid(slot, stack);
    }
}
