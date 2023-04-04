package cech12.solarcooker.client;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.block.AbstractSolarCookerBlock;
import cech12.solarcooker.init.ModBlocks;
import cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SolarCookerBlockEntityRenderer implements BlockEntityRenderer<SolarCookerBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(SolarCookerMod.MOD_ID, "textures/entity/solar_cooker.png");

    private static final LayerDefinition innerCube = createInnerLayerDefinition();

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart inner;
    private final ModelPart lock;

    private static LayerDefinition createInnerLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 43).addBox(3.0F, 2.0F, 3.0F, 10.0F, 8.0F, 10.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public SolarCookerBlockEntityRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        ModelPart modelpart = rendererProvider.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
        this.inner = innerCube.bakeRoot().getChild("inner");
    }

    @Override
    public void render(SolarCookerBlockEntity blockEntity, float partialTicks, @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = blockEntity.getLevel();
        boolean isInWorld = world != null;
        BlockState blockstate = isInWorld ? blockEntity.getBlockState() : ModBlocks.SOLAR_COOKER.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();
        if (block instanceof AbstractSolarCookerBlock) {
            //AbstractSolarCookerBlock abstractBlock = (AbstractSolarCookerBlock)block;
            matrixStackIn.pushPose();
            float f = blockstate.getValue(AbstractSolarCookerBlock.FACING).toYRot();
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f));
            matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            float lidAngle = blockEntity.getOpenNess(partialTicks);
            this.renderModels(matrixStackIn, ivertexbuilder, this.lid, this.lock, this.bottom, this.inner, lidAngle, combinedLightIn, combinedOverlayIn);

            //render item
            if (isInWorld) {
                ItemStack stack = blockEntity.getItem(0);
                if (!stack.isEmpty()) {
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0.5, 0.4, 0.5);
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, world, 0);
                    matrixStackIn.popPose();
                }
            }

            matrixStackIn.popPose();
        }
    }

    private void renderModels(PoseStack matrixStackIn, VertexConsumer bufferIn, ModelPart chestLid, ModelPart chestLatch, ModelPart chestBottom, ModelPart chestInner, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        chestLid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        chestLatch.xRot = chestLid.xRot;
        chestLid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestLatch.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestBottom.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        chestInner.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
