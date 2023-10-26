package cech12.solarcooker.blockentity;

import cech12.solarcooker.init.ModBlockEntityTypes;
import cech12.solarcooker.init.ModRecipeTypes;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class SolarCookerBlockEntity extends AbstractSolarCookerBlockEntity {

    public SolarCookerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SOLAR_COOKER.get(), pos, state, ModRecipeTypes.SOLAR_COOKING.get());
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable("block.solarcooker.solar_cooker");
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new SolarCookerContainer(ModRecipeTypes.SOLAR_COOKING.get(), id, player, this, this.dataAccess);
    }

}
