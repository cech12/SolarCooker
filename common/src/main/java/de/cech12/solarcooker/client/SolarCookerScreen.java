package de.cech12.solarcooker.client;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class SolarCookerScreen extends AbstractContainerScreen<SolarCookerContainer> {
    private static final ResourceLocation guiTexture = Constants.id("textures/gui/container/solar_cooker.png");

    public SolarCookerScreen(SolarCookerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            //draw gui
            int left = this.leftPos;
            int top = this.topPos;
            guiGraphics.blit(guiTexture, left, top, 0, 0, this.imageWidth, this.imageHeight);
            //draw flame
            if (this.menu.isBurning()) {
                guiGraphics.blit(guiTexture, left + 56, top + 36, 176, 0, 14, 14);
            }
            //draw progress
            int progress = this.menu.getCookProgressionScaled();
            guiGraphics.blit(guiTexture, left + 79, top + 34, 176, 14, progress + 1, 16);
            //draw sun
            if (this.menu.isSunlit()) {
                guiGraphics.blit(guiTexture, left + 55, top + 52, 176, 31, 18, 18);
            }
        }
    }
}
