package com.example.itemdice.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class ItemDiceConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec.IntValue ROLL_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.BooleanValue D6_AUTO_RESTORE_ON_PICKUP;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("dice");
        ROLL_COOLDOWN_TICKS = b.comment("Server cooldown between dice utility actions.")
            .defineInRange("rollCooldownTicks", 20, 0, 2000);
        b.pop();

        b.push("d6");
        D6_AUTO_RESTORE_ON_PICKUP = b.comment("Automatically restore d6 snapshot when d6 is picked up.")
            .define("autoRestoreOnPickup", true);
        b.pop();

        SERVER_SPEC = b.build();
    }

    private ItemDiceConfig() {}

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
}
