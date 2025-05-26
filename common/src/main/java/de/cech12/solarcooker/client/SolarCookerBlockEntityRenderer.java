package de.cech12.solarcooker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.AbstractSolarCookerBlock;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class SolarCookerBlockEntityRenderer implements BlockEntityRenderer<SolarCookerBlockEntity> {

    public static final ResourceLocation TEXTURE = Constants.id("textures/entity/chest/solar_cooker.png");
    public static final ResourceLocation TEXTURE_REFLECTOR = Constants.id("textures/entity/chest/solar_cooker_reflector.png");

    private static final LayerDefinition innerCube = createInnerLayerDefinition();
    private static final LayerDefinition reflectorLeftCube = createReflectorLayerDefinition(true);
    private static final LayerDefinition reflectorRightCube = createReflectorLayerDefinition(false);

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart inner;
    private final ModelPart lock;
    private final ModelPart reflectorLeft;
    private final ModelPart reflectorRight;

    private static LayerDefinition createInnerLayerDefinition() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 43).addBox(3.0F, 2.0F, 3.0F, 10.0F, 8.0F, 10.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private static LayerDefinition createReflectorLayerDefinition(boolean left) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        float offset = left ? 0F : 13.998F;
        float rotation = left ? 0F : (float) Math.PI;
        partdefinition.addOrReplaceChild("reflector", CubeListBuilder.create().texOffs(0, 0).addBox(0, 0, 0, 6.999F, 1.0F, 13.998F), PartPose.offsetAndRotation(1.001F + offset, 10.001F, 1.001F + offset, 0, rotation, 0));
        return LayerDefinition.create(meshdefinition, 42, 15);
    }

    public SolarCookerBlockEntityRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        ModelPart modelpart = rendererProvider.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
        this.inner = innerCube.bakeRoot().getChild("inner");
        this.reflectorLeft = reflectorLeftCube.bakeRoot().getChild("reflector");
        this.reflectorRight = reflectorRightCube.bakeRoot().getChild("reflector");
    }

    @Override
    public void render(SolarCookerBlockEntity blockEntity, float partialTicks, @Nonnull PoseStack matrixStackIn, @Nonnull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = blockEntity.getLevel();
        boolean isInWorld = world != null;
        BlockState blockstate = isInWorld ? blockEntity.getBlockState() : Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = blockstate.getBlock();
        if (block instanceof AbstractSolarCookerBlock) {
            matrixStackIn.pushPose();
            float f = blockstate.getValue(AbstractSolarCookerBlock.FACING).toYRot();
            matrixStackIn.translate(0.5D, 0.5D, 0.5D);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f));
            matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
            VertexConsumer vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE));
            float lidAngle = blockEntity.getOpenNess(partialTicks);
            this.renderModels(matrixStackIn, vertexConsumer, lidAngle, combinedLightIn, combinedOverlayIn);
            if (lidAngle > 0) {
                if (blockEntity.hasLeftReflector() || blockEntity.hasRightReflector()) {
                    vertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE_REFLECTOR));
                    this.renderReflectors(matrixStackIn, vertexConsumer, blockEntity.hasLeftReflector(), blockEntity.hasRightReflector(), lidAngle, combinedLightIn, combinedOverlayIn);
                }
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
            }

            matrixStackIn.popPose();
        }
    }

    private void renderModels(PoseStack matrixStackIn, VertexConsumer bufferIn, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        this.lid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        this.lock.xRot = this.lid.xRot;
        this.lid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        this.lock.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        this.bottom.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        this.inner.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }

    private void renderReflectors(PoseStack matrixStackIn, VertexConsumer bufferIn, boolean hasLeftReflector, boolean hasRightReflector, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        float angle = (lidAngle * ((float)Math.PI / 1.8F));
        if (hasLeftReflector) {
            this.reflectorLeft.zRot = angle;
            this.reflectorLeft.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (hasRightReflector) {
            this.reflectorRight.zRot = -angle;
            this.reflectorRight.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}
