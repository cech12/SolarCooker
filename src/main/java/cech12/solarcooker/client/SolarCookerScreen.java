package cech12.solarcooker.client;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.inventory.SolarCookerContainer;
//import com.mojang.blaze3d.matrix.MatrixStack; //1.16
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//import javax.annotation.Nonnull; //1.16

@OnlyIn(Dist.CLIENT)
public class SolarCookerScreen extends ContainerScreen<SolarCookerContainer> {
    private static final ResourceLocation guiTexture = new ResourceLocation(SolarCookerMod.MOD_ID, "textures/gui/container/solar_cooker.png");

    public SolarCookerScreen(SolarCookerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        //this.field_238742_p_ = (this.xSize - this.font.func_238414_a_(this.title)) / 2; //1.16
    }

    /**
     * 1.15
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    public void render(int p_230430_2_, int p_230430_3_, float p_230430_4_) { //1.15
        this.renderBackground(); //1.15
        super.render( p_230430_2_, p_230430_3_, p_230430_4_); //1.15
        this.renderHoveredToolTip(p_230430_2_, p_230430_3_); //1.15
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_230450_2_, int p_230450_3_, int p_230450_4_) { //1.15
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(guiTexture);
            //draw gui
            int left = this.guiLeft;
            int top = this.guiTop;
            this.blit(left, top, 0, 0, this.xSize, this.ySize); //1.15
            //draw flame
            if (this.container.isBurning()) {
                this.blit(left + 56, top + 36, 176, 0, 14, 14); //1.15
            }
            //draw progress
            int progress = this.container.getCookProgressionScaled();
            this.blit(left + 79, top + 34, 176, 14, progress + 1, 16); //1.15
            //draw sun
            if (this.container.isSunlit()) {
                this.blit(left + 55, top + 52, 176, 31, 18, 18); //1.15
            }
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
    }
}
