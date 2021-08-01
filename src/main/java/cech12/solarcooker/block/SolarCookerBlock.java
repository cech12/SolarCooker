package cech12.solarcooker.block;

import cech12.solarcooker.api.blockentity.SolarCookerBlockEntities;
import cech12.solarcooker.tileentity.AbstractSolarCookerBlockEntity;
import cech12.solarcooker.tileentity.SolarCookerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SolarCookerBlock extends AbstractSolarCookerBlock {

    public SolarCookerBlock(BlockBehaviour.Properties builder) {
        super(builder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new SolarCookerBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@Nonnull Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, (BlockEntityType<AbstractSolarCookerBlockEntity>) SolarCookerBlockEntities.SOLAR_COOKER, AbstractSolarCookerBlockEntity::tick);
    }

    /**
     * Interface for handling interaction with blocks that implement AbstractSolarCookerBlock. Called in onBlockActivated
     * inside AbstractSolarCookerBlock.
     */
    @Override
    protected void interactWith(Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof SolarCookerBlockEntity && player instanceof ServerPlayer) {
            //player.openContainer((SolarCookerTileEntity) tileentity);
            NetworkHooks.openGui((ServerPlayer) player, (SolarCookerBlockEntity) blockEntity, pos);
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
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