package com.example.itemdice.client;

import com.example.itemdice.common.menu.DiceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DiceScreen extends AbstractContainerScreen<DiceMenu> {
    private static final ResourceLocation BG = new ResourceLocation("itemdice", "textures/gui/dice.png");

    public DiceScreen(DiceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
