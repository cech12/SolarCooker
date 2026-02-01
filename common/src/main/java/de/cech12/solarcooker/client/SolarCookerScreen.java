package de.cech12.solarcooker.client;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolarCookerScreen extends AbstractContainerScreen<SolarCookerContainer> {
    private static final Identifier guiTexture = Constants.id("textures/gui/container/solar_cooker.png");
    private static final Identifier litSprite = Constants.id("container/solarcooker/lit");
    private static final Identifier burnProgressSprite = Constants.id("container/solarcooker/burn_progress");
    private static final Identifier sunlightSprite = Constants.id("container/solarcooker/sunlight");

    public SolarCookerScreen(SolarCookerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            //draw gui
            int left = this.leftPos;
            int top = this.topPos;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, guiTexture, left, top, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
            //draw flame
            if (this.menu.isBurning()) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, litSprite, 14, 14, 0, 0, left + 56, top + 36, 14, 14);
            }
            //draw progress
            int progress = this.menu.getCookProgressionScaled();
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, burnProgressSprite, 24, 16, 0, 0, left + 79, top + 34, progress, 16);
            //draw sun
            if (this.menu.isSunlit()) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sunlightSprite, 18, 18, 0, 0, left + 55, top + 52, 18, 18);
            }
        }
    }
}
