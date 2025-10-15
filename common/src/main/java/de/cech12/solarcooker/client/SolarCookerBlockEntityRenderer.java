package de.cech12.solarcooker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.AbstractSolarCookerBlock;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolarCookerBlockEntityRenderer implements BlockEntityRenderer<SolarCookerBlockEntity, SolarCookerRenderState> {

    private static final Material MATERIAL_BASE = Sheets.CHEST_MAPPER.apply(Constants.id("solar_cooker"));
    private static final Material MATERIAL_REFLECTOR = Sheets.CHEST_MAPPER.apply(Constants.id("solar_cooker_reflector"));

    private static final LayerDefinition innerCube = createInnerLayerDefinition();
    private static final LayerDefinition reflectorLeftCube = createReflectorLayerDefinition(true);
    private static final LayerDefinition reflectorRightCube = createReflectorLayerDefinition(false);

    private final MaterialSet materials;
    private final ItemModelResolver itemModelResolver;
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
        this.materials = rendererProvider.materials();
        this.itemModelResolver = rendererProvider.itemModelResolver();
        ModelPart modelpart = rendererProvider.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
        this.inner = innerCube.bakeRoot().getChild("inner");
        this.reflectorLeft = reflectorLeftCube.bakeRoot().getChild("reflector");
        this.reflectorRight = reflectorRightCube.bakeRoot().getChild("reflector");
    }

    @Override
    public SolarCookerRenderState createRenderState() {
        return new SolarCookerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SolarCookerBlockEntity blockEntity, @NotNull SolarCookerRenderState blockEntityRenderState, float partialTicks, @NotNull Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, partialTicks, vec, overlay);
        BlockState state = blockEntity.getLevel() != null ? blockEntity.getBlockState() : Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState().setValue(AbstractSolarCookerBlock.FACING, Direction.SOUTH);
        blockEntityRenderState.angle = (state.getValue(AbstractSolarCookerBlock.FACING)).toYRot();
        blockEntityRenderState.open = blockEntity.getOpenNess(partialTicks);
        blockEntityRenderState.hasLeftReflector = blockEntity.hasLeftReflector();
        blockEntityRenderState.hasRightReflector = blockEntity.hasRightReflector();
        ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(itemStackRenderState, blockEntity.getItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        blockEntityRenderState.stack = itemStackRenderState;
    }

    @Override
    public void submit(@NotNull SolarCookerRenderState blockEntityRenderState, @NotNull PoseStack matrixStackIn, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-blockEntityRenderState.angle));
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
        float lidAngle = blockEntityRenderState.open;
        this.renderModels(matrixStackIn, submitNodeCollector, lidAngle, blockEntityRenderState.lightCoords);
        if (lidAngle > 0) {
            if (blockEntityRenderState.hasLeftReflector || blockEntityRenderState.hasRightReflector) {
                this.renderReflectors(matrixStackIn, submitNodeCollector, blockEntityRenderState.hasLeftReflector, blockEntityRenderState.hasRightReflector, lidAngle, blockEntityRenderState.lightCoords);
            }
            //render item
            ItemStackRenderState itemStackRenderState = blockEntityRenderState.stack;
            if (!itemStackRenderState.isEmpty()) {
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5, 0.55, 0.5);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                itemStackRenderState.submit(matrixStackIn, submitNodeCollector, blockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                matrixStackIn.popPose();
            }
        }

        matrixStackIn.popPose();
    }

    private void renderModels(PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector, float lidAngle, int combinedLightIn) {
        this.lid.xRot = -(lidAngle * ((float)Math.PI / 2F));
        this.lock.xRot = this.lid.xRot;
        RenderType renderType = MATERIAL_BASE.renderType(RenderType::entityTranslucent);
        TextureAtlasSprite sprite = this.materials.get(MATERIAL_BASE);
        submitNodeCollector.submitModelPart(this.lid, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
        submitNodeCollector.submitModelPart(this.lock, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
        submitNodeCollector.submitModelPart(this.bottom, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
        submitNodeCollector.submitModelPart(this.inner, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
    }

    private void renderReflectors(PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector, boolean hasLeftReflector, boolean hasRightReflector, float lidAngle, int combinedLightIn) {
        if (hasLeftReflector || hasRightReflector) {
            RenderType renderType = MATERIAL_REFLECTOR.renderType(RenderType::entityTranslucent);
            TextureAtlasSprite sprite = this.materials.get(MATERIAL_REFLECTOR);
            float angle = (lidAngle * ((float)Math.PI / 1.8F));
            if (hasLeftReflector) {
                this.reflectorLeft.zRot = angle;
                submitNodeCollector.submitModelPart(this.reflectorLeft, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
            }
            if (hasRightReflector) {
                this.reflectorRight.zRot = -angle;
                submitNodeCollector.submitModelPart(this.reflectorRight, matrixStackIn, renderType, combinedLightIn, OverlayTexture.NO_OVERLAY, sprite);
            }
        }
    }
}
