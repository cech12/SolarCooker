package de.cech12.solarcooker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.block.AbstractSolarCookerBlock;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Map;

public class SolarCookerBlockEntityRenderer implements BlockEntityRenderer<SolarCookerBlockEntity, SolarCookerRenderState> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Constants.id("solar_cooker"), "main");

    private static final Map<Direction, Transformation> TRANSFORMATIONS;
    private static final SpriteId MATERIAL_BASE = Sheets.CHEST_MAPPER.apply(Constants.id("solar_cooker"));

    private final SpriteGetter sprites;
    private final ItemModelResolver itemModelResolver;
    private final SolarCookerModel model;

    static {
        TRANSFORMATIONS = Util.makeEnumMap(Direction.class, SolarCookerBlockEntityRenderer::createModelTransformation);
    }

    public SolarCookerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.itemModelResolver = context.itemModelResolver();
        this.model = new SolarCookerModel(context.bakeLayer(SolarCookerBlockEntityRenderer.MODEL_LAYER_LOCATION));
    }

    @Override
    @NotNull
    public SolarCookerRenderState createRenderState() {
        return new SolarCookerRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SolarCookerBlockEntity blockEntity, @NotNull SolarCookerRenderState state, float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getLevel() != null ? blockEntity.getBlockState() : Constants.SOLAR_COOKER_BLOCK.get().defaultBlockState().setValue(AbstractSolarCookerBlock.FACING, Direction.SOUTH);
        state.facing = blockState.getValue(AbstractSolarCookerBlock.FACING);
        state.open = blockEntity.getOpenNess(partialTicks);
        state.hasLeftReflector = blockEntity.hasLeftReflector();
        state.hasRightReflector = blockEntity.hasRightReflector();
        ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(itemStackRenderState, blockEntity.getItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        state.stack = itemStackRenderState;
    }

    @Override
    public void submit(@NotNull SolarCookerRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(modelTransformation(state.facing));
        submitNodeCollector.submitModel(model, state, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, MATERIAL_BASE, this.sprites, 0, state.breakProgress);
        //render item
        ItemStackRenderState itemStackRenderState = state.stack;
        if (!itemStackRenderState.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.55, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            itemStackRenderState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public static Transformation modelTransformation(Direction facing) {
        return TRANSFORMATIONS.get(facing);
    }

    private static Transformation createModelTransformation(Direction facing) {
        return new Transformation((new Matrix4f()).rotationAround(Axis.YP.rotationDegrees(-facing.toYRot()), 0.5F, 0.0F, 0.5F));
    }
}
