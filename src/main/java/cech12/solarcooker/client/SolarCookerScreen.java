package cech12.solarcooker.client;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.inventory.SolarCookerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SolarCookerScreen extends ContainerScreen<SolarCookerContainer> {
    private static final ResourceLocation guiTexture = new ResourceLocation(SolarCookerMod.MOD_ID, "textures/gui/container/solar_cooker.png");

    public SolarCookerScreen(SolarCookerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.titleX = (this.xSize - this.font.func_238414_a_(this.title)) / 2;
    }

    @Override
    public void render(@Nonnull MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack p_230450_1_, float partialTicks, int x, int y) {
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(guiTexture);
            //draw gui
            int left = this.guiLeft;
            int top = this.guiTop;
            this.blit(p_230450_1_, left, top, 0, 0, this.xSize, this.ySize);
            //draw flame
            if (this.container.isBurning()) {
                this.blit(p_230450_1_, left + 56, top + 36, 176, 0, 14, 14);
            }
            //draw progress
            int progress = this.container.getCookProgressionScaled();
            this.blit(p_230450_1_, left + 79, top + 34, 176, 14, progress + 1, 16);
            //draw sun
            if (this.container.isSunlit()) {
                this.blit(p_230450_1_, left + 55, top + 52, 176, 31, 18, 18);
            }
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
    }
}
