package de.cech12.solarcooker.platform.services;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Common registry helper service interface.
 */
public interface IRegistryHelper {

    SolarCookerBlockEntity getNewBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state);

}
