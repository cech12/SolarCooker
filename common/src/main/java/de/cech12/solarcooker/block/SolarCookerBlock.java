package de.cech12.solarcooker.block;

import com.mojang.serialization.MapCodec;
import de.cech12.solarcooker.Constants;
import de.cech12.solarcooker.blockentity.SolarCookerBlockEntity;
import de.cech12.solarcooker.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolarCookerBlock extends AbstractSolarCookerBlock {

    public static final MapCodec<AbstractSolarCookerBlock> CODEC = simpleCodec(SolarCookerBlock::new);

    public SolarCookerBlock(BlockBehaviour.Properties builder) {
        super(builder);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return Services.REGISTRY.getNewBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> entityType) {
        if (level.isClientSide()) {
            return createTickerHelper(entityType, Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntity::lidAnimateTick);
        }
        return createTickerHelper(entityType, Constants.SOLAR_COOKER_ENTITY_TYPE.get(), SolarCookerBlockEntity::tick);
    }

    @Override
    protected void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof SolarCookerBlockEntity solarCookerBlockEntity) {
            solarCookerBlockEntity.recheckOpen();
        }
    }

    /**
     * Interface for handling interaction with blocks that implement AbstractSolarCookerBlock. Called in onBlockActivated
     * inside AbstractSolarCookerBlock.
     */
    @Override
    protected void interactWith(Level level, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SolarCookerBlockEntity && player instanceof ServerPlayer) {
            player.openMenu((SolarCookerBlockEntity) blockEntity);
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(BlockState stateIn, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        if (stateIn.getValue(BURNING)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playLocalSound(d0, d1, d2, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.6D, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}