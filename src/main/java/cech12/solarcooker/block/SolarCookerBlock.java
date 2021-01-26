package cech12.solarcooker.block;

import cech12.solarcooker.tileentity.SolarCookerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.model.ItemCameraTransforms; //1.16
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class SolarCookerBlock extends AbstractSolarCookerBlock {

    public SolarCookerBlock(Block.Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
        return new SolarCookerTileEntity();
    }

    /**
     * Interface for handling interaction with blocks that implement AbstractSolarCookerBlock. Called in onBlockActivated
     * inside AbstractSolarCookerBlock.
     */
    @Override
    protected void interactWith(World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof SolarCookerTileEntity && player instanceof ServerPlayerEntity) {
            //player.openContainer((SolarCookerTileEntity) tileentity);
            NetworkHooks.openGui((ServerPlayerEntity) player, (SolarCookerTileEntity) tileentity, pos);
        }
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (stateIn.get(BURNING)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_SMOKER_SMOKE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.6D, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setISTER(Item.Properties props) {
        props.setISTER(() -> () -> new ItemStackTileEntityRenderer() {
            private SolarCookerTileEntity tile;

            @Override
            //render
            public void render(@Nonnull ItemStack stack, @Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer buffer, int x, int y) { //1.15
                if (tile == null) {
                    tile = new SolarCookerTileEntity();
                }
                TileEntityRendererDispatcher.instance.renderItem(tile, matrix, buffer, x, y);
            }
        });
    }
}