package com.example.itemdice.common.registry;

import com.example.itemdice.ItemDiceMod;
import com.example.itemdice.common.item.DiceItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemDiceItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ItemDiceMod.MOD_ID);

    public static final RegistryObject<Item> D4 = registerDice("d4", 16);
    public static final RegistryObject<Item> D6 = registerDice("d6", 27);
    public static final RegistryObject<Item> D20 = registerDice("d20", 54);
    public static final RegistryObject<Item> D100 = registerDice("d100", 108);

    private ItemDiceItems() {}

    private static RegistryObject<Item> registerDice(String id, int slots) {
        return ITEMS.register(id, () -> new DiceItem(new Item.Properties().stacksTo(1), slots));
    }
}
