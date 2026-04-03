package de.cech12.solarcooker.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class SolarCookerModel extends Model<SolarCookerRenderState> {

    private final ModelPart lid;
    private final ModelPart lock;
    private final ModelPart reflectorLeft;
    private final ModelPart reflectorRight;

    public SolarCookerModel(final ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.lid = root.getChild("lid");
        this.lock = root.getChild("lock");
        this.reflectorLeft = root.getChild("reflectorLeft");
        this.reflectorRight = root.getChild("reflectorRight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
        root.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        root.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        Set<Direction> innerDirections = EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN);
        root.addOrReplaceChild("inner", CubeListBuilder.create().texOffs(0, 43).addBox(3.0F, 2.0F, 3.0F, 10.0F, 8.0F, 10.0F, innerDirections), PartPose.ZERO);
        root.addOrReplaceChild("reflectorLeft", CubeListBuilder.create().texOffs(22, 45).addBox(0F, 0F, 0F, 13.998F, 1.0F, 6.999F), PartPose.offsetAndRotation(1.001F, 10.001F, 14.999F, 0F, (float) Math.PI / 2F, 0F));
        root.addOrReplaceChild("reflectorRight", CubeListBuilder.create().texOffs(22, 45).addBox(0F, 0F, 0F, 13.998F, 1.0F, 6.999F), PartPose.offsetAndRotation(14.999F, 10.001F, 1.001F, 0F, (float) Math.PI / 2F * 3F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public void setupAnim(@NotNull final SolarCookerRenderState state) {
        super.setupAnim(state);
        float open = state.open;
        open = 1.0F - open;
        open = 1.0F - open * open * open;
        this.lid.xRot = -(open * (float) (Math.PI / 2));
        this.lock.xRot = this.lid.xRot;
        this.reflectorLeft.visible = open > 0.001F && state.hasLeftReflector;
        this.reflectorRight.visible = open > 0.001F && state.hasRightReflector;
        if (this.reflectorLeft.visible || this.reflectorRight.visible) {
            float reflectorAngle = (open * ((float)Math.PI / 1.8F));
            if (this.reflectorLeft.visible) {
                this.reflectorLeft.zRot = reflectorAngle;
            }
            if (this.reflectorRight.visible) {
                this.reflectorRight.zRot = -reflectorAngle;
            }
        }
    }

}
