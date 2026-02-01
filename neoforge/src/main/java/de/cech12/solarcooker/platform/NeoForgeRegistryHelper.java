package de.cech12.solarcooker.platform;

import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import de.cech12.solarcooker.platform.services.IRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    @Override
    public SolarCookerBlockEntity getNewBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SolarCookerBlockEntity(pos, state);
    }

}
