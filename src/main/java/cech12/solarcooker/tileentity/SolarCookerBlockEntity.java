package cech12.solarcooker.tileentity;

import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.api.blockentity.SolarCookerBlockEntities;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class SolarCookerBlockEntity extends AbstractSolarCookerBlockEntity {

    public SolarCookerBlockEntity(BlockPos pos, BlockState state) {
        super(SolarCookerBlockEntities.SOLAR_COOKER, pos, state, RecipeTypes.SOLAR_COOKING);
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return new TranslatableComponent("block.solarcooker.solar_cooker");
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new SolarCookerContainer(RecipeTypes.SOLAR_COOKING, id, player, this);
    }

}
