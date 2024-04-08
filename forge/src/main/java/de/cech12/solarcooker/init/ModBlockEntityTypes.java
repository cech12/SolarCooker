package de.cech12.solarcooker.init;

import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.ForgeSolarCookerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlockEntityTypes {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

    static {
        Constants.SOLAR_COOKER_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register(Constants.SOLAR_COOKER_NAME, () -> BlockEntityType.Builder.of(ForgeSolarCookerBlockEntity::new, Constants.SOLAR_COOKER_BLOCK.get()).build(null));
    }

}
