package de.cech12.solarcooker.platform;

import de.cech12.solarcooker.blockentity.AbstractSolarCookerBlockEntity;
import de.cech12.solarcooker.blockentity.NeoForgeSolarCookerBlockEntity;
import de.cech12.solarcooker.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public AbstractSolarCookerBlockEntity getNewBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new NeoForgeSolarCookerBlockEntity(pos, state);
    }

}
