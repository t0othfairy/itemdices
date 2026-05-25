package com.example.itemdice.client;

import com.example.itemdice.common.registry.ItemDiceMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ClientSetup {
    private ClientSetup() {}

    public static void register(IEventBus bus) {
        bus.addListener(ClientSetup::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ItemDiceMenus.DICE_MENU.get(), DiceScreen::new));
    }
}
