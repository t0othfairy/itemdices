package com.example.itemdice.common.network;

import com.example.itemdice.ItemDiceMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ItemDiceNetwork {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(ItemDiceMod.MOD_ID, "main"),
        () -> VERSION,
        VERSION::equals,
        VERSION::equals
    );

    private ItemDiceNetwork() {}

    public static void register() {
        // Register gameplay packets here as features are expanded.
    }
}
