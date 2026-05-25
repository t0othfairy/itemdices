package com.example.itemdice.common.item;

import com.example.itemdice.common.capability.DiceInventoryProvider;
import com.example.itemdice.common.menu.DiceMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiceItem extends Item {
    private final int slotCount;

    public DiceItem(Properties properties, int slotCount) {
        super(properties);
        this.slotCount = slotCount;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
        return new DiceInventoryProvider(slotCount);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                NetworkHooks.openScreen(serverPlayer, new DiceMenuProvider(hand), buf -> {
                    buf.writeEnum(hand);
                    buf.writeInt(slotCount);
                });
            } else if ("d4".equals(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(this).getPath())) {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains(InventorySnapshotManager.SNAPSHOT_TAG)) {
                    InventorySnapshotManager.restore(serverPlayer, tag.getCompound(InventorySnapshotManager.SNAPSHOT_TAG));
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            lines.add(Component.translatable("tooltip.itemdice.dice.shift").withStyle(ChatFormatting.AQUA));
            lines.add(Component.translatable("tooltip.itemdice.dice.slot_count", slotCount).withStyle(ChatFormatting.GRAY));
        } else {
            lines.add(Component.translatable("tooltip.itemdice.hold_shift").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static class DiceMenuProvider implements MenuProvider {
        private final InteractionHand hand;

        private DiceMenuProvider(InteractionHand hand) { this.hand = hand; }

        @Override
        public Component getDisplayName() { return Component.translatable("menu.itemdice.dice"); }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
            return DiceMenu.server(id, inventory, hand);
        }
    }
}
