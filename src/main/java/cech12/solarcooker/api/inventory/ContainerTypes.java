package cech12.solarcooker.api.inventory;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.crafting.RecipeTypes;
import cech12.solarcooker.inventory.SolarCookerContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;

public class ContainerTypes {

    public final static ResourceLocation SOLAR_COOKER_ID = new ResourceLocation(SolarCookerMod.MOD_ID, "solarcooker");

    public static ContainerType<? extends Container> SOLAR_COOKER = IForgeContainerType.create((pWindowID, pInventory, pData) -> {
        BlockPos pos = pData.readBlockPos();
        return new SolarCookerContainer(RecipeTypes.SOLAR_COOKING, pWindowID, pInventory, pos);
    }).setRegistryName(SOLAR_COOKER_ID);

}
