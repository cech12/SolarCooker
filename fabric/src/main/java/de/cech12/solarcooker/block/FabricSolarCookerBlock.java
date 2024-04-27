package de.cech12.solarcooker.block;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FabricSolarCookerBlock extends SolarCookerBlock {

    public FabricSolarCookerBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected void interactWith(Level worldIn, @NotNull BlockPos pos, @NotNull Player player) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof SolarCookerBlockEntity container) {
            player.openMenu(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf data) {
                    //do nothing
                }

                @Nonnull
                @Override
                public Component getDisplayName(){
                    return container.getDisplayName();
                }

                @Override
                public AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory inventory, @Nonnull Player player) {
                    return container.createMenu(windowId, inventory, player);
                }
            });
            player.awardStat(Stats.INSPECT_HOPPER);
        }
    }

}
