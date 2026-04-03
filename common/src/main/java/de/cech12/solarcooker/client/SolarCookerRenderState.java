package de.cech12.solarcooker.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class SolarCookerRenderState extends BlockEntityRenderState {

    public float open = 0;
    public Direction facing = Direction.SOUTH;
    public boolean hasLeftReflector = false;
    public boolean hasRightReflector = false;
    public ItemStackRenderState stack = new ItemStackRenderState();

}
