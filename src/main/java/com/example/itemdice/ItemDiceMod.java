package com.example.itemdice;

import com.example.itemdice.client.ClientSetup;
import com.example.itemdice.common.config.ItemDiceConfig;
import com.example.itemdice.common.network.ItemDiceNetwork;
import com.example.itemdice.common.registry.ItemDiceCreativeTabs;
import com.example.itemdice.common.registry.ItemDiceItems;
import com.example.itemdice.common.registry.ItemDiceMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ItemDiceMod.MOD_ID)
public class ItemDiceMod {
    public static final String MOD_ID = "itemdice";

    public ItemDiceMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemDiceItems.ITEMS.register(modEventBus);
        ItemDiceMenus.MENUS.register(modEventBus);
        ItemDiceCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ItemDiceConfig.register();
        ItemDiceNetwork.register();

        if (ModList.get().isLoaded("curios")) {
            com.example.itemdice.common.curios.CuriosIntegration.register(modEventBus);
        }

        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironmentHolder.isClient()) {
            ClientSetup.register(modEventBus);
        }
    }

    private static final class FMLEnvironmentHolder {
        private static boolean isClient() {
            return net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT;
        }
    }
}
