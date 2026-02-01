package de.cech12.solarcooker.inventory;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SolarCookerResultSlot extends FurnaceResultSlot {
    private final Player player;

    public SolarCookerResultSlot(Player player, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    protected void checkTakeAchievements(@NotNull ItemStack stack) {
        super.checkTakeAchievements(stack);
        if (!this.player.level().isClientSide() && this.container instanceof SolarCookerBlockEntity) {
            ((SolarCookerBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
        }
    }
}
