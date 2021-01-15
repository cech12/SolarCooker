package cech12.solarcooker.inventory;

import cech12.solarcooker.tileentity.AbstractSolarCookerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SolarCookerResultSlot extends FurnaceResultSlot {
    private final PlayerEntity player;

    public SolarCookerResultSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    protected void onCrafting(@Nonnull ItemStack stack) {
        super.onCrafting(stack);
        if (!this.player.world.isRemote && this.inventory instanceof AbstractSolarCookerTileEntity) {
            ((AbstractSolarCookerTileEntity)this.inventory).func_235645_d_(this.player);
        }
    }
}
