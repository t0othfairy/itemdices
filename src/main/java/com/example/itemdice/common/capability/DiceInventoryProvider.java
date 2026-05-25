package com.example.itemdice.common.capability;

import com.example.itemdice.common.inventory.DiceInventory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiceInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    private final DiceInventory inventory;
    private final LazyOptional<IItemHandler> handler;

    public DiceInventoryProvider(int slots) {
        this.inventory = new DiceInventory(slots);
        this.handler = LazyOptional.of(() -> inventory);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? handler.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() { return inventory.saveToTag(); }

    @Override
    public void deserializeNBT(CompoundTag nbt) { inventory.loadFromTag(nbt); }

    public DiceInventory inventory() { return inventory; }
}
