package cech12.solarcooker.client;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.inventory.SolarCookerContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SolarCookerScreen extends AbstractContainerScreen<SolarCookerContainer> {
    private static final ResourceLocation guiTexture = new ResourceLocation(SolarCookerMod.MOD_ID, "textures/gui/container/solar_cooker.png");

    public SolarCookerScreen(SolarCookerContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(@Nonnull PoseStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack p_230450_1_, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, guiTexture);
            //draw gui
            int left = this.leftPos;
            int top = this.topPos;
            this.blit(p_230450_1_, left, top, 0, 0, this.imageWidth, this.imageHeight);
            //draw flame
            if (this.menu.isBurning()) {
                this.blit(p_230450_1_, left + 56, top + 36, 176, 0, 14, 14);
            }
            //draw progress
            int progress = this.menu.getCookProgressionScaled();
            this.blit(p_230450_1_, left + 79, top + 34, 176, 14, progress + 1, 16);
            //draw sun
            if (this.menu.isSunlit()) {
                this.blit(p_230450_1_, left + 55, top + 52, 176, 31, 18, 18);
            }
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.imageWidth) || mouseY >= (double)(guiTopIn + this.imageHeight);
    }
}
