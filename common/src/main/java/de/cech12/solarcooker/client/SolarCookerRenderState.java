package de.cech12.solarcooker.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class SolarCookerRenderState extends BlockEntityRenderState {

    public float open;
    public float angle;
    public boolean hasLeftReflector;
    public boolean hasRightReflector;
    public ItemStackRenderState stack;

    public SolarCookerRenderState() {
        this.open = 0;
        this.angle = 0;
        this.hasLeftReflector = false;
        this.hasRightReflector = false;
        this.stack = new ItemStackRenderState();
    }

}
