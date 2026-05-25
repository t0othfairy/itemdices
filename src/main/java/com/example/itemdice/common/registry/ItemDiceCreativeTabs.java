package com.example.itemdice.common.registry;

import com.example.itemdice.ItemDiceMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.Registries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemDiceCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ItemDiceMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.itemdice.main"))
        .icon(() -> new ItemStack(ItemDiceItems.D20.get()))
        .displayItems((params, output) -> {
            output.accept(ItemDiceItems.D4.get());
            output.accept(ItemDiceItems.D6.get());
            output.accept(ItemDiceItems.D20.get());
            output.accept(ItemDiceItems.D100.get());
        }).build());
    private ItemDiceCreativeTabs() {}
}
