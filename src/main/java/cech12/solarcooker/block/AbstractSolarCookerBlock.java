package cech12.solarcooker.block;

import cech12.solarcooker.tileentity.AbstractSolarCookerTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public abstract class AbstractSolarCookerBlock extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty SUNLIT = BlockStateProperties.LIT;
    public static final BooleanProperty BURNING = BlockStateProperties.ENABLED;

    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    protected AbstractSolarCookerBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUNLIT, false).setValue(BURNING, false));
    }

    @Override
    public boolean isToolEffective(BlockState state, ToolType tool) {
        return tool == ToolType.AXE;
    }

    @Override
    @Nonnull
    public ActionResultType use(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            this.interactWith(worldIn, pos, player);
            return ActionResultType.CONSUME;
        }
    }

    /**
     * Interface for handling interaction with blocks that impliment AbstractFurnaceBlock. Called in onBlockActivated
     * inside AbstractFurnaceBlock.
     */
    protected abstract void interactWith(World worldIn, BlockPos pos, PlayerEntity player);

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Called by ItemBlocks after a block is set in the world
     */
    @Override
    public void setPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof AbstractSolarCookerTileEntity) {
                ((AbstractSolarCookerTileEntity)tileentity).setCustomName(stack.getHoverName());
            }
        }

    }

    @Override
    @Deprecated
    public void onRemove(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof AbstractSolarCookerTileEntity) {
                InventoryHelper.dropContents(worldIn, pos, (AbstractSolarCookerTileEntity)tileentity);
                ((AbstractSolarCookerTileEntity)tileentity).getRecipesToAwardAndPopExperience(worldIn, Vector3d.atLowerCornerOf(pos));
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    @Deprecated
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @Deprecated
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, World worldIn, @Nonnull BlockPos pos) {
        return Container.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model
     */
    @Override
    @Nonnull
    public BlockRenderType getRenderShape(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return SHAPE;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable
     */
    @Override
    @Deprecated
    @Nonnull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable
     */
    @Override
    @Deprecated
    @Nonnull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, SUNLIT, BURNING);
    }
}
