package cech12.solarcooker.tileentity;

import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.api.tileentity.SolarCookerTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class SolarCookerTileEntity extends AbstractSolarCookerTileEntity {

    public SolarCookerTileEntity() {
        super(SolarCookerTileEntities.SOLAR_COOKER, RecipeTypes.SOLAR_COOKING);
    }

    @Override
    @Nonnull
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.solarcooker.solar_cooker");
    }

    @Override
    @Nonnull
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new FurnaceContainer(id, player, this, this.furnaceData);
    }

}
