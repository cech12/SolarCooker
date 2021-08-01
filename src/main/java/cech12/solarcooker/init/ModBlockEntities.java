package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.api.blockentity.SolarCookerBlockEntities;
import cech12.solarcooker.tileentity.SolarCookerBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlockEntities {

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        SolarCookerBlockEntities.SOLAR_COOKER = register(SolarCookerBlockEntity::new, "solar_cooker", SolarCookerBlocks.SOLAR_COOKER, event);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType.BlockEntitySupplier<T> supplier, String registryName, Block block, RegistryEvent.Register<BlockEntityType<?>> registryEvent) {
        BlockEntityType<T> tileEntityType = BlockEntityType.Builder.of(supplier, block).build(null);
        tileEntityType.setRegistryName(registryName);
        registryEvent.getRegistry().register(tileEntityType);
        return tileEntityType;
    }

}
