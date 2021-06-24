package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.api.tileentity.SolarCookerTileEntities;
import cech12.solarcooker.tileentity.SolarCookerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModTileEntities {

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        SolarCookerTileEntities.SOLAR_COOKER = register(SolarCookerTileEntity::new, "solar_cooker", SolarCookerBlocks.SOLAR_COOKER, event);
    }

    private static <T extends TileEntity> TileEntityType<T> register(Supplier<T> supplier, String registryName, Block block, RegistryEvent.Register<TileEntityType<?>> registryEvent) {
        TileEntityType<T> tileEntityType = TileEntityType.Builder.of(supplier, block).build(null);
        tileEntityType.setRegistryName(registryName);
        registryEvent.getRegistry().register(tileEntityType);
        return tileEntityType;
    }

}
