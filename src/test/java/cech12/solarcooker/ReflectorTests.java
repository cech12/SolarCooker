package cech12.solarcooker;

import cech12.solarcooker.block.ReflectorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(SolarCookerMod.MOD_ID)
public class ReflectorTests {

    private static final BlockPos REFLECTOR_POSITION = new BlockPos(0, 1, 0);

    @GameTest(template = "north")
    public static void testClockwiseRotationOfNorthFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.CLOCKWISE_90));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.EAST),
                () -> "North facing reflector is not facing to east after clockwise rotation."));
    }

    @GameTest(template = "north")
    public static void testCounterClockwiseRotationOfNorthFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.COUNTERCLOCKWISE_90));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.WEST),
                () -> "North facing reflector is not facing to west after counter clockwise rotation."));
    }

    @GameTest(template = "north")
    public static void testClockwise180RotationOfNorthFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.CLOCKWISE_180));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.SOUTH),
                () -> "North facing reflector is not facing to south after clockwise 180 rotation."));
    }

    @GameTest(template = "north_east")
    public static void testClockwiseRotationOfNorthEastFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.CLOCKWISE_90));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.SOUTH) && ReflectorBlock.isFacingTo(blockState, Direction.EAST),
                () -> "North-east facing reflector is not facing to south-east after clockwise rotation."));
    }

    @GameTest(template = "north_east")
    public static void testCounterClockwiseRotationOfNorthEastFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.COUNTERCLOCKWISE_90));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.NORTH) && ReflectorBlock.isFacingTo(blockState, Direction.WEST),
                () -> "North-east facing reflector is not facing to north-west after counter clockwise rotation."));
    }

    @GameTest(template = "north_east")
    public static void testClockwise180RotationOfNorthEastFacingReflector(GameTestHelper test) {
        test.setBlock(REFLECTOR_POSITION, test.getBlockState(REFLECTOR_POSITION).rotate(test.getLevel(), REFLECTOR_POSITION, Rotation.CLOCKWISE_180));
        test.succeedIf(() -> test.assertBlockState(REFLECTOR_POSITION,
                blockState -> ReflectorBlock.isFacingTo(blockState, Direction.SOUTH) && ReflectorBlock.isFacingTo(blockState, Direction.WEST),
                () -> "North-east facing reflector is not facing to south-west after clockwise 180 rotation."));
    }

}
