package cech12.solarcooker.init;

import cech12.solarcooker.SolarCookerMod;
import cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid= SolarCookerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlockEntityTypes {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SolarCookerMod.MOD_ID);
    public static RegistryObject<BlockEntityType<SolarCookerBlockEntity>> SOLAR_COOKER = BLOCK_ENTITY_TYPES.register("solar_cooker", () -> BlockEntityType.Builder.of(SolarCookerBlockEntity::new, ModBlocks.SOLAR_COOKER.get()).build(null));

}
