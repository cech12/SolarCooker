package cech12.solarcooker.unit;

/*
import cech12.solarcooker.api.block.SolarCookerBlocks;
import cech12.solarcooker.block.ReflectorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectorTests {

    @Test
    public void testReflectorRotationOneSide() {
        final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();
        final BlockPos pos = new BlockPos(0, 0, 0);
        BlockState reflector = SolarCookerBlocks.REFLECTOR.defaultBlockState();
        reflector = reflector.setValue(ReflectorBlock.TYPE, ReflectorBlock.getType(Direction.NORTH));
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should face to north at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should not face to east at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should not face to south at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.CLOCKWISE_90);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should face to east after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should not face to north after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should not face to south after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.COUNTERCLOCKWISE_90);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should face to north after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should not face to east after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should not face to south after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.CLOCKWISE_180);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should face to south after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should not face to north after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should not face to east after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
    }

    @Test
    public void testReflectorRotationMultipleSides() {
        final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();
        final BlockPos pos = new BlockPos(0, 0, 0);
        BlockState reflector = SolarCookerBlocks.REFLECTOR.defaultBlockState();
        reflector = reflector.setValue(ReflectorBlock.TYPE, 12); //north & east
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should face to north at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should face to east at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should not face to south at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west at the beginning: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.CLOCKWISE_90);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should face to east after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should face to south after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should not face to north after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west after first rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.COUNTERCLOCKWISE_90);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should face to north after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should face to east after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should not face to south after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should not face to west after second rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        reflector = reflector.rotate(world, pos, Rotation.CLOCKWISE_180);
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.SOUTH), "Reflector should face to south after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertTrue(ReflectorBlock.isFacingTo(reflector, Direction.WEST), "Reflector should face to west after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.NORTH), "Reflector should not face to north after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
        assertFalse(ReflectorBlock.isFacingTo(reflector, Direction.EAST), "Reflector should not face to east after third rotation: " + reflector.getValue(ReflectorBlock.TYPE));
    }

}
 */
