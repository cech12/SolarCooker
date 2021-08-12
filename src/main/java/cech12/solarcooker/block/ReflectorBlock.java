package cech12.solarcooker.block;

import cech12.solarcooker.api.block.SolarCookerBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class ReflectorBlock extends Block {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 15);

    protected static final List<Integer> TYPES_WITH_ONE_REFLECTOR = Arrays.asList(1, 2, 4, 8);
    protected static final List<Integer> TYPES_WITH_TWO_REFLECTORS = Arrays.asList(3, 5, 6, 9, 10, 12);
    protected static final List<Integer> TYPES_WITH_THREE_REFLECTORS = Arrays.asList(7, 11, 13, 14);
    protected static final List<Integer> TYPES_WITH_FOUR_REFLECTORS = Collections.singletonList(15);

    protected static final VoxelShape SHAPE_NORTH_WEST = Block.box(0.0D, 0.0D, 0.0D, 5.0D, 16.0D, 5.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(5.0D, 0.0D, 0.0D, 11.0D, 16.0D, 5.0D);
    protected static final VoxelShape SHAPE_NORTH_EAST = Block.box(11.0D, 0.0D, 0.0D, 16.0D, 16.0D, 5.0D);
    protected static final VoxelShape SHAPE_EAST = Block.box(11.0D, 0.0D, 5.0D, 16.0D, 16.0D, 11.0D);
    protected static final VoxelShape SHAPE_SOUTH_EAST = Block.box(11.0D, 0.0D, 11.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(5.0D, 0.0D, 11.0D, 11.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH_WEST = Block.box(0.0D, 0.0D, 11.0D, 5.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 5.0D, 5.0D, 16.0D, 11.0D);
    protected static final VoxelShape[] SHAPES = makeShapes();

    private static VoxelShape[] makeShapes() {
        return IntStream.range(0, 16).mapToObj((type) -> {
            Set<VoxelShape> shapes = new HashSet<>();
            if ((type | 8) == type) {
                shapes.add(SHAPE_NORTH_WEST);
                shapes.add(SHAPE_NORTH);
                shapes.add(SHAPE_NORTH_EAST);
            }
            if ((type | 4) == type) {
                shapes.add(SHAPE_NORTH_EAST);
                shapes.add(SHAPE_EAST);
                shapes.add(SHAPE_SOUTH_EAST);
            }
            if ((type | 2) == type) {
                shapes.add(SHAPE_SOUTH_EAST);
                shapes.add(SHAPE_SOUTH);
                shapes.add(SHAPE_SOUTH_WEST);
            }
            if ((type | 1) == type) {
                shapes.add(SHAPE_SOUTH_WEST);
                shapes.add(SHAPE_WEST);
                shapes.add(SHAPE_NORTH_WEST);
            }
            VoxelShape voxelshape = Block.box(0, 0, 0, 0, 0, 0);
            for (VoxelShape shape : shapes) {
                voxelshape = Shapes.or(voxelshape, shape);
            }
            return voxelshape;
        }).toArray(VoxelShape[]::new);
    }

    public static int getType(Direction direction) {
        switch (direction) {
            case NORTH: return 8;
            case EAST: return 4;
            case SOUTH: return 2;
            case WEST: return 1;
        }
        return 8;
    }

    public static boolean isFacingTo(BlockState blockstate, Direction direction) {
        if (direction != Direction.UP && direction != Direction.DOWN) {
            int type = blockstate.getValue(TYPE);
            return (type | getType(direction)) == type;
        }
        return false;
    }

    private static int getCount(int type) {
        if (TYPES_WITH_ONE_REFLECTOR.contains(type)) {
            return 1;
        }
        if (TYPES_WITH_TWO_REFLECTORS.contains(type)) {
            return 2;
        }
        if (TYPES_WITH_THREE_REFLECTORS.contains(type)) {
            return 3;
        }
        if (TYPES_WITH_FOUR_REFLECTORS.contains(type)) {
            return 4;
        }
        return 1;
    }

    public ReflectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, getType(Direction.NORTH)));
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder context) {
        List<ItemStack> drops = super.getDrops(state, context);
        int count = getCount(state.getValue(TYPE));
        if (!drops.isEmpty() && count > 1) {
            //set the count of the dropped reflector blocks
            ItemStack stack = drops.get(0);
            if (stack.getItem().equals(SolarCookerBlocks.REFLECTOR.asItem())) {
                stack.setCount(count);
            }
        }
        return drops;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("item.solarcooker.reflector.description").withStyle(ChatFormatting.BLUE));
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPES[state.getValue(TYPE)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        Direction direction = context.getHorizontalDirection().getOpposite();
        if (blockstate.is(this)) {
            return blockstate.setValue(TYPE, blockstate.getValue(TYPE) | getType(direction));
        } else {
            return this.defaultBlockState().setValue(TYPE, getType(direction));
        }
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().getItem() == this.asItem() && !isFacingTo(state, context.getHorizontalDirection().getOpposite()) || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
        int bits;
        if (direction == Rotation.CLOCKWISE_90) {
            bits = 1;
        } else if (direction == Rotation.CLOCKWISE_180) {
            bits = 2;
        } else if (direction == Rotation.COUNTERCLOCKWISE_90) {
            bits = 3;
        } else {
            return state;
        }
        int type = state.getValue(TYPE);
        int rotated = ((type >> bits) | type << (4 - bits)) & 0xF;
        return state.setValue(TYPE, rotated);
    }

}
