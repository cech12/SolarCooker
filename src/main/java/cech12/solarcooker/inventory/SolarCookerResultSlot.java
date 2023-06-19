package cech12.solarcooker.inventory;

import cech12.solarcooker.blockentity.AbstractSolarCookerBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class SolarCookerResultSlot extends FurnaceResultSlot {
    private final Player player;

    public SolarCookerResultSlot(Player player, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    protected void checkTakeAchievements(@Nonnull ItemStack stack) {
        super.checkTakeAchievements(stack);
        if (!this.player.level().isClientSide && this.container instanceof AbstractSolarCookerBlockEntity) {
            ((AbstractSolarCookerBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
        }
    }
}
