package de.cech12.solarcooker.platform;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import de.cech12.solarcooker.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public SolarCookerBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new SolarCookerBlockEntity(pos, state);
    }

}
