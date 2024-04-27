package de.cech12.solarcooker.init;

import de.cech12.solarcooker.CommonLoader;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntityTypes {

    private static final BlockEntityType<? extends SolarCookerBlockEntity> SOLAR_COOKER_ENTITY_TYPE = register(Constants.SOLAR_COOKER_NAME, BlockEntityType.Builder.of(SolarCookerBlockEntity::new, Constants.SOLAR_COOKER_BLOCK.get()));

    static {
        Constants.SOLAR_COOKER_ENTITY_TYPE = () -> SOLAR_COOKER_ENTITY_TYPE;
    }

    public static void init() {}

    private static BlockEntityType<? extends SolarCookerBlockEntity> register(String name, BlockEntityType.Builder<? extends SolarCookerBlockEntity> blockEntityTypeBuilder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, CommonLoader.id(name), blockEntityTypeBuilder.build(null));
    }

}
