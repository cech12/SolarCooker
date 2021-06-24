package cech12.solarcooker.client;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.block.AbstractSolarCookerBlock;
import cech12.solarcooker.tileentity.SolarCookerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SolarCookerTileEntityRenderer extends TileEntityRenderer<SolarCookerTileEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(SolarCookerMod.MOD_ID, "textures/entity/solar_cooker.png");

    private final ModelRenderer singleLid;
    private final ModelRenderer singleBottom;
    private final ModelRenderer singleInner;
    private final ModelRenderer singleLatch;

    public SolarCookerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.singleBottom = new ModelRenderer(64, 64, 0, 19);
        this.singleBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
        this.singleLid = new ModelRenderer(64, 64, 0, 0);
        this.singleLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.singleLid.y = 9.0F;
        this.singleLid.z = 1.0F;
        this.singleInner = new ModelRenderer(64, 64, 0, 43);
        this.singleInner.addBox(3.0F, 2.0F, 3.0F, 10.0F, 8.0F, 10.0F, 0.0F);
        this.singleLatch = new ModelRenderer(64, 64, 0, 0);
        this.singleLatch.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.singleLatch.y = 8.0F;
    }

    @Override
    public void render(SolarCookerTileEntity tileEntityIn, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntityIn.getLevel();
        boolean isInWorld = world != null;
        BlockState blockstate = isInWorld ? tileEntityIn.getBlockState() : SolarCookerBlocks.SOLAR_COOKER.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();
        if (block instanceof AbstractSolarCookerBlock) {
            //AbstractSolarCookerBlock abstractBlock = (AbstractSolarCookerBlock)block;
            matrixStackIn.pushPose();
            float f = blockstate.getValue(AbstractSolarCookerBlock.FACING).toYRot();
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            float lidAngle = tileEntityIn.getLidAngle(partialTicks);
            this.renderModels(matrixStackIn, ivertexbuilder, this.singleLid, this.singleLatch, this.singleBottom, this.singleInner, lidAngle, combinedLightIn, combinedOverlayIn);

            //render item
            if (isInWorld) {
                ItemStack stack = tileEntityIn.getItem(0);
                if (!stack.isEmpty()) {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.5, 0.4, 0.5);
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                    matrixStackIn.popPose();
                }
            }

            matrixStackIn.popPose();
        }
    }

    private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer chestLid, ModelRenderer chestLatch, ModelRenderer chestBottom, ModelRenderer chestInner, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        chestLid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        chestLatch.xRot = chestLid.xRot;
        chestLid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestLatch.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestBottom.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestInner.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
