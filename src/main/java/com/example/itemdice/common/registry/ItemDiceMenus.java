package com.example.itemdice.common.registry;

import com.example.itemdice.ItemDiceMod;
import com.example.itemdice.common.menu.DiceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemDiceMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ItemDiceMod.MOD_ID);
    public static final RegistryObject<MenuType<DiceMenu>> DICE_MENU = MENUS.register("dice_menu", () -> IForgeMenuType.create(DiceMenu::fromNetwork));
    private ItemDiceMenus() {}
}
