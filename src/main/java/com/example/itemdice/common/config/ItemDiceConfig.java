package com.example.itemdice.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ItemDiceConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec.IntValue ROLL_COOLDOWN_TICKS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("dice");
        ROLL_COOLDOWN_TICKS = b.comment("Server cooldown between dice utility actions.")
            .defineInRange("rollCooldownTicks", 20, 0, 2000);
        b.pop();
        SERVER_SPEC = b.build();
    }

    private ItemDiceConfig() {}

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
}
