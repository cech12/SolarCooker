package de.cech12.solarcooker.inventory;

import de.cech12.solarcooker.ModTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SolarCookerReflectorSlot extends Slot {

    public SolarCookerReflectorSlot(Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(ModTags.Items.SOLAR_COOKER_REFLECTOR);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
