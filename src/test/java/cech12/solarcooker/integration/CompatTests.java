package cech12.solarcooker.integration;

import cech12.solarcooker.block.ReflectorBlock;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

@IntegrationTestClass(value = "compat")
public class CompatTests {

    @IntegrationTest(value = "reflector_facing_property")
    public void testReflectorFacingPropertyCompatibility(IntegrationTestHelper helper) {
        final BlockPos northBlockPos = new BlockPos(0, 1, 0);
        final BlockPos eastBlockPos = new BlockPos(1, 1, 0);
        final BlockPos southBlockPos = new BlockPos(2, 1, 0);
        final BlockPos westBlockPos = new BlockPos(3, 1, 0);

        helper.assertBlockAt(northBlockPos, (blockState) -> blockState.getBlock() instanceof ReflectorBlock, "Block at north block position should be a Reflector.");
        helper.assertBlockAt(northBlockPos, (blockState) -> ReflectorBlock.isFacingTo(blockState, Direction.NORTH), "Reflector with north facing should face to north.");
        helper.assertBlockAt(northBlockPos, (blockState) -> blockState.getValue(ReflectorBlock.TYPE).equals(ReflectorBlock.getType(Direction.NORTH)), "Reflector with north facing should have the corresponding type.");
        helper.assertBlockAt(eastBlockPos, (blockState) -> blockState.getBlock() instanceof ReflectorBlock, "Block at east block position should be a Reflector.");
        helper.assertBlockAt(eastBlockPos, (blockState) -> ReflectorBlock.isFacingTo(blockState, Direction.EAST), "Reflector with east facing should face to east.");
        helper.assertBlockAt(eastBlockPos, (blockState) -> blockState.getValue(ReflectorBlock.TYPE).equals(ReflectorBlock.getType(Direction.EAST)), "Reflector with east facing should have the corresponding type.");
        helper.assertBlockAt(southBlockPos, (blockState) -> blockState.getBlock() instanceof ReflectorBlock, "Block at south block position should be a Reflector.");
        helper.assertBlockAt(southBlockPos, (blockState) -> ReflectorBlock.isFacingTo(blockState, Direction.SOUTH), "Reflector with south facing should face to south.");
        helper.assertBlockAt(southBlockPos, (blockState) -> blockState.getValue(ReflectorBlock.TYPE).equals(ReflectorBlock.getType(Direction.SOUTH)), "Reflector with south facing should have the corresponding type.");
        helper.assertBlockAt(westBlockPos, (blockState) -> blockState.getBlock() instanceof ReflectorBlock, "Block at west block position should be a Reflector.");
        helper.assertBlockAt(westBlockPos, (blockState) -> ReflectorBlock.isFacingTo(blockState, Direction.WEST), "Reflector with west facing should face to west.");
        helper.assertBlockAt(westBlockPos, (blockState) -> blockState.getValue(ReflectorBlock.TYPE).equals(ReflectorBlock.getType(Direction.WEST)), "Reflector with west facing should have the corresponding type.");
    }

}
