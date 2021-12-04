package cech12.solarcooker.api.inventory;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.extensions.IForgeMenuType;

public class ContainerTypes {

    public final static ResourceLocation SOLAR_COOKER_ID = new ResourceLocation(SolarCookerMod.MOD_ID, "solarcooker");

    public static MenuType<? extends AbstractContainerMenu> SOLAR_COOKER = IForgeMenuType.create((pWindowID, pInventory, pData) -> {
        BlockPos pos = pData.readBlockPos();
        return new SolarCookerContainer(RecipeTypes.SOLAR_COOKING, pWindowID, pInventory, pos);
    }).setRegistryName(SOLAR_COOKER_ID);

}
