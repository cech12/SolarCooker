package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.NeoForgeSolarCookerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntityTypes {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register(Constants.SOLAR_COOKER_NAME, () -> BlockEntityType.Builder.of(NeoForgeSolarCookerBlockEntity::new, Constants.SOLAR_COOKER_BLOCK.get()).build(null));
    }

}
